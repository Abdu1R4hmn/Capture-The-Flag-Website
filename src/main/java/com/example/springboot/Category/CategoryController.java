package com.example.springboot.Category;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.Attribute;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

//    Variables
    private CategoryService categoryService;

//    Constructor
    public CategoryController(CategoryService categoryService) {
        this.categoryService =  categoryService;
    }

//    CRUD

    //    GET category ALL.
    @GetMapping("/get/all")
    public List<CategoryDTO> getCategoryAll(){
        return categoryService.getCategoryAll();
    }

    //    GET category by id.
    @GetMapping("/get/{id}")
    public CategoryDTO getCategory(@PathVariable("id")long id){
        return categoryService.getCategory(id);
    }

    //    GET category by id.
    @GetMapping("/get/type/{type}")
    public CategoryDTO getCategoryType(@PathVariable("type")String type){
        return categoryService.getCategoryType(type);
    }

    //    Post category.
    @PostMapping("/post")
    public void postCategory(@RequestBody CategoryDTO categoryDto){
        categoryService.postCategory(categoryDto);
    }

    //    Edit category
    @PatchMapping("/put/{id}")
    public void putCategory(@RequestBody CategoryDTO categoryDto,@PathVariable("id") long id){
        categoryService.putCategory(categoryDto,id);
    }

    //    Delete category
    @DeleteMapping("/delete/{id}")
    public void deleteCategory (@PathVariable("id") long id){
        categoryService.deleteCategory(id);
    }

    //    GET TOTAL NUMBER OF USERS
    @GetMapping(path = "total")
    public long totalCategory() {
        return categoryService.totalCategory();
    }
}
