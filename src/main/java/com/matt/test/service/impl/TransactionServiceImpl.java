package com.matt.test.service.impl;

import com.matt.test.dto.DepositRequest;
import com.matt.test.dto.PurchaseRequest;
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
    public User deposit(DepositRequest depositRequest) {
        if(!checkDeposit(depositRequest.getCoin())){
            throw new NoPermissionException("Amount not permitted");
        }
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User>optionalUser = userRepository.findByUsername(principal);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeposit(depositRequest.getCoin()+user.getDeposit());
        user = userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public PurchaseResponse buy(PurchaseRequest purchaseRequest) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User>optionalUser = userRepository.findByUsername(principal);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));

        Optional<Product>optionalProduct = productRepository.findById(purchaseRequest.getProductId());
        Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product not found"));

        double amount = purchaseRequest.getQuantity() * product.getCost();
        if(purchaseRequest.getQuantity() > product.getAmountAvailable()){
            throw new NoPermissionException("Low Stock: the available product in stock is "+product.getAmountAvailable());
        }
        if(amount > user.getDeposit()){
            throw new NoPermissionException("Insufficient Amount");
        }

        double balance = user.getDeposit()-amount;
        user.setDeposit(balance);
        userRepository.save(user);

        PurchaseResponse response = new PurchaseResponse();
        response.setChange(balance);
        response.setProductName(product.getProductName());
        response.setQuantity(purchaseRequest.getQuantity());
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
