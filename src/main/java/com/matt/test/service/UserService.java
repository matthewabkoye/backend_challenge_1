package com.matt.test.service;

import com.matt.test.dto.CreateUserRequest;
import com.matt.test.dto.CreateUserResponse;
import com.matt.test.dto.UpdateUserRequest;
import com.matt.test.dto.UpdateUserResponse;
import com.matt.test.model.User;

import java.util.List;

public interface UserService {
    CreateUserResponse create(CreateUserRequest request);
    List<User> fetchAllUsers(String username, Integer page, Integer pageSize);
    User updateUser(UpdateUserRequest request, String username);
    User deleteUser(String username);

}
