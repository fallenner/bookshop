package cn.hnust.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnust.dao.CategoryMapper;
import cn.hnust.domain.Category;
import cn.hnust.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public List<Category> query(Category category) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("dto",category);
		return categoryMapper.query(params);
	}

	@Override
	public Category findById(String cid) {
		return categoryMapper.findById(cid);
	}
	
}
