package com.workbench.kato_system.admin.user.service;

import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workbench.kato_system.admin.employee.form.ChangePasswordForm;
import com.workbench.kato_system.admin.employee.form.EmployeeForm;
import com.workbench.kato_system.admin.employee.service.EmployeeService;
import com.workbench.kato_system.admin.user.model.User;
import com.workbench.kato_system.admin.user.model.User.RoleName;
import com.workbench.kato_system.admin.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EmployeeService employeeService;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public User save(EmployeeForm form) {

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

	private User form2User(EmployeeForm form) {

		User u = new User();

		if (form.getId() != null) {
			u = userRepository.findById(form.getId()).orElse(new User());
		}

		u.setName(form.getLastName() + form.getFirstName());
		u.setEmail(form.getEmail());
		// パスワードをエンコードする
		u.setPassword(passwordEncoder.encode(form.getUserForm().getPassword()));
		// デフォルトでユーザー権限に設定
		u.setRoleName(RoleName.USER);
		u = userRepository.save(u);

		employeeService.save(form);

		return u;
	}

	public void changePassword(ChangePasswordForm form) {
		User u = userRepository.findByEmail(form.getEmail());

		if (Objects.nonNull(u)) {
			u.setPassword(passwordEncoder.encode(form.getPassword()));
			userRepository.save(u);
		}
  }

}
