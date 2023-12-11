package com.ar.therapist.user.entity;

import java.util.List;

import com.ar.therapist.user.entity.booking.Booking;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "users_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fullname;
	private String mobile;
	private String username;
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_provider")
	private AuthenticationProvider provider;
	
	private String imageUrl;
	
	@Column(name = "non_locked")
	private boolean nonLocked;
	private boolean enabled;
	
	@OneToMany(mappedBy = "user")
	private List<Booking> bookings;
}

/*
	@Lob 
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "user_image",length=100000)
	private byte[] image;
	
	@Column(name = "user_image_name")
	private String imageName; 
	
	@Column(name = "user_image_type")
	private String imageType;
 * 
 */
