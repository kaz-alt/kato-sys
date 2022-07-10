package com.workbench.kato_system.admin.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;
import com.workbench.kato_system.admin.user.model.User;
import com.workbench.kato_system.admin.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistEmailValidator implements ConstraintValidator<ExistEmailValidation, Object> {

  private final EmployeeRepository employeeRepository;
  private final UserRepository userRepository;

  @Override
    public void initialize(ExistEmailValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

      Employee e = employeeRepository.findByEmail(String.valueOf(value));
      User u = userRepository.findByEmail(String.valueOf(value));

      if (Objects.nonNull(e) || Objects.nonNull(u)) {

        return false;

      }

      return true;
    }

}
