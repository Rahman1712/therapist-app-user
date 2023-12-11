package com.ar.therapist.user.entity.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import com.ar.therapist.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	@Id
	@GeneratedValue( strategy = GenerationType.UUID)
	@GenericGenerator(name = "uuid", type = UuidGenerator.class)
	private String id;
	
	private String bookingId;
	
    @Embedded
    private TherapistInfo therapistInfo;
	
	@Column(name = "appointment_id")
	private String appointmentId;
	
	@Column(name = "appointment_date_time")
    private LocalDateTime appointmentDateTime;
	
	@Column(name = "reschedule_date_time")
	private LocalDateTime rescheduleDateTime;
	
    @Column(name = "cancellation_date_time")
    private LocalDateTime cancellationDateTime;
    
    private LocalDate date;
    
    @Embedded
    private TimeSlot timeSlot;
    
    private Long minutes;
    
    private Double amount;
    
    @Column(length = 2000) 
    private String notes;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status")
	private BookingStatus bookingStatus;
	 
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    @Embedded
    private Payment payment;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}


/*

	//private LocalDateTime bookingDateTime;
	 
 	@Id
//	@GeneratedValue( generator = "uuid2", strategy = GenerationType.UUID)
	@GeneratedValue( strategy = GenerationType.UUID)
//	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@GenericGenerator(name = "uuid", type = UuidGenerator.class)
	private String id;
 
 
@Temporal(TemporalType.DATE)
	private Date bookingDate;
	@Temporal(TemporalType.TIME)
	private Date bookingTime;
 */
