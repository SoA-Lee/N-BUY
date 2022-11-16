package com.dalgorithm.nbuy.order.service.serviceImpl;

import com.dalgorithm.nbuy.exception.impl.order.DuplicatedOrderInfoException;
import com.dalgorithm.nbuy.exception.impl.order.OrderNotFoundException;
import com.dalgorithm.nbuy.exception.impl.product.WithdrawnProductException;
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

        Product product = productRepository.findByIdWithPessimisticLock(parameter.getProductId());

        if (parameter.getApplicantId().equals(product.getRecruiterId())) {
            throw new RuntimeException("본인의 같이구매 상품에 대해서는 주문 신청이 불가능합니다.");
        } else if (product.getProductStatus().equals(ProductStatus.WITHDRAW)) {
            throw new WithdrawnProductException();
        }

        long count = orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(parameter.getProductId(), parameter.getApplicantId(), Collections.singleton(OrderStatus.REQ));

        if (count > 0) {
            throw new DuplicatedOrderInfoException();
        }

        increaseProductApplyStatus(product);

        Order order = Order.builder()
                .productId(product.getId())
                .applicantId(parameter.getApplicantId())
                .orderStatus(OrderStatus.REQ)
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);
        log.info("[" + order.getApplicantId() + "]" + "님의 [주문 번호-"+ order.getId() +"] 신청 완료");
    }

    @Override
    public List<OrderDto> myApply(String applicantId) {
        OrderInput parameter = new OrderInput();
        parameter.setApplicantId(applicantId);

        return orderMapper.selectListMyOrder(parameter);
    }

    @Override
    public void cancelPurchase(OrderInput parameter) {
        Order order = orderRepository.findById(parameter.getOrderId()).orElseThrow(OrderNotFoundException::new);

        decreaseProductApplyStatus(parameter.getProductId());

        order.setOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);

        log.info("[" + order.getApplicantId() + "]" + "님의 [주문 번호-"+ order.getId() +"] 취소 완료");
    }

    private void increaseProductApplyStatus(Product product) {
        int numberApplication = product.getNumberApplication() + 1;

        if (numberApplication == product.getTotalPeople()) {
            product.setProductStatus(ProductStatus.FINISH);
        } else if (numberApplication > product.getTotalPeople()) {
            throw new IllegalStateException("신청 인원이 모집 인원 수를 수를 초과하였습니다.");
        }
        product.setNumberApplication(numberApplication);
        productRepository.save(product);
    }

    private void decreaseProductApplyStatus(long productId) {
        Product product = productRepository.findByIdWithPessimisticLock(productId);

        if (product.getProductStatus().equals(ProductStatus.FINISH)) {
            throw new RuntimeException("인원 모집이 마감되어 취소가 불가능합니다.");
        }

        int numberApplication = product.getNumberApplication() - 1;
        product.setNumberApplication(numberApplication);

        productRepository.save(product);
    }
}
