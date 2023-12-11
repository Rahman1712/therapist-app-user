package com.ar.therapist.user.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.dto.AuthenticationRequest;
import com.ar.therapist.user.dto.AuthenticationResponse;
import com.ar.therapist.user.dto.OauthRequest;
import com.ar.therapist.user.dto.UserRegisterRequest;
import com.ar.therapist.user.service.UserAuthenticateService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthenticationController {

	private final UserAuthenticateService userAuthenticateService;
	
	@GetMapping("/work")
	public String work() {
		return "Worked";
	}
	
	@GetMapping("/set")
	public String set(HttpServletResponse response) {
		Cookie c = new Cookie("apples", "kasmirappple_orange");
		c.setPath("/");
		c.setSecure(true);
		c.setHttpOnly(true);
		response.addCookie(c);
		
		return "SETTTT";
	}
	@GetMapping("/get")
	public String get(@CookieValue(value = "apples", required = false) String a ,HttpServletRequest request) {
		System.err.println(request.getCookies());
		if(a!=null) return "Cookie val :"+a;
		else return "Not Cookie got";
	}
	
	@GetMapping("/check")
	public String check(HttpServletRequest request) {
//		Optional<Cookie> cookie = CookieUtils.getCookie(request, "refresh_token");
//		System.out.println(cookie);
//		System.out.println(cookie.get().getValue());
//		if(cookie.isPresent()) return cookie.get().getValue();
		
		Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    return cookie.getValue();
                }
            }
        }
		return "Not Found"; 
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid UserRegisterRequest request) {
		return new ResponseEntity<>(userAuthenticateService.register(request), HttpStatus.CREATED); 
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
		return ResponseEntity.ok(userAuthenticateService.authenticate(authRequest, response));
	}
	
	@PostMapping("/refresh-token")
	public void refresh(
		HttpServletRequest request,
		HttpServletResponse response
	) throws StreamWriteException, DatabindException, IOException {
		System.err.println(request.getCookies());
		System.err.println("CCCCCCOOOKI REFRSH CNTRLR");
		for(Cookie c: request.getCookies()) {
			System.err.println(c.getName()+" ||| "+c.getValue());
		}
		userAuthenticateService.refreshToken(request, response);
	}
	
	@PostMapping("validate-token/oauth2-token")  // oauth validate and return token
	public ResponseEntity<AuthenticationResponse> getTokenByOauth2(@RequestBody OauthRequest token, HttpServletResponse response) {
		System.err.println(token); 
		return ResponseEntity.ok(userAuthenticateService.oauth2TokenValidate(token.getToken(), response));
	}
	
	@PostMapping("/verify-otp") // For OTP verify at signup
	public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp, HttpServletResponse response) {
		return ResponseEntity.ok(userAuthenticateService.verifyOtp(email, otp, response));
	}
	
	@PostMapping("/verify-forgot-otp") // For OTP verify at forgot time : return token
	public ResponseEntity<?> verifyForgotOtp(@RequestParam String email, @RequestParam String otp) {
		return ResponseEntity.ok(userAuthenticateService.verifyForgotOtp(email, otp));
	}
	
	@PostMapping("/mail-reset-otp") // For OTP at forgot time , (also that function use in register)
	public ResponseEntity<String> verifyOtp(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		return ResponseEntity.ok(userAuthenticateService.sendMailForVerify(email));
	}
	
	@PutMapping("/resend-otp") // For OTP at signup, forgot time
	public ResponseEntity<String> resendOtp(@RequestParam String email) {
		return ResponseEntity.ok(userAuthenticateService.refreshOtp(email)); 
	}
	
	@PutMapping("/update-password") // For Password update at forgot time 
	public ResponseEntity<String> resendOtp(@RequestParam String username, String newPassword, String token) {
		return ResponseEntity.ok(userAuthenticateService.updateForgotPassword(username, newPassword, token)); 
	}

	
}

/*
	@PostMapping("/cookie-send")
    public ResponseEntity<?> login(HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie("darb_code", "DARB1712"+new Random().nextInt(100));
        refreshTokenCookie.setHttpOnly(true); // Make the cookie accessible only through HTTP (not JavaScript)
        refreshTokenCookie.setPath("/"); // Set the cookie path
        refreshTokenCookie.setMaxAge(30 * 24 * 3600); // Set the cookie expiration time (e.g., 30 days)

        response.addCookie(refreshTokenCookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_code", "ACCESS_1712"+new Random().nextInt(10));

        return ResponseEntity.ok(responseBody);
    }
    
    @GetMapping("/cookie-get")
    public ResponseEntity<?> someEndpoint(HttpServletRequest request) {
        // Retrieve the cookies from the request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("darb_code")) {
                    String darb_code = cookie.getValue();
                    // You have the darb_code, you can now use it for your logic.
                    return ResponseEntity.ok("Darb Code: " + darb_code);
                }
            }
        }

        // If the darb_code  cookie is not found, handle it accordingly (e.g., redirect to login).
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("darb code token not found.");
    }

*/