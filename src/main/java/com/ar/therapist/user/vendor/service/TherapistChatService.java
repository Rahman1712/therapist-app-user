package com.ar.therapist.user.vendor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ar.therapist.user.common.config.CommonJwtService;
import com.ar.therapist.user.dto.Chat;
import com.ar.therapist.user.dto.ChatRequest;
import com.ar.therapist.user.entity.booking.TherapistInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TherapistChatService {

//	private final WebClient webClient;
	private final CommonJwtService commonJwtService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}

	@Value("${chat.service.api.url.therapistsinfo_byuserid}")
	private String CHAT_SERVICE_THERAPISTS_INFO_BYUSERID;
	
	@Value("${chat.service.api.url.create}")
	private String CHAT_SERVICE_CREATE;
	
	@Value("${chat.service.api.url.chat_byroomid}")
	private String CHAT_SERVICE_CHAT_BYROOMID;
	
	public List<TherapistInfo> getTherapistsListByUserId(Long userId) {
		List<TherapistInfo> therapistInfos =
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(CHAT_SERVICE_THERAPISTS_INFO_BYUSERID+userId)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToFlux(TherapistInfo.class)
				.collectList()
				.block()
				;
		
		return therapistInfos;
	}

	public Chat createChat(ChatRequest chatRequest) {
		Chat chat = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.post()
				.uri(CHAT_SERVICE_CREATE)
				.body(BodyInserters.fromValue(chatRequest))
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(Chat.class)
				.block();
		
		return chat; 
	}

	public Chat findByRoomId(String roomId) {
		Chat chat = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(CHAT_SERVICE_CHAT_BYROOMID+roomId)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(Chat.class)
				.block();
		
		return chat;
	}
	
	
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}

}

