package com.dalgorithm.nbuy.product.repository;

import com.dalgorithm.nbuy.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByCategoryId(long categoryId);

    List<Product> findByRecruiterId(String userId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from product p where p.id = :id")
    Product findByIdWithPessimisticLock(Long id);
}
