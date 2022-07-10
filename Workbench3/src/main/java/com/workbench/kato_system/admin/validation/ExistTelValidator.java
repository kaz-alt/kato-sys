package com.workbench.kato_system.admin.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistTelValidator implements ConstraintValidator<ExistTelValidation, Object> {

  private final EmployeeRepository employeeRepository;

  @Override
    public void initialize(ExistTelValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

      Employee e = employeeRepository.findByTel(String.valueOf(value));

      if (Objects.nonNull(e)) {

        return false;

      }

      return true;
    }

}
