package com.dalgorithm.nbuy.product.repository;

import com.dalgorithm.nbuy.product.entity.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsProductRepository extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findByProductTitleContains(String productTitle);
}
