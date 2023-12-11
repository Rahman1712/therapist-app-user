package com.ar.therapist.user.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ar.therapist.user.config.oauth.CustomOAuth2UserService;
import com.ar.therapist.user.config.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.ar.therapist.user.config.oauth.OAuth2AuthenticationFailureHandler;
import com.ar.therapist.user.config.oauth.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	private final CustomOAuth2UserService oAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	
	@Value("${cors.set.allowed.origins}")
	private String[] CROSS_ORIGIN_URLS;
	
	/*
	 * By default, Spring OAuth2 uses
	 * HttpSessionOAuth2AuthorizationRequestRepository to save the authorization
	 * request. But, since our service is stateless, we can't save it in the
	 * session. We'll save the request in a Base64 encoded cookie instead.
	 */
	  @Bean
	  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
	      return new HttpCookieOAuth2AuthorizationRequestRepository();
	  }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .cors(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable())
		.formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .exceptionHandling((exceptionHandling) ->
			exceptionHandling
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				//.accessDeniedPage("/errors/access-denied")
		)
		.authorizeHttpRequests(ahr ->
			ahr.requestMatchers(
					"/api/v1/auth/**", 
					"/api/v1/user-to-admin/**",
					"/api/v1/user-to-therapist/**",
					"/api/v1/therapist_booking/**",
					"/api/v1/video/**" 
			).permitAll()
			.anyRequest().authenticated()
		)
		.sessionManagement(sm -> 
		     sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
        .oauth2Login(oauthlogin -> oauthlogin
//        	.loginPage("/login")
        	.authorizationEndpoint(endpoint -> endpoint 
        	  .baseUri("/oauth2/authorize")
              .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
            )
            .redirectionEndpoint(redirectEndpoint -> redirectEndpoint
            		.baseUri("/oauth2/callback/*")
            )
            .userInfoEndpoint(userInfo -> userInfo
            		.userService(oAuth2UserService)
            )
            .successHandler(oAuth2LoginSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
        )
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.logout((logout) -> logout
			.logoutUrl("/api/v1/auth/logout")
			.addLogoutHandler(logoutHandler)
			.logoutSuccessHandler((request, response, authentication) -> 
				SecurityContextHolder.clearContext()
			)
 		)
		;

    return http.build();
	}
    
//	@Bean
//	public CorsFilter corsFilter() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.setAllowCredentials(true);
//		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
//		corsConfiguration.setAllowedHeaders(Arrays.asList(
//					"Origin","Access-Control-Allow-Origin", "Content-Type",
//					"Accept","Authorization","Origin, Accept","X-Requested-With",
//					"Access-Control-Request-Method","Access-Control-Request-Headers"
//				));
//		corsConfiguration.setExposedHeaders(Arrays.asList(
//					"Origin","Content-Type","Accept","Authorization",
//					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
//					"Access-Control-Allow-Credentials"
//				));
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//		return new CorsFilter(urlBasedCorsConfigurationSource);
//	}

}


/*
 * 
 * 
 * 
 @Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
		corsConfiguration.setAllowedHeaders(Arrays.asList(
					"Origin","Access-Control-Allow-Origin", "Content-Type",
					"Accept","Authorization","Origin, Accept","X-Requested-With",
					"Access-Control-Request-Method","Access-Control-Request-Headers"
				));
		corsConfiguration.setExposedHeaders(Arrays.asList(
					"Origin","Content-Type","Accept","Authorization",
					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
					"Access-Control-Allow-Credentials"
				));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("*");
				
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
	
	
	.cors(corsCustomizer -> {
            corsCustomizer.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList("Authorization"));
                configuration.setMaxAge(3600L);
                return configuration;
            });
        })
	
 */

/*
http
			.csrf(csrf -> csrf.disable())
			.cors(Customizer.withDefaults())
			.sessionManagement(sm -> 
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(requests -> 
				requests
				.requestMatchers(
						"/api/v1/auth/**",
						"/oauth2/**"
//						"/api/v1/video/**"
						)
				.permitAll()
				.anyRequest().authenticated()
			)
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			
			.oauth2Login()
//			.loginPage("/login")
			//.loginPage("/api/v1/demo")
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
			.redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
			.userInfoEndpoint().userService(oAuth2UserService)
			.and()
			.successHandler(oAuth2LoginSuccessHandler)
			.failureHandler(oAuth2AuthenticationFailureHandler)
			.and()
			
//			.oauth2Login(oauth2login -> {
//				oauth2login.authorizationEndpoint(authEndPoint ->
//						authEndPoint.baseUri("/oauth2/authorize")
//				);
//				
//			})
			
			.logout((logout) -> logout
				.logoutUrl("/api/v1/auth/logout")
				.addLogoutHandler(logoutHandler)
				.logoutSuccessHandler((request, response, authentication) -> 
					SecurityContextHolder.clearContext()
				)
	 		)

			;
			
		return http.build();
*/


//.oauth2Login()
//.loginPage("/login")
//  .authorizationEndpoint()
//  .baseUri("/oauth2/authorize")
//  .authorizationRequestRepository(cookieAuthorizationRequestRepository())
//  .and()
//  .redirectionEndpoint()
//  .baseUri("/oauth2/callback/*")
//  .and()
//  .userInfoEndpoint().userService(oAuth2UserService)
//  .and()
//  .successHandler(oAuth2LoginSuccessHandler)
//  .failureHandler(oAuth2AuthenticationFailureHandler)
//.and()