package cn.hnust.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hnust.domain.Category;
import cn.hnust.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController{
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<Category> list(Category category,HttpServletRequest request,ModelMap m){
		return categoryService.query(category);
	}
}
