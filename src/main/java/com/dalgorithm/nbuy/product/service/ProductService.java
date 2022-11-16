package com.dalgorithm.nbuy.product.service;

import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    @Transactional(readOnly = true)
    Page<ProductDto> list(ProductParam productParam, Pageable pageable);

    @Transactional(readOnly = true)
    ProductDto getById(Long id);

    @Transactional
    void addProduct(Product product);

    @Transactional
    ProductDto detailProduct(long id);

    @Transactional
    void cancelRecruitProduct(long id, String userId);

    @Transactional(readOnly = true)
    List<ProductDto> myRecruit(String recruiterId);

    @Transactional
    void deleteProduct(long id);

    @Transactional
    void saveAllProductDocuments();

    @Transactional(readOnly = true)
    List<ProductDto> searchProduct(String productTitle);
}
