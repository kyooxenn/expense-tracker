package com.proj.expensetracker.service.iface;

import com.proj.expensetracker.entity.Category;
import com.proj.expensetracker.request.CategoryRequest;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoryList();

}
