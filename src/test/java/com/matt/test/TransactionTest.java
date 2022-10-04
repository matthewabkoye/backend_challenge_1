package com.matt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.test.dto.CreateUserRequest;
import com.matt.test.dto.DepositRequest;
import com.matt.test.dto.PurchaseRequest;
import com.matt.test.enums.Role;
import com.matt.test.model.Product;
import com.matt.test.model.User;
import com.matt.test.repo.ProductRepository;
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
import org.springframework.web.servlet.function.ServerRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    private Product p;
    private  User u1,u;
    @BeforeEach
    void setup(){
        u = new User();
        u.setPassword("test123");
        u.setRole(Role.BUYER);
        u.setUsername("Tester");
        u.setDeposit(100);
        userRepository.save(u);

        u1 = new User();
        u1.setPassword("test123");
        u1.setRole(Role.SELLER);
        u1.setUsername("Tester2");
        u1.setDeposit(100);
        userRepository.save(u);

        p = new Product();
        p.setAmountAvailable(10L);
        p.setProductName("Biscuit");
        p.setSellerId(u);
        p.setCost(5);
        p = productRepository.save(p);
    }

    @AfterEach
    void destroy(){
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenUserAmountIsEnoughToBuyProduct_success() throws Exception{
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(p.getId());
        request.setQuantity(3);
        String req = mapper.writeValueAsString(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(u.getUsername(),"test123");
        this.mockMvc.perform(post("/buy").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .headers(headers)
                        .content(req))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    void whenBuyerDeposit_success() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setCoin(50);
        String req = mapper.writeValueAsString(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(u.getUsername(),"test123");
        this.mockMvc.perform(post("/deposit").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .headers(headers)
                        .content(req))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

}
