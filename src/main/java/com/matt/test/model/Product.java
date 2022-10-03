package com.matt.test.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_available", unique = true)
    private Long amountAvailable;

    private double cost;

    @Column(name = "product_name", unique = true)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "Seller_id")
    private User sellerId;
}
