package com.ar.therapist.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	private Long id;
	private MessageRole role;
	private String content;
	private LocalDateTime timestamp;
}
