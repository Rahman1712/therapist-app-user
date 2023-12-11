package com.ar.therapist.user.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ar.therapist.user.entity.User;
import com.ar.therapist.user.exception.UserException;
import com.ar.therapist.user.repo.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider   {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;  
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.err.println("CCCCCCCCCCCCCCC");
		String username = authentication.getName();
	    String password = authentication.getCredentials().toString();
	    User user = repo.findByUsername(username).orElseThrow(()->
				new UserException("username not found"));
	    if (!encoder.matches(password, user.getPassword())) {
	    	throw new UserException("Invalid Password");
	    }
	    if(!user.isEnabled()) throw new UserException("account is not enabled");
	    if(!user.isNonLocked()) throw new UserException("account is locked");
	    	
	    List<String> userRoles = List.of(user.getRole().name());
	    
	    return new UsernamePasswordAuthenticationToken(username, password, userRoles.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
