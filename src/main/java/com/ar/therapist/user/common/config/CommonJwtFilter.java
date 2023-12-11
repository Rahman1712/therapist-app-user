package com.ar.therapist.user.common.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommonJwtFilter implements Filter{

	private final CommonJwtService commonJwtService;
	
	@Override
	public void doFilter(ServletRequest request, 
			ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;
		final String userName;
		System.err.println("FFFFFFFFFFmmmm");
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request to Products Service");
			return;
		}
		System.err.println("FFFFFFFFFFmmmmPPPPPPPPPPPPP");
		jwt = authHeader.split(" ")[1].trim();
		try {
			userName = commonJwtService.extractUsername(jwt);
			
			System.err.println("FFFFFFFFFFmmmmPPPPPPP UUUUUU :: "+ userName);
			if(userName == null || 
					!commonJwtService.isTokenValid(jwt, 
							httpRequest.getHeader("Username"))) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request to Users Service");
				return;
			}
		}catch(ExpiredJwtException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			errorResponse.setMessage("Token has Expired : user section");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonError = objectMapper.writeValueAsString(errorResponse);
			
			httpResponse.getWriter().write(jsonError);
			return;
		}catch(Exception ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("An Error occured");
			return;
		}
		
		chain.doFilter(request, response);
	}

	
}
