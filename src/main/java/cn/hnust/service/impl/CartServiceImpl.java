package cn.hnust.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnust.dao.CartMapper;
import cn.hnust.domain.Cart;
import cn.hnust.domain.User;
import cn.hnust.service.BookService;
import cn.hnust.service.CartService;
import cn.hnust.service.UserService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private BookService bookservice;
	@Autowired
	private UserService userservice;
	@Override
	public Map<String, Object> statistics(String userId) {
		return cartMapper.statistics(userId);
	}
	@Override
	public void save(Cart cart) {
		if(StringUtils.isEmpty(cart.getId())){
			User user=(User)SecurityUtils.getSubject().getSession().getAttribute("currentUser");
			cart.setUid(user.getUsername());
			cart.setId(UUID.randomUUID().toString().replace("-", ""));
			cart.setCount(1);
			cartMapper.insert(cart);
		}else{
			cartMapper.update(cart);
		}
	}
	@Override
	public List<Cart> query(Cart cart, String entitySQL, String order) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("orderBy", order);
		params.put("mapper", cart);
		params.put("entitySQL", entitySQL);
		List<Cart> carts= cartMapper.query(params);
		for(Cart dto:carts){
			dto.setBook(bookservice.findById(dto.getBid()));
			dto.setCartUser(userservice.findByUserName(dto.getUid()));
		}
		return carts;
	}
	@Override
	public void remove(String id) {
		cartMapper.deleteById(id);
	}
	@Override
	public void removeByUId(String uid) {
		cartMapper.removeByUId(uid);
	}

}
