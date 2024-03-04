package com.proj.expensetracker.controller;


import com.proj.expensetracker.entity.Category;
import com.proj.expensetracker.request.CategoryRequest;
import com.proj.expensetracker.service.iface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategoryList(CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }


}
