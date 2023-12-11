package com.ar.therapist.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String token;
	
	@Enumerated(EnumType.STRING)
	private TokenType tokenType;
	
	private boolean expired;
	
	private boolean revoked;
	
	@Column(name = "logged_at", 
			nullable = false, updatable = false, insertable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime logged_at; 
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
} 
