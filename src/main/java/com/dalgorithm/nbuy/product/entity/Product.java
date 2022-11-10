package com.dalgorithm.nbuy.product.entity;

import com.dalgorithm.nbuy.product.dto.ProductFormDto;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;
    private String recruiterId;

    private String productTitle;

    @Column(length = 1000)
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

    public static Product createProduct(ProductFormDto productFormDto) {
        return Product.builder()
                .categoryId(productFormDto.getCategoryId())
                .recruiterId(productFormDto.getRecruiterId())
                .productTitle(productFormDto.getProductTitle())
                .productUrl(productFormDto.getProductUrl())
                .price(productFormDto.getPrice())
                .productStatus(ProductStatus.PROGRESS)
                .numberApplication(0)
                .regDate(LocalDateTime.now())
                .totalPeople(productFormDto.getTotalPeople())
                .sharingPlace(productFormDto.getSharingPlace())
                .sharingDay(productFormDto.getSharingDay())
                .build();
    }
}
