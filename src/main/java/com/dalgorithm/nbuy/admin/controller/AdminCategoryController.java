package com.dalgorithm.nbuy.admin.controller;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.service.CategoryService;
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

    @GetMapping()
    public String listCategory(Model model) {

        List<CategoryDto> list = categoryService.getCategoryList();
        model.addAttribute("list", list);

        return "admin/category/list";
    }


    @PostMapping("/add")
    public String addCategory(String categoryName) {

        categoryService.addCategory(categoryName);

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(CategoryDto categoryDto) {

        categoryService.deleteCategory(categoryDto.getId());

        return "redirect:/admin/categories";
    }

    @PostMapping("/update")
    public String updateCategory(CategoryDto categoryDto) {

        categoryService.updateCategory(categoryDto);

        return "redirect:/admin/categories";
    }

}
