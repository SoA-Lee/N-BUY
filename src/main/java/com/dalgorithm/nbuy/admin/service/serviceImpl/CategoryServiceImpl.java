package com.dalgorithm.nbuy.admin.service.serviceImpl;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.entity.Category;
import com.dalgorithm.nbuy.admin.exception.CategoryErrorCode;
import com.dalgorithm.nbuy.admin.exception.CategoryException;
import com.dalgorithm.nbuy.admin.mapper.CategoryMapper;
import com.dalgorithm.nbuy.admin.repository.CategoryRepository;
import com.dalgorithm.nbuy.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return CategoryDto.fromEntity(categoryList);
    }

    @Override
    public void addCategory(String categoryName) {

        if (categoryRepository.findByCategoryName(categoryName).isPresent()){
            throw new CategoryException(CategoryErrorCode.CATEGORY_ALREADY_EXIST);
        }

        Category category = Category.builder()
                .categoryName(categoryName)
                .build();

        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto) {

        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        category.setCategoryName(categoryDto.getCategoryName());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> frontList(CategoryDto categoryDto) {
        return categoryMapper.select(categoryDto);
    }
}
