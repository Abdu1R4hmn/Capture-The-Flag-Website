package com.example.springboot.Category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

//    Variables
    private CategoryRepo categoryRepo;

//    Constructor
    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

//   DTO
    private CategoryDTO convertToDto(Category category) {
        return new CategoryDTO(category.getType());
    }

// CRUD

    // Get Category by id.
    public List<CategoryDTO> getCategoryAll() {

        List<CategoryDTO> categories = categoryRepo.findAll().stream().map(this::convertToDto).toList();

        return categories;
    }

    // Get Category by id.
    public CategoryDTO getCategory(long id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("User Not Found"));

        CategoryDTO categoryDTO = convertToDto(category);


        return categoryDTO;
    }

    // Get Category by Type.
    public CategoryDTO getCategoryType(String type) {

        Category category = categoryRepo.findByType(type);

//                .orElseThrow(() -> new IllegalStateException("User Not Found"));

        CategoryDTO categoryDTO = convertToDto(category);


        return categoryDTO;
    }

    // Post Category.
    public void postCategory(CategoryDTO categoryDto) {

        if (categoryRepo.findByType(categoryDto.getType()) != null) {
            throw new IllegalStateException("Category Already Exists!");
        }

        Category category = new Category(categoryDto.getType());

        categoryRepo.save(category);
    }

    // Put Category
    public void putCategory(CategoryDTO categoryDto, long id) {

        Category category = categoryRepo.findById(id).orElseThrow(()-> new IllegalStateException("Category Not Found!"));

        category.setType(categoryDto.getType());

        categoryRepo.save(category);
    }

    // Delete Category
    public void deleteCategory(long id) {

        Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalStateException("Category not found"));

        categoryRepo.delete(category);
    }

    //      GET TOTAL NUMBER OF USERS
    public long totalCategory() {
        return categoryRepo.findAll().stream().count();
    }

}
