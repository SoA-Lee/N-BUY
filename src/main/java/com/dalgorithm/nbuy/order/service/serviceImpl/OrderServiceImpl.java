package com.dalgorithm.nbuy.order.service.serviceImpl;

import com.dalgorithm.nbuy.exception.impl.order.DuplicatedOrderInfoException;
import com.dalgorithm.nbuy.exception.impl.product.ProductNotFoundException;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.order.repository.OrderRepository;
import com.dalgorithm.nbuy.order.service.OrderService;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import com.dalgorithm.nbuy.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public void reqPurchase(OrderInput parameter) {

        Product product = productRepository.findById(parameter.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        if (parameter.getApplicantId().equals(product.getRecruiterId())) {
            throw new RuntimeException("본인의 같이구매 상품에 대해서는 주문 신청이 불가능합니다.");
        }

        long count = orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(parameter.getProductId(), parameter.getApplicantId(), Collections.singleton(OrderStatus.REQ));

        if (count > 0) {
            throw new DuplicatedOrderInfoException();
        }

        Order order = Order.builder()
                .productId(product.getId())
                .applicantId(parameter.getApplicantId())
                .orderStatus(OrderStatus.REQ)
                .productStatus(product.getProductStatus())
                .orderDate(LocalDateTime.now())
                .build();

        updateProductApplicationStatus(product);

        orderRepository.save(order);
        log.info("[" + order.getApplicantId() + "]" + "님의 [주문 번호-"+ order.getId() +"] 신청 완료");
    }

    private void updateProductApplicationStatus(Product product) {
        int numberApplication = product.getNumberApplication() + 1;

        if (numberApplication == product.getTotalPeople()) {
            product.setProductStatus(ProductStatus.FINISH);
            product.setNumberApplication(numberApplication);
        } else if (numberApplication > product.getTotalPeople()) {
            throw new IllegalStateException("신청 인원이 모집 인원 수를 수를 초과하였습니다.");
        }
        product.setNumberApplication(numberApplication);
        productRepository.save(product);
    }
}
