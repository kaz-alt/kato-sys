package com.workbench.kato_system.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class HomeController {

	@GetMapping(value = { "/", "/home" })
	String index() {
		return "home/index";
	}

}
