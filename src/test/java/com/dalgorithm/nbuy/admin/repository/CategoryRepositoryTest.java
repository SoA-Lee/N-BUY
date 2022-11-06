package com.dalgorithm.nbuy.admin.repository;

import com.dalgorithm.nbuy.admin.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void saveTest() {
        Category category = Category.builder()
                .categoryName("의류")
                .build();

        Category savedCategory = categoryRepository.save(category);

        assertEquals("의류", savedCategory.getCategoryName());
    }
}