package com.matt.test.resp;

import com.matt.test.dto.CreateProductRequest;
import com.matt.test.dto.CreateProductResponse;
import com.matt.test.dto.UpdateProductRequest;
import com.matt.test.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/product")
@CrossOrigin
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService =  productService;
    }


    @PreAuthorize("hasAuthority('SELLER')")
    @PostMapping
    public ResponseEntity<?>create(@RequestBody @Validated CreateProductRequest request, Principal principal){
        CreateProductResponse product = productService.create(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping
    public ResponseEntity<?> getProduct(@RequestParam(name = "productName",required = false)Long productId,
                                        @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                        Principal principal) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.fetch(productId,page,pageSize,principal)) ;
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateProductRequest request,
                                    @RequestParam(value = "productId",required = true) Long productId,Principal principal){
        return ResponseEntity.ok(productService.update(request,productId, principal));
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @DeleteMapping
    public ResponseEntity delete(@RequestParam(required = true)Long productId, Principal principal){
        return ResponseEntity.ok(productService.delete(productId, principal));
    }
}
