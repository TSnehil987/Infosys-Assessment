package com.infosys.javaassessment.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.javaassessment.dto.UserDTO;
import com.infosys.javaassessment.model.LoginRequestModel;
import com.infosys.javaassessment.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	private Environment env;
	
	private UserService userService;

	@Autowired
	public AuthenticationFilter(UserService userService, Environment env) {
		this.env = env;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
			return getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(
								creds.getEmail(), 
								creds.getPassword(), 
								new ArrayList<>()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = ((User) authResult.getPrincipal()).getUsername();
		if(userService != null) {
			UserDTO userDetails = userService.getUserByEmail(username);
			
			String token = Jwts.builder().setSubject(userDetails.getUserId())
			.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
			.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
			.compact();
			
			response.addHeader("token", token);
			response.addHeader("userId", userDetails.getUserId());
		} else {
			log.error("Null Pointer Exception");
		}
	}
}
