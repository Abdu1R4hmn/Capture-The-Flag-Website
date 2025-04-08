package com.example.springboot.Category;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.Attribute;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private CategoryRepo categoryRepo;
    private CategoryService categoryService;

    public CategoryController(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    //    GET id
    @GetMapping("/get/{id}")
    public CategoryDTO getCategory(@PathParam("id")long id){
        return categoryService.getCategory(id);
    }

    //    GET type
    @GetMapping("/get/{type}")
    public CategoryDTO getCategoryType(@PathParam("type")String type){
        return categoryService.getCategoryType(type);
    }

//    //    Post
//    @PostMapping("/post")
//    public CategoryDTO postCategory(){
//
//    }

//    Edit

//    Delete

}
