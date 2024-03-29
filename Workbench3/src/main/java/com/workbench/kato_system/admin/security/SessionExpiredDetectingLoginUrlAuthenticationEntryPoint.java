package com.workbench.kato_system.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class SessionExpiredDetectingLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

  public SessionExpiredDetectingLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
        throws IOException, ServletException {

      if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
      }

      super.commence(request, response, authException);
  }

  @Override
  protected String buildRedirectUrlToLoginPage(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) {

      String redirectUrl = super.buildRedirectUrlToLoginPage(request, response, authException);
      if (isRequestedSessionInvalid(request)) {
          redirectUrl += redirectUrl.contains("?") ? "&" : "?";
          redirectUrl += "timeout";
      }
      return redirectUrl;
  }

  private boolean isRequestedSessionInvalid(HttpServletRequest request) {
      return request.getSession().getId() != null && !request.isRequestedSessionIdValid();
  }

}
