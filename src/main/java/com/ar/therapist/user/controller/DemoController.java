package com.ar.therapist.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.user.dto.UserDto;
import com.ar.therapist.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
	
	@Autowired private UserService userService;

	@GetMapping
	public String worked(HttpServletRequest request) {
		System.err.println(request.getCookies());
		System.err.println("DEMOOOOOOOO");
		return "Hey its a private data 📁 🔐"; 
	}
	
	@GetMapping("/users")
    public ResponseEntity<List<UserDto>> allUsers(){
    	return ResponseEntity.ok(userService.findAll());
    }
}
