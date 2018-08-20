package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Authority extends BaseEntity{

	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String authority;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
