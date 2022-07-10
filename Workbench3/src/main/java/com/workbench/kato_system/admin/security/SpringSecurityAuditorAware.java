package com.workbench.kato_system.admin.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();

		return Optional.of(user.getEmail());
	}

}
