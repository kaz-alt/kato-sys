package com.workbench.kato_system.admin.notification.service;

import java.time.LocalDateTime;
import java.util.Optional;

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

		Optional<Notification> notification = notificationRepository.findById(employeeId);

		Notification n = new Notification();
		LocalDateTime now = LocalDateTime.now();

		if (notification.isPresent()) {

			n = notification.get();
			n.setCheckTime(now);

		} else {

			n.setEmployeeId(employeeId);
			n.setCheckTime(now);

		}

		notificationRepository.save(n);
	}

}
