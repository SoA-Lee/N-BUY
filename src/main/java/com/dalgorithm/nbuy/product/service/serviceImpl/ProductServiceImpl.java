package com.dalgorithm.nbuy.product.service.serviceImpl;

import com.dalgorithm.nbuy.exception.impl.product.ProductNotFoundException;
import com.dalgorithm.nbuy.order.entity.Order;
import com.dalgorithm.nbuy.order.entity.OrderStatus;
import com.dalgorithm.nbuy.order.repository.OrderRepository;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.entity.ProductDocument;
import com.dalgorithm.nbuy.product.entity.ProductStatus;
import com.dalgorithm.nbuy.product.repository.EsProductRepository;
import com.dalgorithm.nbuy.product.repository.ProductRepository;
import com.dalgorithm.nbuy.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final EsProductRepository esProductRepository;

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
    public void cancelRecruitProduct(long id, String userId) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (product.getRecruiterId().equals(userId)){
            product.setProductStatus(ProductStatus.WITHDRAW);

            setOrderStatusWithdraw(id);
            log.info("[상품 번호] " + id + " 상품 모집을 취소합니다.");

            productRepository.save(product);
        } else throw new AccessDeniedException("해당 상품에 접근 권한이 존재하지 않습니다.");
    }

    @Override
    public List<ProductDto> myRecruit(String recruiterId) {
        List<Product> productList = productRepository.findByRecruiterId(recruiterId);
        return ProductDto.fromEntity(productList);
    }

    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        log.info("[상품 번호 - " + product.getId() + "] 상품을 삭제합니다.");
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> searchProduct(String productTitle) {
        return esProductRepository.findByProductTitleContains(productTitle)
                .stream()
                .map(ProductDto::fromEs)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "${scheduler.save.product}")
    public void saveAllProductDocuments() {
        List<ProductDocument> productDocuments
                = productRepository.findAll().stream().map(ProductDocument::from).collect(Collectors.toList());
        log.info("[" + LocalDateTime.now() + "] : ES REPO 업데이트");
        esProductRepository.saveAll(productDocuments);
    }

    private void setOrderStatusWithdraw(long id) {
        List<Order> orderList = orderRepository.findAllByProductId(id);
        for ( Order order : orderList) {
            order.setOrderStatus(OrderStatus.FORCE_DELETE);
        }
        orderRepository.saveAll(orderList);
    }
}
