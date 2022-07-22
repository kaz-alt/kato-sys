package com.workbench.kato_system.admin.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workbench.kato_system.admin.login.model.LoginUserDetails;
import com.workbench.kato_system.admin.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/notification")
public class NotificationController {

	private final NotificationService notificationService;


	/**
	 * 確認時間更新
	 */
	@GetMapping("/update_check_time")
	@ResponseBody
	public ResponseEntity<String> updateCheckTime(@AuthenticationPrincipal LoginUserDetails user) {

		try {

			notificationService.updateCheckTime(user.getUserId());

		} catch (Exception e) {

			return new ResponseEntity<>("failed to update...", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}
