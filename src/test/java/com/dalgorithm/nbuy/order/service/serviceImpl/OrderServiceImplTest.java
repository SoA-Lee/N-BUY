package com.dalgorithm.nbuy.order.service.serviceImpl;

import com.dalgorithm.nbuy.exception.AbstractException;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.order.repository.OrderRepository;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import com.dalgorithm.nbuy.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("주문 신청하기 성공")
    void reqPurchase() {
        long orderId = 1L;
        long productId = 2L;
        String userId1 = "abc";
        String userId2 = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);
        orderInput.setApplicantId(userId1);

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId2)
                .productStatus(ProductStatus.PROGRESS)
                .totalPeople(10)
                .numberApplication(0)
                .build();

        // given
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        given(orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(
                any(), any(), any())).willReturn(0L);

        // when
        orderService.reqPurchase(orderInput);

        // then
        assertEquals(product.getNumberApplication(), 1);
    }

    @Test
    @DisplayName("주문 신청하기 실패 - 진행자와 신청자 동일")
    void reqPurchaseFailedByNotFoundOrder() {
        long orderId = 1L;
        long productId = 2L;
        String userId1 = "abc";
        String userId2 = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);
        orderInput.setApplicantId(userId1);

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId1)
                .productStatus(ProductStatus.PROGRESS)
                .totalPeople(10)
                .numberApplication(0)
                .build();

        // given
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> orderService.reqPurchase(orderInput));

        // then
        assertEquals(exception.getMessage(), "본인의 같이구매 상품에 대해서는 주문 신청이 불가능합니다.");
    }

    @Test
    @DisplayName("주문 신청하기 실패 - 모집 중지된 상품")
    void reqPurchaseFailedByWithdrawn() {
        long orderId = 1L;
        long productId = 2L;
        String userId1 = "abc";
        String userId2 = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);
        orderInput.setApplicantId(userId1);

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId2)
                .productStatus(ProductStatus.WITHDRAW)
                .totalPeople(10)
                .numberApplication(0)
                .build();

        // given
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> orderService.reqPurchase(orderInput));

        // then
        assertEquals(exception.getMessage(), "관리자 및 등록자에 의해 삭제된 상품입니다.");
    }

    @Test
    @DisplayName("주문 신청하기 실패 - 동일 상품에 대한 신청 이력 존재")
    void reqPurchaseFailedByAlreadyOrder() {
        long orderId = 1L;
        long productId = 2L;
        String userId1 = "abc";
        String userId2 = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);
        orderInput.setApplicantId(userId1);

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId2)
                .productStatus(ProductStatus.PROGRESS)
                .totalPeople(10)
                .numberApplication(0)
                .build();

        // given
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        given(orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(
                any(), any(), any())).willReturn(1L);

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> orderService.reqPurchase(orderInput));

        // then
        assertEquals(exception.getMessage(), "해당 상품에 대해 중복된 신청 정보가 존재합니다.");
    }

    @Test
    @DisplayName("주문 신청하기 실패 - 모집 인원 수 초과")
    void reqPurchaseFailedByOverTotalNumber() {
        long orderId = 1L;
        long productId = 2L;
        String userId1 = "abc";
        String userId2 = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);
        orderInput.setApplicantId(userId1);

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId2)
                .productStatus(ProductStatus.PROGRESS)
                .totalPeople(10)
                .numberApplication(10)
                .build();

        // given
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        given(orderRepository.countByProductIdAndApplicantIdAndOrderStatusIn(
                any(), any(), any())).willReturn(0L);

        // when
        Exception exception = assertThrows(
                Exception.class, () -> orderService.reqPurchase(orderInput));

        // then
        assertEquals(exception.getMessage(), "신청 인원이 모집 인원 수를 수를 초과하였습니다.");
    }

    @Test
    @DisplayName("주문 신청 취소하기 성공")
    void cancelPurchase() {
        long orderId = 1L;
        long productId = 2L;
        String userId = "qwe";

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);

        Order order = Order.builder()
                .id(orderId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .recruiterId(userId)
                .productStatus(ProductStatus.PROGRESS)
                .numberApplication(0)
                .build();

        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));
        given(productRepository.findByIdWithPessimisticLock(anyLong()))
                .willReturn(product);

        // when
        orderService.cancelPurchase(orderInput);

        // then
        assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("주문 신청 취소하기 실패 - 존재하지 않는 주문")
    void cancelPurchaseFailedByNotFoundOrder() {
        long orderId = 1L;
        long productId = 2L;

        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(orderId);
        orderInput.setProductId(productId);

        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> orderService.cancelPurchase(orderInput));

        // then
        assertEquals(exception.getMessage(), "존재하지 않는 주문 정보입니다.");
    }
}