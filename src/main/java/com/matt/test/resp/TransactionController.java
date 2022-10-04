package com.matt.test.resp;

import com.matt.test.dto.CreateProductRequest;
import com.matt.test.dto.CreateProductResponse;
import com.matt.test.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product1")
public class TransactionController {
    private final ProductService productService;

    public TransactionController(ProductService productService) {
        this.productService =  productService;
    }


    @PreAuthorize("hasRole('Seller')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated CreateProductRequest request){
        CreateProductResponse product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

}
