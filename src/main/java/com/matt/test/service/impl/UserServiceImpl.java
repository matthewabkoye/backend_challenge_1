package com.matt.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.test.dto.CreateUserRequest;
import com.matt.test.dto.CreateUserResponse;
import com.matt.test.dto.UpdateUserRequest;
import com.matt.test.exceptions.NoPermissionException;
import com.matt.test.model.User;
import com.matt.test.repo.UserRepository;
import com.matt.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository,
                           ObjectMapper mapper,
                           PasswordEncoder encoder){
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }
    @Override
    public CreateUserResponse create(CreateUserRequest request) {
        if(!request.getRole().equalsIgnoreCase("seller") && !request.getRole().equalsIgnoreCase("buyer")){
            throw new NoPermissionException("Invalid role");
        }
        Optional<User>u = userRepository.findByUsername(request.getUsername());
        if(u.isPresent()){
            throw new NoPermissionException("User with the username exist");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setDeposit(request.getDeposit());
        user.setPassword(encoder.encode(request.getPassword()));
        user = userRepository.save(user);
        CreateUserResponse response = new CreateUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setDeposit(user.getDeposit());
        return response;
    }

    @Override
    public List<User> fetchAllUsers(String username, Integer page, Integer pageSize) {
        if(username != null && !username.isEmpty()){
            Optional<User>optUser = userRepository.findByUsername(username);
            User user = optUser.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            List<User>users = Arrays.asList(user);
            return users;
        }else{
            Pageable pageRequest = PageRequest.of(page-1,pageSize);
            Page<User> users = userRepository.findAll(pageRequest);
            return users.getContent();
        }

    }

    @Override
    public User updateUser(UpdateUserRequest request, String username) {
        Optional<User>optUser = userRepository.findByUsername(username);
        User user = optUser.orElseThrow(() -> new UsernameNotFoundException("username not found"));
        if(request.getPassword() != null && !request.getPassword().isEmpty()){
            user.setPassword(encoder.encode(request.getPassword()));
        }
        if(request.getDeposit() != null){
            user.setDeposit(request.getDeposit());
        }
        if (request.getRole() != null && !request.getRole().isEmpty()){
            if(!request.getRole().equalsIgnoreCase("seller")&&request.getRole().equalsIgnoreCase("buyer")){
                throw new RuntimeException("Invalid role");
            }
            user.setRole(request.getRole());
        }
        user = userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public User deleteUser(String username) {
        Optional<User>optUser = userRepository.findByUsername(username);
        User user = optUser.orElseThrow(() -> new UsernameNotFoundException("username not found"));
        userRepository.delete(user);
        user.setPassword(null);
        return user;
    }
}
