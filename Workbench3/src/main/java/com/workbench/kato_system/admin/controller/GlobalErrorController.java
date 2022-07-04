package com.workbench.kato_system.admin.controller;

import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class GlobalErrorController implements ErrorController {

	@RequestMapping
	public String index(Model model, HttpServletRequest request){

    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (Objects.nonNull(status) && String.valueOf(status).equals("404")) {

      return "error/404";

    }

    if (Objects.nonNull(status) && String.valueOf(status).equals("403")) {

      return "error/403";

    }

		return "error/500";
	}

}
