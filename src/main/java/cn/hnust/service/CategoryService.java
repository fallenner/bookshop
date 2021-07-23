package cn.hnust.service;

import java.util.List;

import cn.hnust.domain.Category;

public interface CategoryService {

    List<Category> query(Category category);

    Category findById(String cid);

}
