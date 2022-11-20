package com.dalgorithm.nbuy.admin.controller;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 리스트 정렬하기")
    @GetMapping()
    public String listCategory(Model model) {

        List<CategoryDto> list = categoryService.getCategoryList();
        model.addAttribute("list", list);

        return "admin/category/list";
    }


    @ApiOperation(value = "입력한 문자열로 카테고리 생성하기")
    @PostMapping("/add")
    public String addCategory(String categoryName) {

        categoryService.addCategory(categoryName);

        return "redirect:/admin/categories";
    }

    @ApiOperation(value = "선택한 카테고리 삭제하기")
    @PostMapping("/delete")
    public String deleteCategory(CategoryDto categoryDto) {

        categoryService.deleteCategory(categoryDto.getId());

        return "redirect:/admin/categories";
    }

    @ApiOperation(value = "선택한 카테고리 수정하기")
    @PostMapping("/update")
    public String updateCategory(CategoryDto categoryDto) {

        categoryService.updateCategory(categoryDto);

        return "redirect:/admin/categories";
    }

}
