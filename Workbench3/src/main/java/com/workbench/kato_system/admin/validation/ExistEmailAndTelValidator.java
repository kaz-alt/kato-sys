package com.workbench.kato_system.admin.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.workbench.kato_system.admin.employee.model.Employee;
import com.workbench.kato_system.admin.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistEmailAndTelValidator implements ConstraintValidator<ExistEmailAndTelValidation, Object> {

  private final EmployeeRepository employeeRepository;

  private String[] fields;
  private String message;

  @Override
    public void initialize(ExistEmailAndTelValidation constraintAnnotation) {
      this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

      List<Boolean> validList = new ArrayList<>();

      BeanWrapper beanWrapper = new BeanWrapperImpl(value);

      Object id = beanWrapper.getPropertyValue(fields[0]);
      Object email = beanWrapper.getPropertyValue(fields[1]);
      Object tel = beanWrapper.getPropertyValue(fields[2]);

      Employee e1 = employeeRepository.findByEmail(String.valueOf(email));
      Employee e2 = employeeRepository.findByTel(String.valueOf(tel));

      message = "※このメールアドレスは既に登録されております";
      if (Objects.isNull(e1)) {
        validList.add(true);
      } else {
        validList.add(this.validator(e1, id, context, message, fields[1], e1.getEmail(), email));
      }

      message = "※この電話番号は既に登録されております";
      if (Objects.isNull(e2)) {
        validList.add(true);
      } else {
        validList.add(this.validator(e2, id, context, message, fields[2], e2.getTel(), tel));
      }

      return !validList.contains(false);
    }

    private boolean validator(Employee e, Object id, ConstraintValidatorContext context,
        String message, String field, String value, Object target) {

      if (e.getId() != null && e.getId().equals(id) && value.equals(target)) {
        return true;
      }

      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addPropertyNode(field)
        .addConstraintViolation();

      return false;
    }

}
