package com.workbench.kato_system.admin.login.model;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.workbench.kato_system.admin.user.model.User;

public class LoginUserDetails implements UserDetails{

	private User user;

	public LoginUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Integer getUserId() {
		return this.user.getUserId();
	}

	public String getEmail() {
		return this.user.getEmail();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + this.user.getRoleName().name());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		if (Objects.isNull(user)) {
			return null;
		}
		return this.user.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
