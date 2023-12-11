package com.ar.therapist.user.config.oauth;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.therapist.user.config.JwtService;
import com.ar.therapist.user.entity.AuthenticationProvider;
import com.ar.therapist.user.entity.Role;
import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.exception.BadRequestException;
import com.ar.therapist.user.exception.OAuth2AuthenticationProcessingException;
import com.ar.therapist.user.service.UserService;
import com.ar.therapist.user.service.UsersDetails;
import com.ar.therapist.user.utils.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	//@Autowired	private UserService userService;
	private final UserService userService;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	private final JwtService jwtService;
	
	@Value("${api.redirect.uris}")
	private String[] API_REDIRECT_URIS;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal(); 
		
		String email = oAuth2User.getEmail();
		if(!StringUtils.hasText(email)) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        } 
		
		User user = userService.findUserByEmail(email);
		
		String name = oAuth2User.getName();
		
		if(user == null) {
			//register as a new customer
			user = userService.createNewUserAfterOAuthLoginSuccess(
					email,name, Role.USER, AuthenticationProvider.valueOf(oAuth2User.getProvider()), oAuth2User.getImageUrl()); //like AuthenticationProvider.google
		}else {
			//update existing customer
			user = userService.updateUserAfterOAuthLoginSuccess(
					user,name,AuthenticationProvider.valueOf(oAuth2User.getProvider()), oAuth2User.getImageUrl());
		}
		
		System.out.println("Customer's Email:"+email);
		
//		super.onAuthenticationSuccess(request, response, authentication);
		
		
		String targetUrl = determineTargetUrl(request, response, user);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
	
	 protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, User user) {
	        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
	                .map(Cookie::getValue);

	        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
	            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
	        }

	        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

	        String token = jwtService.generateOauth2Token(new UsersDetails(user));

	        return UriComponentsBuilder.fromUriString(targetUrl)
	                .queryParam("token", token)
	                .build().toUriString();
	    }

	    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
	        super.clearAuthenticationAttributes(request);
	        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	    }

	    private boolean isAuthorizedRedirectUri(String uri) {
	        URI clientRedirectUri = URI.create(uri);

	        List<String> urisList = Arrays.asList(API_REDIRECT_URIS);
	        return  urisList.stream()
	                .anyMatch(authorizedRedirectUri -> {
	                    // Only validate host and port. Let the clients use different paths if they want to
	                    URI authorizedURI = URI.create(authorizedRedirectUri);
	                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
	                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
	                        return true;
	                    }
	                    return false;
	                });
	    }

}

/*
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		System.out.println(oAuth2User);
		String email = oAuth2User.getEmail();
		
		User user = userService.findUserByEmail(email);
		
		String name = oAuth2User.getName();
		
		if(user == null) {
			//register as a new customer
			userService.createNewUserAfterOAuthLoginSuccess(
					email,name,AuthenticationProvider.GOOGLE);
		}else {
			//update existing customer
			userService.updateUserAfterOAuthLoginSuccess(
					user,name,AuthenticationProvider.GOOGLE);
		}
		
		System.out.println("Customer's Email:"+email);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

*/