package cn.hnust.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.hnust.domain.Order;

public interface OrderMapper {
	void insert(Order order);

	List<Order> query(Map<String, Object> params, RowBounds rowBounds);

	Integer queryCount(Map<String, Object> params);

	Order findById(String oid);

	void update(Order order);

	void remove(String oid);
}
