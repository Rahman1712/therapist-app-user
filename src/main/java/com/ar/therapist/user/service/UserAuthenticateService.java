package com.ar.therapist.user.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ar.therapist.user.config.JwtService;
import com.ar.therapist.user.controller.UserRegisterException;
import com.ar.therapist.user.dto.AuthenticationRequest;
import com.ar.therapist.user.dto.AuthenticationResponse;
import com.ar.therapist.user.dto.UserRegisterRequest;
import com.ar.therapist.user.entity.AuthenticationProvider;
import com.ar.therapist.user.entity.Role;
import com.ar.therapist.user.entity.Token;
import com.ar.therapist.user.entity.TokenType;
import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.exception.ErrorResponse;
import com.ar.therapist.user.exception.UserException;
import com.ar.therapist.user.repo.TokenRepository;
import com.ar.therapist.user.repo.UserRepository;
import com.ar.therapist.user.utils.CookieUtils;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserAuthenticateService {

	@Autowired private UserRepository userRepository;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private MailService mailService;
	@Autowired private OTPService otpService;
	@Autowired private TokenRepository tokenRepo;
	
	// register
	public AuthenticationResponse register(UserRegisterRequest request) {
			var user = User.builder()
					.fullname(request.getFullname())
					.mobile(request.getMobile())
					.email(request.getEmail())
					.username(request.getUsername())
					.password(passwordEncoder.encode(request.getPassword()))
					.role(Role.USER) //Role.USER
					.provider(AuthenticationProvider.local)
					.nonLocked(true) // at register not lock true
					.enabled(false) // no/t enable at start
					.enabled(true) // FOR DEMO
					.build();
			
			userRepository.findByUsername(request.getUsername()).ifPresent(
					u-> {
						throw new UserRegisterException(new ErrorResponse("username already exists.", "username", request.getUsername()));
					});
			userRepository.findByEmail(request.getEmail()).ifPresent(
					u-> {
						throw new UserRegisterException(new ErrorResponse("email already exists.", "email", request.getEmail()));
					});
			
			User userSaved = userRepository.save(user);
			
			try {
				sendMailForVerify(userSaved);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
				throw new UserException("Error in new Registration ...");
			}
			
			return AuthenticationResponse.builder()
					.accessToken("ACCESS_TOKEN") 
					.refreshToken("REFRESH_TOKEN") 
					.build();
	}

	// login
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, HttpServletResponse response) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						 authRequest.getUsername(),
						 authRequest.getPassword()
						 )
				);
		
		var user = userRepository.findByUsername(authRequest.getUsername())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(new UsersDetails(user));
		var refreshToken = jwtService.generateRefreshToken(new UsersDetails(user));
		
		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);   
		
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		
		return AuthenticationResponse.builder()
				.id(user.getId())
				.accessToken(jwtToken)
				.username(user.getUsername())
				.email(user.getEmail())
				.fullname(user.getFullname())
				.role(user.getRole().name())
				.mobile(user.getMobile())
				.provider(user.getProvider().name()) 
				.imageUrl(user.getImageUrl())
				.build();
	}
	
	private void saveUserToken(User user, String jwtToken) {
		var token = Token.builder()
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepo.save(token);
	}
	
	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepo.findAllValidTokensByUser(user.getId());
		
		if(validUserTokens.isEmpty()) return;
		
		validUserTokens.forEach(t ->{
			t.setExpired(true);
			t.setRevoked(true);
		});
		
		tokenRepo.saveAll(validUserTokens);
		
		tokenRepo.deleteAll(validUserTokens);
	}
	
	// refresh tokens
	public void refreshToken(
			HttpServletRequest request, 
			HttpServletResponse response) 
		throws StreamWriteException, DatabindException, IOException {
		
		System.err.println("RRRRRRRRRRREEEEEEEEEEFFFREESHHHHHHHH");
		final String userName;
		final String refreshToken;  
		Optional<Cookie> cookie = CookieUtils.getCookie(request, "refresh_token");
		if(cookie.isEmpty()) {
			//return;
			throw new UserException("Your Session has been Expired Please Login");
		}
		refreshToken = cookie.get().getValue();
		try {
			userName = jwtService.extractUsername(refreshToken);
		
			if(userName != null) {
				var user = this.userRepository.findByUsername(userName)
						.orElseThrow();
				
				if(jwtService.isTokenValid(refreshToken, new UsersDetails(user))) {
					var accessToken = jwtService.generateToken(new UsersDetails(user));
					
					revokeAllUserTokens(user);  /// 
					saveUserToken(user, accessToken);  /// 
					
					var authResponse = AuthenticationResponse.builder()
							.accessToken(accessToken)
							//.refreshToken(refreshToken)
							.build();
					new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				}
			}
		}catch(ExpiredJwtException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			new ObjectMapper().writeValue(response.getOutputStream(), "Session has been Expired");
		}catch(Exception ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("An Error occured");
		}
		
	}

	// oauth2 token return
	public AuthenticationResponse oauth2TokenValidate(String token, HttpServletResponse response) {
		var userName = jwtService.extractUsername(token);
		var user = this.userRepository.findByUsername(userName)
				.orElseThrow();
		if(!jwtService.isTokenValid(token, new UsersDetails(user))) {
			throw new UserException("Invalid Oauth2 Token"); 
		}
		var jwtToken = jwtService.generateToken(new UsersDetails(user));
		var refreshToken = jwtService.generateRefreshToken(new UsersDetails(user));
		
		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		
		revokeAllUserTokens(user);  /// 
		saveUserToken(user, jwtToken);  ///
		
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.id(user.getId())
				.username(user.getUsername())
				.email(user.getEmail())
				.fullname(user.getFullname())
				.role(user.getRole().name())
				.mobile(user.getMobile())
				.provider(user.getProvider().name())
				.imageUrl(user.getImageUrl())
				.build();
	}

	// send mail function user
	public String sendMailForVerify(User user) throws UnsupportedEncodingException, MessagingException {
    	String otp = otpService.generateOTP(user);
    	System.out.println("OTP : "+otp);
    	return mailService.sendOTPMail(user, otp);
//    	return "OTP sended to ✉️ : "+user.getEmail();
	}
	
	// send mail function email -> send mail abow abow function
	public String sendMailForVerify(String userEmail) throws UnsupportedEncodingException, MessagingException {
    	User user = userRepository.findByEmail(userEmail)
        		.orElseThrow(() -> new UserException("Not Valid Email Id"));
    	return sendMailForVerify(user);	
	}
	
	// verify otp  -- this at signup time
	public AuthenticationResponse verifyOtp(String email, String otp, HttpServletResponse response) {
		var user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserException("Not Valid Username"));
		
		if(otpService.verifyOTP(user, otp)) {
			userRepository.updateEnabledById(user.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new UserException("Invalid otp : OTP is incorrect ❌");
		}
		
		System.out.println("OTP verified successfully ✅.");
		var jwtToken = jwtService.generateToken(new UsersDetails(user));
		var refreshToken = jwtService.generateRefreshToken(new UsersDetails(user));

		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.id(user.getId())
				.username(user.getUsername())
				.email(user.getEmail())
				.fullname(user.getFullname())
				.role(user.getRole().name())
				.mobile(user.getMobile())
				.provider(user.getProvider().name()) 
				.imageUrl(user.getImageUrl())
				.build();
	}
	
	// verify forgot otp and : return token   -- this at forgot time
	public String verifyForgotOtp(String email, String otp) {
		var user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserException("Not Valid Username"));
		
		if(otpService.verifyOTP(user, otp)) {
			userRepository.updateEnabledById(user.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new UserException("Invalid otp : OTP is incorrect ❌");
		}
		
		var jwtToken = jwtService.generateToken(new UsersDetails(user));
		
		return jwtToken; //"OTP verified successfully ✅."
	}
	
	// refresh otp
	public String refreshOtp(String email) {
		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserException("Not a Valid User")); 
		
		try {
			sendMailForVerify(user);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new UserException("Error in Otp Resend");
		}
		
		return "OTP Resended to ✉️ : "+user.getEmail();
	}
	
	// update forgot password
	public String updateForgotPassword(String username, String newPassword, String token) {
		User user = userRepository.findByUsername(username)
        		.orElseThrow(() -> new UserException("Not Valid Username ❌"));  
		
		if(!jwtService.isTokenValid(token, new UsersDetails(user))) {
			throw new UserException("Not Valid Token ❌");
		}
		
		userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
		return "Password updated successfully ✅"; 
	}
	
}

