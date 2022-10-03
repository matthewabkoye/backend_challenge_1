package com.matt.test.service.impl;

import com.matt.test.dto.PurchaseResponse;
import com.matt.test.exceptions.NoPermissionException;
import com.matt.test.exceptions.NotFoundException;
import com.matt.test.model.Product;
import com.matt.test.model.User;
import com.matt.test.repo.ProductRepository;
import com.matt.test.repo.UserRepository;
import com.matt.test.service.TransactionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public TransactionServiceImpl(UserRepository userRepository,
                                  ProductRepository productRepository){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    @Override
    public User deposit(int coin) {
        if(!checkDeposit(coin)){
            throw new NoPermissionException("Amount not permitted");
        }
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User>optionalUser = userRepository.findByUsername(principal);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeposit(coin);
        user = userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public PurchaseResponse buy(int quantity, Long productId) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User>optionalUser = userRepository.findByUsername(principal);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));

        Optional<Product>optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product not found"));

        double amount = quantity * product.getCost();
        if(amount > user.getDeposit()){
            throw new NoPermissionException("Insufficient Amount");
        }
        double balance = user.getDeposit()-amount;
        user.setDeposit(balance);
        userRepository.save(user);

        PurchaseResponse response = new PurchaseResponse();
        response.setChange(balance);
        response.setProductName(product.getProductName());
        response.setQuantity(quantity);
        response.setTotalAmount(amount);

        return response;
    }

    @Override
    public User reset() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User>optionalUser = userRepository.findByUsername(principal);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeposit(0);
        user =userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    private boolean checkDeposit(int coin){
        List<Integer>acceptable = Arrays.asList(5,10,20,50,100);
        return acceptable.contains(coin);
    }
}
