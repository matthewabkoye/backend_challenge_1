package com.matt.test.resp;

import com.matt.test.constant.RestBase;
import com.matt.test.dto.CreateUserRequest;
import com.matt.test.dto.CreateUserResponse;
import com.matt.test.dto.UpdateUserRequest;
import com.matt.test.dto.UpdateUserResponse;
import com.matt.test.model.User;
import com.matt.test.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<?>create(@RequestBody @Validated CreateUserRequest createUserRequest){
        CreateUserResponse user = userService.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<?>fetchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize
    ){
        List<User>users = userService.fetchAllUsers(username,page,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("{username}")
    public ResponseEntity<?>updateUser(@RequestBody @Validated UpdateUserRequest request,
                                       @PathVariable(required = false) String username){
        User resp = userService.updateUser(request,username);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping
    public ResponseEntity<?>delete(@RequestParam(required = true)String username){
        return ResponseEntity.ok( userService.deleteUser(username));
    }


}
