package com.uac.service;

import com.uac.domain.AuthUser;

public interface AuthService {
	
	public AuthUser findUserByUsername(String username);
}
