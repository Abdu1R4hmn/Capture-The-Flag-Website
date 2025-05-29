package com.example.springboot.Category;

import com.example.springboot.exceptions.categoryException.CategoryAlreadyExistsException;
import com.example.springboot.exceptions.categoryException.CategoryDeleteConstraintException;
import com.example.springboot.exceptions.categoryException.CategoryNotFoundException;
import com.example.springboot.exceptions.categoryException.CategoryServiceLogicException;
import com.example.springboot.responses.ApiResponseDto;
import com.example.springboot.responses.ApiResponseStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

//    Constructor
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


//    APIs

    //    GET ALL
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<List<CategoryDTO>>> getCategoryAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategoryAll(page,size);
    }

    //    GET all with challenge counts for admin
    @GetMapping("/get/all/with-challenge-counts")
    public ResponseEntity<ApiResponseDto<List<CategoryChallengeCountDTO>>> getAllWithChallengeCounts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws CategoryServiceLogicException {
        return categoryService.getCategoryChallengeCounts(size, page);
    }

    //    GET by Id
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponseDto<CategoryDTO>> getCategory(@PathVariable("id") long id) throws CategoryNotFoundException {
        return categoryService.getCategory(id);
    }

    //    GET by Type
    @GetMapping("/get/type/{type}")
    public ResponseEntity<ApiResponseDto<CategoryDTO>> getCategoryType(@PathVariable("type") String type) throws CategoryNotFoundException {
        return categoryService.getCategoryType(type);
    }

    //    POST category
    @PostMapping("/post")
    public ResponseEntity<ApiResponseDto<?>> postCategory(@Valid @RequestBody CategoryDTO categoryDto) throws CategoryAlreadyExistsException {
        return categoryService.postCategory(categoryDto);
    }

    //    Edit category
    @PatchMapping("/put/{id}")
    public ResponseEntity<ApiResponseDto<?>> putCategory(@Valid @RequestBody CategoryDTO categoryDto, @PathVariable("id") long id) throws CategoryNotFoundException, CategoryAlreadyExistsException {
        return categoryService.putCategory(categoryDto, id);
    }

    //    Delete category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteCategory(@PathVariable("id") long id) throws CategoryNotFoundException {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/total")
    public long totalCategory() {
        return categoryService.totalCategory();
    }
}
