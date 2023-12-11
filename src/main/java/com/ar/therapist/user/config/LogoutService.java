package com.ar.therapist.user.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.ar.therapist.user.repo.TokenRepository;
import com.ar.therapist.user.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
	
	private final TokenRepository tokenRepository;

	@Override
	public void logout(
		HttpServletRequest request,
		HttpServletResponse response, 
		Authentication authentication
	) {
		System.err.println("LOGOGOGOOGOGOGOGOT");

		CookieUtils.deleteCookie(request, response, "refresh_token");
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		jwt = authHeader.split(" ")[1].trim(); //jwt = authHeader.substring(7);
		System.err.println(jwt);
		var storedToken = tokenRepository.findByToken(jwt)
				.orElse(null);
				//.orElseThrow(() ->  new UserException("Invalid User Logout Operation ❌"));
		
//		System.err.println(storedToken);
		if(storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			System.err.println("SSSSSSSAAAAAAAAAA LOGO");
			tokenRepository.save(storedToken);
		}
	}
	
	

}
