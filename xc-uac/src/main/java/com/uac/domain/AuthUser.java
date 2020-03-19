package com.uac.domain;

import java.util.Set;

import lombok.Data;
@Data
public class AuthUser{
	private String id;
	private String password;
	private  String username;
	private  Set<AuthPermission> authorities;
	private Set<AuthRole> roles;
	
	private String deptId;
	
	private String deptName;
}
