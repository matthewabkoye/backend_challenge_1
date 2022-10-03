package com.matt.test.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne
    private Product product;
    private Integer quantity;
    @ManyToOne
    private User buyer;

    @PrePersist
    public void setCreated(){
        created = LocalDateTime.now();
    }

}
