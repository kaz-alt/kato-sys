package com.workbench.kato_system.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/loginForm")
@RequiredArgsConstructor
public class LoginController {

	@GetMapping("")
	public String index(Model model, @RequestParam(value = "error", required = false) boolean error){

		if(error) {
		  model.addAttribute("hasError",true);
		}

		return "loginForm";
	}

}
