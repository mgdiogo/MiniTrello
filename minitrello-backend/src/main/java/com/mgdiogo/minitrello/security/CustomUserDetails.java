package com.mgdiogo.minitrello.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mgdiogo.minitrello.enums.UserRole;
import com.mgdiogo.minitrello.utility.RoleMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private Long userId;
	private String email;
	private String password;
	private UserRole role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return RoleMapper.toAuthorities(this.role);
	}

	@Override
	public String getUsername() {
		return this.email;
	}
}
