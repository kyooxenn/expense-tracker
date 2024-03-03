package com.proj.expensetracker.service;

import com.proj.expensetracker.entity.UserInfo;
import com.proj.expensetracker.mapper.UserInfoMapper;
import com.proj.expensetracker.utils.SnowflakeIdWorkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import com.proj.expensetracker.repository.UserInfoRepository;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService { 

	@Autowired
	private UserInfoRepository repository;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private PasswordEncoder encoder; 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

		Optional<UserInfo> userDetail = repository.findByUsername(username);

		// Converting userDetail to UserDetails 
		return userDetail.map(UserInfoDetails::new) 
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
	} 

	public void addUser(UserInfo userInfo) {

		if (ObjectUtils.isEmpty(userInfo.getUsername())) {
			throw new RuntimeException("Username must not be empty! Please Try again.");
		}

		if (ObjectUtils.isEmpty(userInfo.getPassword())) {
			throw new RuntimeException("Password must not be empty! Please Try again.");
		}

		if (ObjectUtils.isEmpty(userInfo.getEmail())) {
			throw new RuntimeException("Email must not be empty! Please Try again.");
		}

		boolean userExist = userInfoMapper.getUserInfo(userInfo);

		if (userExist) {
			throw new RuntimeException("User exist! Please Try again.");
		}

		SnowflakeIdWorkerUtils snow = new SnowflakeIdWorkerUtils(0,0);
		userInfo.setId(snow.nextId());
		userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
		repository.save(userInfo);
	} 


} 
