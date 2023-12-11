package com.ar.therapist.user.vendor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.entity.booking.BookingDTO;
import com.ar.therapist.user.entity.booking.BookingRequest;
import com.ar.therapist.user.entity.booking.BookingRescheduleRequest;
import com.ar.therapist.user.entity.booking.RazorPayment;
import com.ar.therapist.user.entity.booking.TherapistInfo;
import com.ar.therapist.user.vendor.service.TherapistBookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/therapist_booking")
@RequiredArgsConstructor
public class TherapistBookingController {

	private final TherapistBookingService therapistBookingService;

    @PostMapping("/book")  // booking by therapistid( in bookingRequest) 
    public ResponseEntity<BookingDTO> bookAppointment(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(therapistBookingService.bookAppointment(bookingRequest));
    }
    
    @PutMapping("/book-reschedule/byid/{bookingId}")   
    public ResponseEntity<BookingDTO> bookReschedule(
    		@PathVariable("bookingId") String bookingId,
    		@RequestBody BookingRescheduleRequest rescheduleRequest) {
    	return ResponseEntity.ok(therapistBookingService.bookReschedule(bookingId, rescheduleRequest));
    }
    
    @PutMapping("/{id}/updatePayment")
    public ResponseEntity<BookingDTO> updateBookingPayment(
            @PathVariable String id,
            @RequestBody RazorPayment razorPayment
    ) {
        return ResponseEntity.ok(therapistBookingService.updateBookingPayment(id, razorPayment));
    }
    
    @GetMapping("/booking-byid/{bookingId}")
    public ResponseEntity<BookingDTO> findByidBook(@PathVariable String bookingId) {
        return ResponseEntity.ok(therapistBookingService.findByIdBooking(bookingId));
    }
    
    @GetMapping("booking-byuserid/{userId}")
    public ResponseEntity<List<BookingDTO>> findBookingsByUserId(@PathVariable Long userId) {
    	return ResponseEntity.ok(therapistBookingService.findAllByUserId(userId));
    }
    
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(therapistBookingService.cancelBooking(bookingId));
    }
    
    @GetMapping("/book-therapists/by-user/{userId}")
    public ResponseEntity<List<TherapistInfo>> getTherapistInfosByUserIdAndPaymentStatus(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(therapistBookingService.getTherapistInfosByUserIdAndPaymentStatus(userId));
    }
}
