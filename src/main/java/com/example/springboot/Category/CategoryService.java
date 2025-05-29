package com.example.springboot.Category;

import com.example.springboot.exceptions.categoryException.CategoryAlreadyExistsException;
import com.example.springboot.exceptions.categoryException.CategoryDeleteConstraintException;
import com.example.springboot.exceptions.categoryException.CategoryNotFoundException;
import com.example.springboot.exceptions.categoryException.CategoryServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    private CategoryDTO convertToDto(Category category) {
        return new CategoryDTO(category.getId(), category.getType());
    }

    private Category convertToEntity(CategoryDTO categoryDto) {
        return new Category(categoryDto.getType());
    }

// APIs

    // Get all with challenge count for admin
    public ResponseEntity<ApiResponseDto<List<CategoryChallengeCountDTO>>> getCategoryChallengeCounts(int size, int page) throws CategoryServiceLogicException {
        try {
            int offset = page * size;
            List<CategoryChallengeCountDTO> results = categoryRepo.fetchCategoryChallengeCountsPaged(offset, size);
            int total = categoryRepo.countAllCategories();
            int totalPages = (int) Math.ceil((double) total / size);

            return ResponseEntity.ok(new ApiResponseDto<>(
                    ApiResponseStatus.SUCCESS.name(),
                    results,
                    totalPages
            ));
        } catch (Exception e) {
            throw new CategoryServiceLogicException("Failed to fetch categories with challenge counts");
        }
    }

    public ResponseEntity<ApiResponseDto<List<CategoryDTO>>> getCategoryAll(int page, int size) {

        Page<Category> categoriesPage = categoryRepo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

        List<CategoryDTO> categoryDto = categoriesPage.getContent().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), categoryDto,categoriesPage.getTotalPages()));
    }

    public ResponseEntity<ApiResponseDto<CategoryDTO>> getCategory(long id) throws CategoryNotFoundException {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), convertToDto(category)));
    }

    public ResponseEntity<ApiResponseDto<CategoryDTO>> getCategoryType(String type) throws CategoryNotFoundException {
        Category category = categoryRepo.findByType(type);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with type: " + type);
        }
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), convertToDto(category)));
    }

    public ResponseEntity<ApiResponseDto<?>> postCategory(CategoryDTO categoryDto) throws CategoryAlreadyExistsException {
        if (categoryRepo.findByType(categoryDto.getType()) != null) {
            throw new CategoryAlreadyExistsException("Category already exists with type: " + categoryDto.getType());
        }
        categoryRepo.save(new Category(categoryDto.getType()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Category created successfully!"));
    }

    public ResponseEntity<ApiResponseDto<?>> putCategory(CategoryDTO categoryDto, long id) throws CategoryNotFoundException, CategoryAlreadyExistsException {

        if (categoryRepo.findByType(categoryDto.getType()) != null) {
            throw new CategoryAlreadyExistsException("Category already exists with type: " + categoryDto.getType());
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        category.setType(categoryDto.getType());
        categoryRepo.save(category);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Category updated successfully!"));
    }

    public ResponseEntity<ApiResponseDto<?>> deleteCategory(long id) throws CategoryNotFoundException, CategoryDeleteConstraintException {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        if (category.getChallenge() != null && !category.getChallenge().isEmpty()) {
            throw new CategoryDeleteConstraintException("Cannot delete category with associated challenges.");
        }
        categoryRepo.delete(category);
        return ResponseEntity.ok(new ApiResponseDto<>(ApiResponseStatus.SUCCESS.name(), "Category deleted successfully!"));
    }

    public long totalCategory() {
        return categoryRepo.findAll().size();
    }
}
