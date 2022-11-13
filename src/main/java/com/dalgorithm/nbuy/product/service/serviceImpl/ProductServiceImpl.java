package com.dalgorithm.nbuy.product.service.serviceImpl;

import com.dalgorithm.nbuy.exception.impl.product.ProductNotFoundException;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.order.repository.OrderRepository;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.repository.ProductRepository;
import com.dalgorithm.nbuy.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    @Override
    public Page<ProductDto> list(ProductParam param, Pageable pageable) {

        if (param.getCategoryId() < 1) {
            pageable = PageRequest.of(
                    pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                    pageable.getPageSize());
            return productRepository.findAll(pageable).map(ProductDto::fromEntity);
        }

        Optional<List<Product>> optionalProducts = productRepository.findByCategoryId(param.getCategoryId());

        if (optionalProducts.isPresent()) {
            List<ProductDto> productList = ProductDto.fromEntity(optionalProducts.get());

            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), productList.size());

            return new PageImpl<>(productList.subList(start, end), pageable, productList.size());
        }

        throw new ProductNotFoundException();
    }

    @Override
    public ProductDto getById(Long id) {
        return productRepository.findById(id).map(ProductDto::fromEntity).orElse(null);
    }

    @Override
    public void addProduct(Product product) {
        log.info("[상품] " + product.getProductTitle() + "을 등록합니다.");
        productRepository.save(product);
    }

    @Override
    public ProductDto detailProduct(long id) {
        log.info("[상품 번호] " + id + " 상품을 조회합니다.");
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return ProductDto.fromEntity(product);
    }

    @Override
    public void deleteProduct(long id, Principal principal) {
        ProductDto productDto = ProductDto.fromEntity(productRepository.findById(id)
                        .orElseThrow(ProductNotFoundException::new));

        if (productDto.getRecruiterId().equals(principal.getName())){
            productRepository.deleteById(id);
            setOrderStatusWithdraw(id);
            log.info("[상품 번호] " + id + " 상품을 삭제합니다.");
        } else throw new RuntimeException("해당 상품에 접근 권한이 존재하지 않습니다.");
    }

    private void setOrderStatusWithdraw(long id) {
        List<Order> orderList = orderRepository.findAllByProductId(id);
        for ( Order order : orderList) {
            order.setOrderStatus(OrderStatus.FORCE_DELETE);
        }
        orderRepository.saveAll(orderList);
    }
}
