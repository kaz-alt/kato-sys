package com.workbench.kato_system.admin.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.model.ErrorLog;
import com.workbench.kato_system.admin.repository.ErrorLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController implements ErrorController {

  private final ErrorLogRepository errorLogRepository;
  private final ErrorAttributes error;

	@RequestMapping
	public String index(Model model, HttpServletRequest request, @AuthenticationPrincipal LoginUserDetails user){

    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    String errorPath = Objects.toString(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI), "UNKNOWN");

    if (Objects.nonNull(user)) {

      ErrorLog errorLog = new ErrorLog(errorPath, user.getUsername(), LocalDateTime.now(ZoneId.of("Asia/Tokyo")));

      errorLogRepository.save(errorLog);

    }

    if (Objects.nonNull(status) && Integer.valueOf(String.valueOf(status)).equals(HttpStatus.NOT_FOUND.value())) {

      return "error/404";

    }

    if (Objects.nonNull(status) && Integer.valueOf(String.valueOf(status)).equals(HttpStatus.FORBIDDEN.value())) {

      return "error/403";

    }

    Throwable e = error.getError(new ServletWebRequest(request));

    log.error("An expected error has occurred [path : {}]", String.valueOf(status), errorPath, e);

		return "error/500";
	}

}
