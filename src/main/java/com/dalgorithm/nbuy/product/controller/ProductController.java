package com.dalgorithm.nbuy.product.controller;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.service.CategoryService;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductFormDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.entity.Product;
import com.dalgorithm.nbuy.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String productList(Model model, @PageableDefault(size = 5, sort = "id",
            direction = Sort.Direction.DESC ) Pageable pageable, ProductParam productParam) {

        Page<ProductDto> list = productService.list(productParam, pageable);

        int startPage = Math.max(1, list.getPageable().getPageNumber() - 4);
        int endPage = Math.min(list.getPageable().getPageNumber() + 4, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        int productTotalCount = 0;

        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());
        if (categoryList != null) {
            for(CategoryDto x : categoryList) {
                productTotalCount += x.getProductCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productTotalCount", productTotalCount);

        return "product/list";
    }

    @GetMapping("/new")
    public String productAdd(Model model, HttpServletRequest request
            , ProductFormDto productFormDto) {

        model.addAttribute("category", categoryService.getCategoryList());

        boolean editMode = request.getRequestURI().contains("/edit");
        ProductDto detail = new ProductDto();

        if (editMode) {
            long id = productFormDto.getId();
            ProductDto existProduct = productService.getById(id);
            if (existProduct == null) {
                model.addAttribute("message", "상품 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existProduct;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "product/add";
    }

    @PostMapping("/new")
    public String productAddSubmit(@Valid ProductFormDto productFormDto,
                                   Model model, Principal principal) {

        productFormDto.setRecruiterId(principal.getName());
        Product product = Product.createProduct(productFormDto);

        productService.addProduct(product);
        model.addAttribute("successMessage", "상품 등록이 완료되었습니다. 진행 상태를 확인해주세요.");

        return "index";
    }

    @GetMapping("/detail")
    ResponseEntity<?> productDetail() {
        // TODO
        return null;
    }

}
