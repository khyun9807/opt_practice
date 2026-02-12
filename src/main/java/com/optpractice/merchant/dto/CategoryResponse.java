package com.optpractice.merchant.dto;

import com.optpractice.merchant.entity.Category;

public class CategoryResponse {

    /**
     * Category Item
     */
    public record CategoryItem(
            Long id,
            String name,
            Long parentCategoryId
    ) {
        public static CategoryResponse.CategoryItem from(Category category) {
            return new CategoryResponse.CategoryItem(
                    category.getId(),
                    category.getName(),
                    category.getParentCategory() != null ? category.getParentCategory().getId() : null
            );
        }
    }
}