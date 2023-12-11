package com.ar.therapist.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
	private Long id;
	private Long therapistId;
    private Long userId;
    private String roomId;
    private List<Message> messages;
}
