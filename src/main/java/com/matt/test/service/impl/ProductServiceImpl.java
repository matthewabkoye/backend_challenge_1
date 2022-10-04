package com.matt.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matt.test.dto.CreateProductRequest;
import com.matt.test.dto.CreateProductResponse;
import com.matt.test.dto.UpdateProductRequest;
import com.matt.test.exceptions.NoPermissionException;
import com.matt.test.exceptions.NotFoundException;
import com.matt.test.model.Product;
import com.matt.test.model.User;
import com.matt.test.repo.ProductRepository;
import com.matt.test.repo.UserRepository;
import com.matt.test.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository,
                              ObjectMapper mapper){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }
    @Override
    public CreateProductResponse create(CreateProductRequest request, Principal principal) {
        //log.info("Principal :: {}",principal);

       // String principal = SecurityContextHolder.getContext().getAuthentication().getName();
       Optional<User>optUser = userRepository.findByUsername(principal.getName());

       Product product = mapper.convertValue(request,Product.class);
       product.setSellerId(optUser.get());
       product = productRepository.save(product);
       CreateProductResponse resp = new CreateProductResponse();
       resp.setProductName(product.getProductName());
       resp.setCost(product.getCost());
       resp.setAmountAvailable(product.getAmountAvailable());
       resp.setSellerId(product.getSellerId().getId());
       return resp;
    }

    @Override
    public List<Product> fetch(Long productId, Integer page, Integer pageSize, Principal principal) {
        if(productId == null){
            PageRequest pageRequest = PageRequest.of(page-1, pageSize);
            Page<Product>productPage = productRepository.findAll(pageRequest);
            return productPage.getContent();
        }else{
            Optional<Product>optProduct = productRepository.findById(productId);
            Product p = optProduct.orElseThrow(() -> new NotFoundException("Product name not find"));
            return Arrays.asList(p);
        }
    }

    @Override
    public Product update(UpdateProductRequest request, Long productId, Principal principal) {
        Optional<User>optUser = userRepository.findByUsername(principal.getName());

        Optional<Product>optional = productRepository.findById(productId);
        Product productUpdate = optional.orElseThrow(() -> new NotFoundException("Product Id not found"));
        if(optUser.get().getId() != productUpdate.getSellerId().getId()){
            throw new NoPermissionException("You cannot update product you did not create");
        }
        productUpdate.setCost(request.getCost());
        productUpdate.setProductName(request.getProductName());
        productUpdate.setAmountAvailable(request.getAmountAvailable());
        productUpdate = productRepository.save(productUpdate);
        return productUpdate;
    }

    @Override
    public Product delete(Long productId, Principal principal) {
        Optional<User>optUser = userRepository.findByUsername(principal.getName());

        Optional<Product>optional = productRepository.findById(productId);
        Product productDelete = optional.orElseThrow(() -> new NotFoundException("Product Id not found"));
        if(optUser.get().getId() != productDelete.getSellerId().getId()){
            throw new NoPermissionException("You cannot delete product you did not create");
        }
        productRepository.delete(productDelete);
        return productDelete;
    }
}
