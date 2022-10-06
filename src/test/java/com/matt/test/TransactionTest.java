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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    private Product p;
    private  User u1,u;


    @BeforeEach
    void setup(){

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        u = new User();
        u.setPassword("test123");
        u.setRole(Role.BUYER);
        u.setUsername("Tester");
        u.setDeposit(20);
        userRepository.save(u);

        u1 = new User();
        u1.setPassword("test123");
        u1.setRole(Role.SELLER);
        u1.setUsername("Tester2");
        u1.setDeposit(20);
        userRepository.save(u);

        p = new Product();
        p.setAmountAvailable(10L);
        p.setProductName("Biscuit");
        p.setSellerId(u);
        p.setCost(10);
        p = productRepository.save(p);
    }

    @AfterEach
    void destroy(){
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "Tester",authorities = {"BUYER"})
    void whenUserAmountIsEnoughToBuyProduct_success() throws Exception{
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(p.getId());
        request.setQuantity(3);
        String req = mapper.writeValueAsString(request);
        this.mockMvc.perform(post("/buy").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(req))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.description").value("Insufficient Amount"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Tester",authorities = {"BUYER"})
    public void whenBuyerHasLowCredit() throws Exception {
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(p.getId());
        request.setQuantity(2);
        String req = mapper.writeValueAsString(request);
        this.mockMvc.perform(post("/buy").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(req))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    @WithMockUser(username = "Tester",authorities = {"BUYER"})
    void whenBuyerDeposit_success() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setCoin(50);
        String req = mapper.writeValueAsString(request);
        this.mockMvc.perform(post("/deposit").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(req))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    @WithMockUser(username = "Tester",authorities = {"BUYER"})
    void whenBuyerDepositWrongCoinValue_Thr() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setCoin(53);
        String req = mapper.writeValueAsString(request);
        this.mockMvc.perform(post("/deposit").accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(req))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                //.andExpect(jsonPath("$description").value("Amount not permitted"))
                .andDo(print());
    }

}
