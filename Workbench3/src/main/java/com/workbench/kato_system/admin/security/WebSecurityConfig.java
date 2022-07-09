package com.workbench.kato_system.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.workbench.kato_system.admin.login.service.LoginUserDetailsService;


@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	LoginUserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .loginProcessingUrl("/login")
        .loginPage("/loginForm")
        .permitAll()
        .defaultSuccessUrl("/home", true)
        .usernameParameter("username")
        .passwordParameter("password")
        .failureUrl("/loginForm?error=true")
        .permitAll()
    ).logout(logout -> logout
        .logoutSuccessUrl("/loginForm")
        .permitAll()
    ).authorizeHttpRequests(authz -> authz
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .mvcMatchers("/js/**", "/css/**", "/dist/**", "/favicon.ico").permitAll()
        .mvcMatchers("/loginForm/register").permitAll()
        .mvcMatchers("/admin").hasRole("ADMIN")
        .anyRequest().authenticated()
    );
    return http.build();
  }

}
