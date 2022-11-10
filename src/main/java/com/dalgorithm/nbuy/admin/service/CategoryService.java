package com.dalgorithm.nbuy.admin.service;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    /**
     * 카테고리 목록
     */
    List<CategoryDto> getCategoryList();

    /**
     * 카테고리 신규 추가
     */
    void addCategory(String categoryName);

    /**
     * 카테고리 수정
     */
    void updateCategory(CategoryDto categoryDto);

    /**
     * 카테고리 삭제
     */
    void deleteCategory(long id);

    /**
     * 프론트 카테고리 정보
     */
    List<CategoryDto> frontList(CategoryDto categoryDto);
}
