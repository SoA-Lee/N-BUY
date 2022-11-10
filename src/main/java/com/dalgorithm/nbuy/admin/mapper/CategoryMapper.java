package com.dalgorithm.nbuy.admin.mapper;

import com.dalgorithm.nbuy.admin.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> select(CategoryDto categoryDto);

}
