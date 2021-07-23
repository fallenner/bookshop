package cn.hnust.dao;

import java.util.List;
import java.util.Map;

import cn.hnust.domain.Category;

public interface CategoryMapper {

    List<Category> query(Map<String, Object> params);

    Category findById(String cid);

}
