package com.ead.course.services.impl;

import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {

        return repository.findAll(spec, pageable);
    }

    @Override
    public UserModel saveUserEvent(UserModel userModel) {
        return this.repository.save(userModel);
    }

    @Override
    public void deleteUserEvent(UUID userId) {
        this.repository.deleteById(userId);
    }
}
