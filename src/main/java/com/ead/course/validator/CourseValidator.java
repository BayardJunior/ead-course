package com.ead.course.validator;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Qualifier("defaultValidator")
    @Autowired
    Validator validator;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDto courseDto = (CourseDto) target;
        validator.validate(courseDto, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        Optional<UserModel> userModel = userService.findById(userInstructor);

        if (!userModel.isPresent()) {
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor ".concat(userInstructor.toString()).concat(" Not Found!"));
        }
        if (userModel.get().getUserType().equals(UserType.STUDENT)) {
            errors.rejectValue("userInstructor", "UserInstructorError", "User Must Be a Instructor or Admin");
        }
    }
}
