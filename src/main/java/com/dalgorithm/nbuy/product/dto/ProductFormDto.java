package com.dalgorithm.nbuy.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFormDto {

    private Long id;
    private Long categoryId;
    private String recruiterId;

    @NotNull(message = "제목은 필수 항목입니다.")
    private String productTitle;

    private String productUrl;

    @NotNull(message = "가격은 필수 항목입니다.")
    private int price;

    @NotNull(message = "총 인원은 필수 항목입니다.")
    private int totalPeople;

    @NotNull(message = "나눔 장소는 필수 항목입니다.")
    private String sharingPlace;

    @NotNull(message = "나눔 날짜는 필수 항목입니다.")
    private String sharingDay;

    private String imgName;
    private String imgUrl;
}
