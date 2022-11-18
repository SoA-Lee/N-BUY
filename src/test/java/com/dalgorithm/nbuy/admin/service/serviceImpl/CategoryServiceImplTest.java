package com.dalgorithm.nbuy.admin.service.serviceImpl;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.entity.Category;
import com.dalgorithm.nbuy.admin.repository.CategoryRepository;
import com.dalgorithm.nbuy.exception.AbstractException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("카테고리 리스트 가져오기 성공")
    void getCategoryList() {
        Category category1 = Category.builder()
                .categoryName("카테고리1")
                .build();

        Category category2 = Category.builder()
                .categoryName("카테고리2")
                .build();

        List<Category> categoryList = Arrays.asList(category1, category2);

        // given
        given(categoryRepository.findAll())
                .willReturn(categoryList);

        // when
        List<CategoryDto> testList = categoryService.getCategoryList();

        // then
        assertEquals(2, testList.size());
    }

    @Test
    @DisplayName("카테고리 추가 성공")
    void addCategory() {
        // given
        given(categoryRepository.findByCategoryName(anyString()))
                .willReturn(Optional.empty());

        // when
        categoryService.addCategory("테스트");
    }

    @Test
    @DisplayName("카테고리 추가 실패 - 이미 존재하는 카테고리")
    void addCategoryFailedByDuplicated() {
        Category category = Category.builder()
                .categoryName("카테고리테스트")
                .build();

        // given
        given(categoryRepository.findByCategoryName(anyString()))
                .willReturn(Optional.of(category));

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> categoryService.addCategory("카테고리테스트"));

        // then
        assertEquals(exception.getMessage(), "이미 존재하는 카테고리입니다.");
    }

    @Test
    @DisplayName("카테고리 업데이트 성공")
    void updateCategory() {

        Long id = 1L;

        Category category = Category.builder()
                .id(id)
                .categoryName("test")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(id)
                .categoryName("update")
                .build();

        // given
        given(categoryRepository.findById(anyLong()))
                .willReturn(Optional.of(category));

        // when
        categoryService.updateCategory(categoryDto);

        // then
        assertEquals(category.getCategoryName(), "update");
    }

    @Test
    @DisplayName("카테고리 업데이트 실패 - 이미 존재하는 카테고리")
    void updateCategoryFailedByCategoryNotFound() {
        Long id = 1L;

        CategoryDto category = CategoryDto.builder()
                .id(id)
                .categoryName("카테고리테스트")
                .build();

        // given
        given(categoryRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        AbstractException exception = assertThrows(
                AbstractException.class,
                () -> categoryService.updateCategory(category));

        // then
        assertEquals(exception.getMessage(), "존재하지 않는 카테고리입니다.");
    }
}