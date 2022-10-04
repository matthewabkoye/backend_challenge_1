package com.matt.test.resp;

import com.matt.test.dto.CreateProductRequest;
import com.matt.test.dto.CreateProductResponse;
import com.matt.test.dto.DepositRequest;
import com.matt.test.dto.PurchaseRequest;
import com.matt.test.service.ProductService;
import com.matt.test.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService =  transactionService;
    }


    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("deposit")
    public ResponseEntity<?> deposit(@RequestBody @Validated DepositRequest request){
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("buy")
    public ResponseEntity<?> buy(@RequestBody @Validated PurchaseRequest request){
        return ResponseEntity.ok(transactionService.buy(request));
    }

}
