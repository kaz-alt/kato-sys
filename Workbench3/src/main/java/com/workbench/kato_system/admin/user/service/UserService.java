package com.workbench.kato_system.admin.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.user.form.UserForm;
import com.workbench.kato_system.admin.user.model.User;
import com.workbench.kato_system.admin.user.model.User.RoleName;
import com.workbench.kato_system.admin.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User save(UserForm form) {

		User user = form2User(form);

		return user;
	}

	public void delete(Integer id) {

		Optional<User> u = userRepository.findById(id);
		if (u.isPresent()) {
			User user = u.get();
			userRepository.delete(user);
		}
	}

	private User form2User(UserForm form) {

		User u = new User();

		if (form.getId() != null) {
			u = userRepository.findById(form.getId()).orElse(new User());
		}

		u.setName(form.getName());
		u.setEmail(form.getEmail());
		// パスワードをエンコードする
		u.setPassword(passwordEncoder.encode(form.getPassword()));
		// デフォルトでユーザー権限に設定
		u.setRoleName(RoleName.USER);
		u = userRepository.save(u);

		return u;
	}

}
