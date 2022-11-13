package com.dalgorithm.nbuy.admin.service;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {
    /**
     * 카테고리 목록
     */
    @Transactional(readOnly = true)
    List<CategoryDto> getCategoryList();

    /**
     * 카테고리 신규 추가
     */

    @Transactional
    void addCategory(String categoryName);

    /**
     * 카테고리 수정
     */
    @Transactional
    void updateCategory(CategoryDto categoryDto);

    /**
     * 카테고리 삭제
     */
    @Transactional
    void deleteCategory(long id);

    /**
     * 프론트 카테고리 정보
     */
    @Transactional(readOnly = true)
    List<CategoryDto> frontList(CategoryDto categoryDto);
}
