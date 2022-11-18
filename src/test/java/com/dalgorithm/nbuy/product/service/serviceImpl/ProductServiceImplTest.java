package com.dalgorithm.nbuy.product.service.serviceImpl;

import com.dalgorithm.nbuy.exception.AbstractException;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.repository.OrderRepository;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import com.dalgorithm.nbuy.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("상품 id 가져오기 성공")
    void getById() {
        Long id = 1L;
        String title = "title";

        Product product = Product.builder()
                .id(1L)
                .productTitle(title)
                .build();

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        ProductDto productDto = productService.getById(id);

        // then
        assertEquals(productDto.getProductTitle(), title);
    }


    @Test
    @DisplayName("상품 상세정보 가져오기 성공")
    void detailProduct() {
        long id = 1L;
        String title = "title";
        String recruiterId = "abc";

        Product product = Product.builder()
                .id(1L)
                .productTitle(title)
                .recruiterId(recruiterId)
                .build();

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        ProductDto productDto = productService.detailProduct(id);

        // then
        assertEquals(productDto.getProductTitle(), title);
        assertEquals(productDto.getRecruiterId(), recruiterId);
    }

    @Test
    @DisplayName("상품 상세정보 가져오기 실패 - 존재하지 않는 상품")
    void detailProductFailedByNotFoundProduct() {

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> productService.deleteProduct(1L));

        // then
        assertEquals(exception.getMessage(), "존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 모집 취소하기 성공")
    void cancelRecruitProduct() {
        long id = 1L;
        String userId = "abc";

        Product product = Product.builder()
                .id(id)
                .recruiterId(userId)
                .build();

        Order order1 = Order.builder()
                .productId(id)
                .build();

        List<Order> orderList = Collections.singletonList(order1);

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(orderRepository.findAllByProductId(anyLong()))
                .willReturn(orderList);

        // when
        productService.cancelRecruitProduct(id, userId);

        // then
        assertEquals(product.getProductStatus(), ProductStatus.WITHDRAW);
    }

    @Test
    @DisplayName("상품 모집 취소하기 실패 - 존재하지 않는 상품")
    void cancelRecruitProductFailedByNotFoundProduct() {
        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> productService.cancelRecruitProduct(1L, "abc"));

        // then
        assertEquals(exception.getMessage(), "존재하지 않는 상품입니다.");
    }


    @Test
    @DisplayName("상품 모집 취소하기 실패 - 접근 권한 없음")
    void cancelRecruitProductFailedByAccessDenied() {
        long id = 1L;
        String userId = "abc";

        Product product = Product.builder()
                .id(id)
                .recruiterId(userId)
                .build();

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> productService.cancelRecruitProduct(1L, "qwe"));

        // then
        assertEquals(exception.getMessage(), "해당 상품에 접근 권한이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 삭제하기 성공")
    void deleteProduct() {
        long id = 1L;

        Product product = Product.builder()
                .id(id)
                .build();

        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when
        productService.deleteProduct(id);

        Optional<Product> result = productRepository.findById(id);

        // then
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("상품 삭제하기 실패 - 존재하지 않는 상품")
    void deleteProductFailedByNotFoundProduct() {
        // given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> productService.deleteProduct(1L));

        // then
        assertEquals(exception.getMessage(), "존재하지 않는 상품입니다.");
    }
}