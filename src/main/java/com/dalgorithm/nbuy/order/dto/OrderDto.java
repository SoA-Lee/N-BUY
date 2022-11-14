package com.dalgorithm.nbuy.order.dto;

import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private Long productId;

    private String applicantId;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private String productTitle;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .applicantId(order.getApplicantId())
                .orderDate(order.getOrderDate())
                .build();
    }

    public static List<OrderDto> fromEntity(List<Order> orderList) {
        return orderList.stream().map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
}
