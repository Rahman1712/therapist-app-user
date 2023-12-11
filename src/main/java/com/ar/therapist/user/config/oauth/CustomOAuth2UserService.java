package com.ar.therapist.user.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.err.println("OOOOOOOOOOOOOOOOO");
		userRequest.getAccessToken().getScopes().forEach(System.out::println);
		OAuth2User user = super.loadUser(userRequest);
		return  new CustomOAuth2User(user, userRequest.getClientRegistration().getRegistrationId());
	} 
	
}

//System.out.println(userRequest.getClientRegistration());
//ClientRegistration{registrationId='google', clientId='25785987323-r15qiacq5unonl9bji8bocsse51b6hbh.apps.googleusercontent.com', clientSecret='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', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/oauth2/callback/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@3996ac2c, clientName='Google'}


// OAuth2User   object nte vila
//CustomOAuth2User(oauth2User=Name: [104799263791206332049], Granted Authorities: [[OAUTH2_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]], User Attributes: [{sub=104799263791206332049, name=raaka dc, given_name=raaka, family_name=dc, picture=https://lh3.googleusercontent.com/a/ACg8ocI_PHaIL2L4MpUlND2ruEZQ8FWKP6nShSwm-_ROwSZR=s96-c, email=donrahman6@gmail.com, email_verified=true, locale=en}])
