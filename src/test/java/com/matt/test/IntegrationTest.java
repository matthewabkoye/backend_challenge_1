package com.matt.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.test.dto.CreateUserRequest;
import com.matt.test.enums.Role;
import com.matt.test.model.User;
import com.matt.test.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        User u = new User();
        u.setPassword("pass1231");
        u.setUsername("testUser1");
        u.setDeposit(50);
        u.setRole(Role.SELLER);
        userRepository.save(u);

        u = new User();
        u.setPassword("pass1232");
        u.setUsername("testUser2");
        u.setDeposit(50);
        u.setRole(Role.BUYER);
        userRepository.save(u);
    }

    @AfterEach
    void desstroy(){
        userRepository.deleteAll();
    }

    @Test
    void testUserPOST_success() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("pass123");
        createUserRequest.setRole(Role.SELLER);
        createUserRequest.setDeposit(100D);
        String req = mapper.writeValueAsString(createUserRequest);
        this.mockMvc.perform(post("/user").accept(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .content(req))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    void testUserFetch_success() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testUser1","pass1231");
        this.mockMvc.perform(get("/user").headers(headers))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
