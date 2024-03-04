package com.proj.expensetracker.service.impl;


import com.proj.expensetracker.entity.Category;
import com.proj.expensetracker.repository.CategoryRepository;
import com.proj.expensetracker.request.CategoryRequest;
import com.proj.expensetracker.service.iface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }
}
