package com.dalgorithm.nbuy.product.repository;

import com.dalgorithm.nbuy.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByCategoryId(long categoryId);
}