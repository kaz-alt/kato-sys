package com.workbench.kato_system.admin.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.todo.form.ToDoForm;
import com.workbench.kato_system.admin.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/todo")
public class ToDoController {

	private final TodoService todService;

	/**
	 * ToDoの追加
	 */
	@PostMapping(value = "/create")
	@ResponseBody
	public ResponseEntity<String> create(
			@ModelAttribute @Validated ToDoForm form,
			BindingResult result,
			@AuthenticationPrincipal LoginUserDetails user) {

		if (result.hasErrors()) {
			return new ResponseEntity<>("fail to save", HttpStatus.BAD_REQUEST);
		}

		try {
			todService.save(form, user);
		} catch (Exception e) {
			return new ResponseEntity<>("an error has occurred while saving", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}
