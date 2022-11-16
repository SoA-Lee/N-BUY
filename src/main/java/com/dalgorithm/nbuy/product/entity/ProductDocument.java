package com.dalgorithm.nbuy.product.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "product")
public class ProductDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String productTitle;

    private Long categoryId;
    private String recruiterId;

    private int price;
    private int totalPeople;
    private int numberApplication;

    private String sharingPlace;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public static ProductDocument from(Product product){
        return ProductDocument.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .productTitle(product.getProductTitle())
                .recruiterId(product.getRecruiterId())
                .price(product.getPrice())
                .totalPeople(product.getTotalPeople())
                .numberApplication(product.getNumberApplication())
                .sharingPlace(product.getSharingPlace())
                .productStatus(product.getProductStatus())
                .build();
    }
}
