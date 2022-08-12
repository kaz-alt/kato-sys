package com.workbench.kato_system.admin.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Constraint(validatedBy = ExistEmailAndTelValidator.class)
public @interface ExistEmailAndTelValidation {

  String[] fields();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload()  default {};

  String message() default "※このメールアドレスは既に登録されております";

}
