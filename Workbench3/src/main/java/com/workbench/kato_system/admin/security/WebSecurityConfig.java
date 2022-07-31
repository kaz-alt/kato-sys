package com.workbench.kato_system.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import com.workbench.kato_system.admin.login.service.LoginUserDetailsService;


@EnableWebSecurity
public class WebSecurityConfig {

  private final String LOGIN = "/loginForm";

	@Autowired
	LoginUserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

  @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new SessionExpiredDetectingLoginUrlAuthenticationEntryPoint(LOGIN);
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(
                  HttpServletRequest request,
                  HttpServletResponse response,
                  AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {
                if (accessDeniedException instanceof MissingCsrfTokenException) {
                    authenticationEntryPoint().commence(request, response, null);
                } else {
                    new AccessDeniedHandlerImpl().handle(request, response, accessDeniedException);
                }
            }
        };
    }

	@Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .loginProcessingUrl("/login")
        .loginPage(LOGIN)
        .permitAll()
        .defaultSuccessUrl("/home", true)
        .usernameParameter("username")
        .passwordParameter("password")
        .failureUrl("/loginForm?error=true")
        .permitAll()
    ).logout(logout -> logout
        .logoutSuccessUrl(LOGIN)
        .permitAll()
    ).authorizeHttpRequests(authz -> authz
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .mvcMatchers("/js/**", "/css/**", "/dist/**", "/favicon.ico").permitAll()
        .mvcMatchers("/loginForm/register", "/login?**").permitAll()
        .mvcMatchers("/admin").hasRole("ADMIN")
        .anyRequest().authenticated()
    ).exceptionHandling(except -> except
        // 通常のRequestとAjaxを両方対応するSessionTimeout用
        .authenticationEntryPoint(authenticationEntryPoint())
        // csrfはsessionがないと動かない。SessionTimeout時にPOSTすると403 Forbiddenを必ず返してしまうため、
        // MissingCsrfTokenExceptionの時はリダイレクトを、それ以外の時は通常の扱いとする。
        .accessDeniedHandler(accessDeniedHandler())
    );
    return http.build();
  }

}
