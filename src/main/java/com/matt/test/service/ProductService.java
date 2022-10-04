package com.matt.test.service;

import com.matt.test.dto.*;
import com.matt.test.model.Product;
import com.matt.test.model.User;

import java.security.Principal;
import java.util.List;

public interface ProductService {
    CreateProductResponse create(CreateProductRequest request);
    List<Product> fetch(Long productId, Integer page, Integer pageSize, Principal principal);
    Product update(UpdateProductRequest request, Long productId, Principal principal);
    Product delete(Long productId, Principal principal);
}
