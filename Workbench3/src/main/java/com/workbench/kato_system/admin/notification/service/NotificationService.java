package com.workbench.kato_system.admin.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.notification.model.Notification;
import com.workbench.kato_system.admin.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public Notification getOne(Integer employeeId) {

		return notificationRepository.findById(employeeId).orElse(new Notification());

	}

	public void updateCheckTime(Integer employeeId) {

		notificationRepository.updateCheckTime(LocalDateTime.now(), employeeId);

	}

}
