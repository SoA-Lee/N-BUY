package com.dalgorithm.nbuy.product.dto;

import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private long id;
    private Long categoryId;
    private String recruiterId;

    private String productTitle;
    private String productUrl;

    private int price;
    private int totalPeople;
    private int numberApplication;

    private String sharingPlace;
    private String sharingDay;
    private LocalDateTime regDate;

    private String imgName;
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .recruiterId(product.getRecruiterId())
                .productTitle(product.getProductTitle())
                .productUrl(product.getProductUrl())
                .price(product.getPrice())
                .totalPeople(product.getTotalPeople())
                .numberApplication(product.getNumberApplication())
                .sharingPlace(product.getSharingPlace())
                .sharingDay(product.getSharingDay())
                .regDate(product.getRegDate())
                .imgName(product.getImgName())
                .imgUrl(product.getImgUrl())
                .productStatus(product.getProductStatus())
                .build();
    }

    public static List<ProductDto> fromEntity(List<Product> productList) {
        return productList.stream().map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }
}
