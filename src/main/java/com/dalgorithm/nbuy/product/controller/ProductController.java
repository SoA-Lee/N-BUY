package com.dalgorithm.nbuy.product.controller;

import com.dalgorithm.nbuy.admin.controller.AdminProductController;
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

        AdminProductController.setPageWithCategory(model, pageable, productParam, productService, categoryService);

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

    @GetMapping("/detail/{id}")
    public String productDetail(Model model, ProductParam productParam, Principal principal) {

        String curUser = principal.getName();
        ProductDto productDto = productService.detailProduct(productParam.getId());

        model.addAttribute("detail", productDto);
        model.addAttribute("curUser", curUser);

        return "product/detail";
    }

    @PostMapping("/cancel")
    public String productCancel(Model model, ProductParam productParam, Principal principal) {

        productService.cancelRecruitProduct(productParam.getId(), principal.getName());
        model.addAttribute("successMessage", "상품 모집이 취소 되었습니다. 마이 페이지를 확인해주세요.");

        return "index";
    }

}
