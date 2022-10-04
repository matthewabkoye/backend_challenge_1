package com.matt.test.service;

import com.matt.test.dto.DepositRequest;
import com.matt.test.dto.PurchaseRequest;
import com.matt.test.dto.PurchaseResponse;
import com.matt.test.model.User;

public interface TransactionService {
    public User deposit(DepositRequest depositRequest);
    public PurchaseResponse buy(PurchaseRequest purchaseRequest);
    public User reset();
}
