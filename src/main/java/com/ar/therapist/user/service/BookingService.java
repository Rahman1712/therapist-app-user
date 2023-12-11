package com.ar.therapist.user.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.entity.booking.Booking;
import com.ar.therapist.user.entity.booking.BookingDTO;
import com.ar.therapist.user.exception.UserException;
import com.ar.therapist.user.repo.BookingRepository;
import com.ar.therapist.user.repo.UserRepository;

@Service
public class BookingService {

	@Autowired private BookingRepository bookingRepository;
	@Autowired private UserRepository userRepository;
	
	public List<Booking> findByUserIdAndTherapistInfoId(Long userId, Long therapistId) {
        return bookingRepository.findByUserIdAndTherapistInfoTherapistId(userId, therapistId);
    }

    public void bookAppointment(BookingDTO dto) {
    	System.err.println(dto.getDate());
    	User user = userRepository.findById(dto.getUserData().getUserId()).orElse(null);
    	
        Booking booking = new Booking();
        booking.setBookingId(dto.getId());
        booking.setAppointmentId(dto.getId());
        booking.setAppointmentDateTime(dto.getAppointmentDateTime());
        booking.setTherapistInfo(dto.getTherapistInfo());
        booking.setCancellationDateTime(dto.getCancellationDateTime());
        booking.setNotes(dto.getNotes());
        booking.setMinutes(dto.getMinutes());
        booking.setAmount(dto.getAmount());
        booking.setTimeSlot(dto.getTimeSlot());
        booking.setDate(LocalDate.parse(dto.getDate()));
        booking.setUser(user);
        booking.setBookingStatus(dto.getBookingStatus());
        booking.setPaymentStatus(dto.getPaymentStatus());
        booking.setBookingType(dto.getBookingType());
        booking.setPayment(dto.getPayment());
        
        bookingRepository.save(booking);
    }
    
    @Transactional
	public void bookReschedule(BookingDTO bookingDTO) {
		
	}
    
    @Transactional
    public void updateBookingPayment(String appointmentId, BookingDTO dto) {
    	Booking booking = bookingRepository.findByAppointmentId(appointmentId)
    			.orElseThrow(() -> new UserException("Booking not found with appointmentId: " + appointmentId));

    	booking.setPayment(dto.getPayment());
    	booking.setBookingStatus(dto.getBookingStatus());
    	booking.setPaymentStatus(dto.getPaymentStatus());
    	booking.setTimeSlot(dto.getTimeSlot());
    	
    	bookingRepository.save(booking);
    }

    
	
}
