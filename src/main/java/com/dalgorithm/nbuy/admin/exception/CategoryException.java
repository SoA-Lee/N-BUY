package com.dalgorithm.nbuy.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryException extends RuntimeException {

    private CategoryErrorCode categoryErrorCode;
    private String errorMessage;

    public CategoryException(CategoryErrorCode categoryErrorCode) {
        this.categoryErrorCode = categoryErrorCode;
        this.errorMessage = categoryErrorCode.getDescription();
    }
}
