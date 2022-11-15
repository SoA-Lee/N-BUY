package com.dalgorithm.nbuy.admin.controller;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.service.CategoryService;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.dto.ProductParam;
import com.dalgorithm.nbuy.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String productList(Model model, @PageableDefault(size = 5, sort = "id",
            direction = Sort.Direction.DESC ) Pageable pageable, ProductParam productParam) {

        setPageWithCategory(model, pageable, productParam, productService, categoryService);

        return "admin/product/list";
    }

    @PostMapping("/delete")
    public String productDelete(Model model, ProductParam productParam) {

        productService.deleteProduct(productParam.getId());
        model.addAttribute("successMessage", "상품 삭제가 완료되었습니다.");

        return "redirect:/admin/products";
    }

    public static void setPageWithCategory(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
                                            , ProductParam productParam, ProductService productService, CategoryService categoryService) {
        Page<ProductDto> list = productService.list(productParam, pageable);

        int startPage = Math.max(1, list.getPageable().getPageNumber() - 4);
        int endPage = Math.min(list.getPageable().getPageNumber() + 4, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        int productTotalCount = 0;

        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());
        if (categoryList != null) {
            for (CategoryDto x : categoryList) {
                productTotalCount += x.getProductCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productTotalCount", productTotalCount);
    }
}
