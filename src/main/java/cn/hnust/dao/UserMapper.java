package cn.hnust.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.hnust.domain.User;

public interface UserMapper {

	User findByUserName(String username);

	List<User> query(Map<String, Object> params, RowBounds rowBounds);

	Integer queryCount(Map<String, Object> params);

	void insert(User user);

	void update(User user);
	
}
