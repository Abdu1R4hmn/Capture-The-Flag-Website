package com.example.springboot.Category;

import com.example.springboot.Exceptions.UserNotFoundException;
import com.example.springboot.User.User;
import com.example.springboot.User.UserResponseDto;
import org.hibernate.FetchNotFoundException;
import org.hibernate.engine.internal.Nullability;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }


    //   DTO
    private CategoryDTO convertToDto(Category category) {
        return new CategoryDTO(category.getType());
    }


    public CategoryDTO getCategory(long id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("User Not Found"));

        CategoryDTO categoryDTO = convertToDto(category);


        return categoryDTO;
    }

    public CategoryDTO getCategoryType(String type) {

        Category category = categoryRepo.findByType(type);

//                .orElseThrow(() -> new IllegalStateException("User Not Found"));

        CategoryDTO categoryDTO = convertToDto(category);


        return categoryDTO;
    }
}
