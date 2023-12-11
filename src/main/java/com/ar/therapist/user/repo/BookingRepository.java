package com.ar.therapist.user.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.entity.booking.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>{

	List<Booking> findByUser(User user);
	
	List<Booking> findByUserIdAndTherapistInfoTherapistId(Long userId, Long therapistInfoId);

	List<Booking> findByTherapistInfoTherapistIdAndAppointmentDateTimeBetween(Long therapistId, LocalDateTime startDate, LocalDateTime endDate);

	List<Booking> findByTherapistInfoTherapistId(Long therapistId);

	Optional<Booking> findByAppointmentId(String appointmentId);
}

