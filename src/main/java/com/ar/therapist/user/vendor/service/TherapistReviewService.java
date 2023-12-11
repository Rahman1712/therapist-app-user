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
import com.ar.therapist.user.dto.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TherapistReviewService {

//	private final WebClient webClient;
	private final CommonJwtService commonJwtService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}
	
	@Value("${review.service.api.url.create}")
	private String THERAPIST_SERVICE_REVIEW_CREATE;
	
	@Value("${review.service.api.url.update_review}")
	private String THERAPIST_SERVICE_REVIEW_UPDATE;
	
	@Value("${review.service.api.url.delete_review}")
	private String THERAPIST_SERVICE_REVIEW_DELETE;
	
	@Value("${review.service.api.url.by_user}")
	private String THERAPIST_SERVICE_REVIEW_BYUSER;
	
	@Value("${review.service.api.url.by_therapist}")
	private String THERAPIST_SERVICE_REVIEW_BYTHERAPIST;
	
	@Value("${review.service.api.url.by_user_therapist}")
	private String THERAPIST_SERVICE_REVIEW_BY_USER_AND_THERAPIST;
	
	@Value("${review.service.api.url.by_booking}")
	private String THERAPIST_SERVICE_REVIEW_BYBOOKING;

	public Review createReview(Review review) {
        Review createdReview = 
        		loadBalancedWebClientBuilder().filter(lbFunction).build()
        		.post()
                .uri(THERAPIST_SERVICE_REVIEW_CREATE)
                .body(BodyInserters.fromValue(review))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToMono(Review.class)
                .block();

        return createdReview;
    }

    public Review updateReview(Long id, Review updatedReview) {
        Review savedReview = 
        		loadBalancedWebClientBuilder().filter(lbFunction).build()
        		.put()
                .uri(THERAPIST_SERVICE_REVIEW_UPDATE + id)
                .body(BodyInserters.fromValue(updatedReview))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToMono(Review.class)
                .block();

        return savedReview;
    }
	
    public List<Review> getReviewsByUserId(Long userId) {
        List<Review> reviews = 
        		loadBalancedWebClientBuilder().filter(lbFunction).build()
        		.get()
                .uri(THERAPIST_SERVICE_REVIEW_BYUSER + userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        return reviews;
    }
    
	public Review getReviewByBookingId(String bookingId) {
		Review review = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
                .uri(THERAPIST_SERVICE_REVIEW_BYBOOKING + bookingId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToMono(Review.class)
                .block();
		
		return review;
	}

    public List<Review> getReviewsByUserIdAndTherapistId(Long userId, Long therapistId) {
        List<Review> reviews = 
        		loadBalancedWebClientBuilder().filter(lbFunction).build()
        		.get()
                .uri(THERAPIST_SERVICE_REVIEW_BY_USER_AND_THERAPIST+"/user/"+ userId +"/therapist/"+ therapistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        return reviews;
    }

    public List<Review> getReviewsByTherapistId(Long therapistId) {
        List<Review> reviews = 
        		loadBalancedWebClientBuilder().filter(lbFunction).build()
        		.get()
                .uri(THERAPIST_SERVICE_REVIEW_BYTHERAPIST + therapistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        return reviews;
    }

    public void deleteReview(Long id) {
    	        loadBalancedWebClientBuilder().filter(lbFunction).build()
    			.delete()
                .uri(THERAPIST_SERVICE_REVIEW_DELETE + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + commonJwtService.generateToken(getAuthenticationUsername()))
                .header("Username", getAuthenticationUsername())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.createError())
                .toBodilessEntity()
                .block();
    }
    
	
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}



	
}

/*
 * public String updateProductsQuantiy(List<OrderProduct> orderProducts, String type) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(PRODUCT_SERVICE_URL_UPDATE_PRODUCTS_QUANTIY)
				.queryParam("type", type); // ADD, DELETE
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		
		return  webClient.post()
				.uri(uriPathWithQueryParams)
				.body(BodyInserters.fromValue(orderProducts))
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtProductsService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(String.class)
				.block();
	}
 */
