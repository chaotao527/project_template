package com.singhand.sd.template.bizmodel.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

class InValidatorTest {

    private final Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure().buildValidatorFactory().getValidator();

    @Test
    void broken_if_unknown_value() {

    /*Assertions.assertFalse(
        validator.validateValue(Research.class, "catalog", "作战体系").isEmpty());*/
    }

    @Test
    void pass_if_known_value() {

   /* Assertions.assertTrue(
        validator.validateValue(Research.class, "catalog", "防空防导").isEmpty());*/
    }
}