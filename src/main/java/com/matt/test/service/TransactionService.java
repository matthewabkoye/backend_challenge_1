package com.matt.test.service;

import com.matt.test.dto.PurchaseResponse;
import com.matt.test.model.User;

public interface TransactionService {
    public User deposit(int coin);
    public PurchaseResponse buy(int quantity, Long productId);
    public User reset();
}
