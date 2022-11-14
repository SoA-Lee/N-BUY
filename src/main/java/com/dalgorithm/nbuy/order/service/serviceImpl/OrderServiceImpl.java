package com.dalgorithm.nbuy.order.service.serviceImpl;

import com.dalgorithm.nbuy.exception.impl.order.DuplicatedOrderInfoException;
import com.dalgorithm.nbuy.exception.impl.product.ProductNotFoundException;
import com.dalgorithm.nbuy.order.dto.OrderDto;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.order.mapper.OrderMapper;
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
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    @Override
    public void reqPurchase(OrderInput parameter) {

        Product product = productRepository.findById(parameter.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        if (parameter.getApplicantId().equals(product.getRecruiterId())) {
            throw new RuntimeException("본인의 같이구매 상품에 대해서는 주문 신청이 불가능합니다.");
        } else if (product.getProductStatus().equals(ProductStatus.WITHDRAW)) {
            throw new RuntimeException("관리자 및 등록자에 의해 삭제된 상품입니다.");
        } else if (product.getProductStatus().equals(ProductStatus.FINISH)) {
            throw new RuntimeException("모집이 마감된 상품입니다.");
        }

        long count = orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(parameter.getProductId(), parameter.getApplicantId(), Collections.singleton(OrderStatus.REQ));

        if (count > 0) {
            throw new DuplicatedOrderInfoException();
        }

        Order order = Order.builder()
                .productId(product.getId())
                .applicantId(parameter.getApplicantId())
                .orderStatus(OrderStatus.REQ)
                .orderDate(LocalDateTime.now())
                .build();

        updateProductApplicationStatus(product);

        orderRepository.save(order);
        log.info("[" + order.getApplicantId() + "]" + "님의 [주문 번호-"+ order.getId() +"] 신청 완료");
    }

    @Override
    public List<OrderDto> myApply(String applicantId) {
        OrderInput parameter = new OrderInput();
        parameter.setApplicantId(applicantId);

        return orderMapper.selectListMyOrder(parameter);
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
