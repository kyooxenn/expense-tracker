package com.proj.expensetracker.controller;


import com.proj.expensetracker.entity.UserInfo;
import com.proj.expensetracker.request.AuthRequest;
import com.proj.expensetracker.service.JwtService;
import com.proj.expensetracker.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") 
public class UserController { 

	@Autowired
	private UserInfoService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;



	@GetMapping("/welcome")
	public String welcome() {
		return "Hi, this api is not secured";
	}


	@GetMapping("/current-user")
	public Object getCurrentUser(Authentication authentication) {

		if (!ObjectUtils.isEmpty(authentication)) {
			return authentication.getPrincipal();
		}

		return "Either token expired or invalid token";

	}

	@PostMapping("/register")
	public HttpStatus addNewUser(@RequestBody UserInfo userInfo) {
		service.addUser(userInfo);
		return HttpStatus.CREATED;
	} 

	@GetMapping("/user/userProfile") 
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfile() { 
		return "Welcome to User Profile"; 
	} 

	@GetMapping("/admin/adminProfile") 
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public String adminProfile(HttpServletRequest request) {
		return "Welcome to Admin Profile"; 
	} 

	@PostMapping("/login")
	public Object login(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername()); 
		} else {
			return HttpStatus.UNAUTHORIZED;
		} 
	}


} 
