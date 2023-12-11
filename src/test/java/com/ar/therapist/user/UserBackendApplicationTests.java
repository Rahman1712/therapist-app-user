package com.ar.therapist.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import com.ar.therapist.user.config.JwtService;

@SpringBootTest
class UserBackendApplicationTests {
	
	// Create an instance of JwtService
    private JwtService jwtService = new JwtService();

    @Test
    public void testIsTokenValid() {
        // Define your secret key and token expiration
        String secretKey = "your-secret-key";
        long expiration = 3600000; // 1 hour in milliseconds

        // Create a sample UserDetails object
//        UserDetails userDetails = new User("username", "password", new ArrayList<>());
//
//        // Generate a sample token
//        String token = jwtService.buildToken(new HashMap<>(), userDetails, expiration);
//
//        // Test if the token is valid
//        boolean isValid = jwtService.isTokenValid(token, userDetails);
//
//        assertTrue(isValid);
    }

	
}
