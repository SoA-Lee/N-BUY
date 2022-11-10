package com.dalgorithm.nbuy.order.entity;

import com.dalgorithm.nbuy.product.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String categoryName;

    private String applicantId;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
}
