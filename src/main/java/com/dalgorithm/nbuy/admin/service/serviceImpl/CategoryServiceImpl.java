package com.dalgorithm.nbuy.admin.service.serviceImpl;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import com.dalgorithm.nbuy.admin.entity.Category;
import com.dalgorithm.nbuy.admin.mapper.CategoryMapper;
import com.dalgorithm.nbuy.admin.repository.CategoryRepository;
import com.dalgorithm.nbuy.admin.service.CategoryService;
import com.dalgorithm.nbuy.exception.impl.category.AlreadyExistCategoryException;
import com.dalgorithm.nbuy.exception.impl.category.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
            throw new AlreadyExistCategoryException();
        }

        Category category = Category.builder()
                .categoryName(categoryName)
                .build();

        log.info("[category]" + categoryName + "가 추가되었습니다.");
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto) {

        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(CategoryNotFoundException::new);

        category.setCategoryName(categoryDto.getCategoryName());

        log.info("[category]" + category.getCategoryName() + "가 변경되었습니다.");
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
