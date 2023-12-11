package com.ar.therapist.user.vendor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.dto.Chat;
import com.ar.therapist.user.dto.ChatRequest;
import com.ar.therapist.user.entity.booking.TherapistInfo;
import com.ar.therapist.user.vendor.service.TherapistChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-messages")
@RequiredArgsConstructor
public class TherapistChatController {

	private final TherapistChatService therapistChatService;
 
	@PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestBody ChatRequest chatRequest) {
        Chat createdChat = therapistChatService.createChat(chatRequest);
        return new ResponseEntity<>(createdChat, HttpStatus.CREATED);
    }
	
	@GetMapping("/by-roomId/{roomId}")
	public ResponseEntity<Chat> getMessagesByRoomId(@PathVariable String roomId) {
		return ResponseEntity.ok(therapistChatService.findByRoomId(roomId));
	}
	
    @GetMapping("/therapists/{userId}")
    public ResponseEntity<List<TherapistInfo>> getTherapistsListByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(therapistChatService.getTherapistsListByUserId(userId));
    }
}
