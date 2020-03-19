package com.uac.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.uac.service.AuthService;
@Component
public class LftUserServiceImpl implements UserDetailsService{
	
	@Autowired
	private AuthService authService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//AuthUser authUser = authService.findUserByUsername(username);
		return null;
	}

}
