package com.dalgorithm.nbuy.product.service;

import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDto> list(ProductParam productParam, Pageable pageable);
    ProductDto getById(Long id);

    void addProduct(Product product);
}
