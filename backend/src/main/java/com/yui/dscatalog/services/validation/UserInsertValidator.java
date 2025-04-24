package com.yui.dscatalog.services.validation;


import com.yui.dscatalog.dto.UserInsertDTO;
import com.yui.dscatalog.models.User;
import com.yui.dscatalog.repositories.UserRepository;
import com.yui.dscatalog.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage>  list = new ArrayList<>();
        User user = repository.findByEmail(dto.getEmail());
        if (user != null){
            list.add(new FieldMessage("email", "Email já existe"));
        }
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
