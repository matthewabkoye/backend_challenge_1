package com.matt.test;

import com.matt.test.dto.CreateUserRequest;
import com.matt.test.dto.CreateUserResponse;
import com.matt.test.enums.Role;
import com.matt.test.exceptions.NoPermissionException;
import com.matt.test.model.User;
import com.matt.test.repo.UserRepository;
import com.matt.test.service.UserService;
import static  org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


@SpringBootTest
public class UserServiceTest {
//    @Test
//    void contextLoads() {
//    }
    @Autowired
    private UserService userService;

    @Autowired
    private  UserRepository userRepository;
    public  User u;

    @BeforeEach
     void setup(){
        User u = new User();
        u.setDeposit(100.0);
        u.setRole(Role.SELLER);
        u.setPassword("test123");
        u.setUsername("testUsername");
        u= userRepository.save(u);

        User u2 = new User();
        u2.setDeposit(100.0);
        u2.setRole(Role.SELLER);
        u2.setPassword("pass234");
        u2.setUsername("testSeller");
        u2= userRepository.save(u2);

        u2 = new User();
        u2.setDeposit(100.0);
        u2.setRole(Role.BUYER);
        u2.setPassword("pass234");
        u2.setUsername("testBuyer");
        u2= userRepository.save(u2);
    }

    @AfterEach
    void destroy(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("whenCreatingUserWithWriteParam_success")
    void whenCreatingUserWithWriteParam_success(){
        CreateUserRequest request = new CreateUserRequest();
        request.setDeposit(100.0);
        request.setRole(Role.SELLER);
        request.setPassword("test123");
        request.setUsername("testUsername1");
        CreateUserResponse resp = userService.create(request);
        assertNotNull(resp.getId());
    }

    @Test
    @DisplayName("whenCreatingUserWithExistingUsername_throwNopermissionException")
    void whenCreatingUserWithExistingUsername_throwNopermissionException(){
        Throwable throwable = assertThrows(NoPermissionException.class,() ->{
            CreateUserRequest request2 = new CreateUserRequest();
            request2.setDeposit(100.0);
            request2.setRole(Role.SELLER);
            request2.setPassword("test123");
            request2.setUsername("testUsername");
            CreateUserResponse resp2 = userService.create(request2);
        });
        assertEquals("User with the username exist", throwable.getMessage());
    }

    @Test
    @DisplayName("whenFIndUserWithCorrectUsername_success")
    void whenFIndUserWithCorrectUsername_success(){
        List<User> userList = userService.fetchAllUsers("testUsername", null,null);
        assertEquals(1,userList.size());
        User u = userList.get(0);
        assertEquals(Role.SELLER, u.getRole());
    }

    @Test
    void whenFIndUserWithWrongUsername_throwNoPermissionException(){
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () ->{
            List<User> userList = userService.fetchAllUsers("testUsername34", null,null);
        });
        assertEquals("Username not found",throwable.getMessage());
    }

    @Test
    void whenFIndUserWithoutUsernameWithPage_success(){
        List<User> userList = userService.fetchAllUsers(null, 1,2);
        assertEquals(2,userList.size());
    }


}
