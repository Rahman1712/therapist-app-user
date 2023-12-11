package com.ar.therapist.user.vendor.service;

import java.time.LocalDateTime;
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
import com.ar.therapist.user.entity.booking.BookingDTO;
import com.ar.therapist.user.entity.booking.BookingRequest;
import com.ar.therapist.user.entity.booking.BookingRescheduleRequest;
import com.ar.therapist.user.entity.booking.RazorPayment;
import com.ar.therapist.user.entity.booking.TherapistInfo;
import com.ar.therapist.user.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TherapistBookingService {

//	private final WebClient webClient;
	private final CommonJwtService commonJwtService;
	private final BookingService bookingService; 
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}
	
	@Value("${therapist.service.api.url.booking}")
	private String THERAPIST_SERVICE_URL_BOOK;
	
	@Value("${therapist.service.api.url.reschedule}")
	private String THERAPIST_SERVICE_URL_RESCHEDULE;
	
	@Value("${therapist.service.api.url.update_payment}")
	private String THERAPIST_SERVICE_URL_UPDATE_PAYMENT;
	
	@Value("${therapist.service.api.url.booking-byid}")
	private String THERAPIST_SERVICE_URL_BOOKING_BYID;
	
	@Value("${therapist.service.api.url.bookings-byuser}")
	private String THERAPIST_SERVICE_URL_BOOKING_BYUSERID;
	
	@Value("${therapist.service.api.url.cancel_booking}")
	private String THERAPIST_SERVICE_URL_BOOKING_CANCEL;
	
	@Value("${therapist.service.api.url.therapists_byuserid}")
	private String THERAPIST_SERVICE_URL_THERAPISTS_BYUSERID;

	
	public BookingDTO bookAppointment(BookingRequest bookingRequest) {
		  bookingRequest.setAppointmentDateTime(LocalDateTime.now());
		  BookingDTO bookingDTO = 
				  loadBalancedWebClientBuilder().filter(lbFunction).build()  
				.post()
				.uri(THERAPIST_SERVICE_URL_BOOK)
				.body(BodyInserters.fromValue(bookingRequest))
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(BookingDTO.class)
				.block();
		  
		  System.err.println(bookingDTO);
		  bookingService.bookAppointment(bookingDTO); // to save in user side 
		  
		  return bookingDTO;
	}
	
	public BookingDTO bookReschedule(String bookingId, BookingRescheduleRequest rescheduleRequest) {
		  rescheduleRequest.setRescheduleDateTime(LocalDateTime.now());
		  BookingDTO bookingDTO = 
				  loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(THERAPIST_SERVICE_URL_RESCHEDULE+bookingId)
				.body(BodyInserters.fromValue(rescheduleRequest))
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(BookingDTO.class)
				.block();
		  
		  System.err.println(bookingDTO);
		  //bookingService.bookReschedule(bookingDTO); // to save in user side 
		  
		  return bookingDTO;
	}
	
	public BookingDTO updateBookingPayment(String id, RazorPayment razorPayment) {

		BookingDTO bookingDTO = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(THERAPIST_SERVICE_URL_UPDATE_PAYMENT+id+"/updatePayment")
				.body(BodyInserters.fromValue(razorPayment))
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(BookingDTO.class)
				.block();
		  
		  bookingService.updateBookingPayment(id, bookingDTO); // to save in user side 
		  
		  return bookingDTO;
	}
	
	public BookingDTO findByIdBooking(String bookingId) {

		BookingDTO bookingDTO = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(THERAPIST_SERVICE_URL_BOOKING_BYID+bookingId)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(BookingDTO.class)
				.block();
		  
		  return bookingDTO;
	}
	
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}

	public List<BookingDTO> findAllByUserId(Long userId) {
		List<BookingDTO> bookingDTOList = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(THERAPIST_SERVICE_URL_BOOKING_BYUSERID+userId)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToFlux(BookingDTO.class)
				.collectList()
				.block()
				;
		  
		  return bookingDTOList;
	}

	public BookingDTO cancelBooking(String bookingId) {
		BookingDTO bookingDTO = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(THERAPIST_SERVICE_URL_BOOKING_CANCEL+bookingId)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToMono(BookingDTO.class)
				.block();
		  
//		  bookingService.updateBookingPayment(id, bookingDTO); // to save in user side 
		  
		  return bookingDTO;
	}

	public List<TherapistInfo> getTherapistInfosByUserIdAndPaymentStatus(Long userId) {
		List<TherapistInfo> therapistInfos = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(THERAPIST_SERVICE_URL_THERAPISTS_BYUSERID+userId)
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
