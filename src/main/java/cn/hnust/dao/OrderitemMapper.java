package cn.hnust.dao;

import java.util.List;

import cn.hnust.domain.Orderitem;

public interface OrderitemMapper {
	void insert(Orderitem orderitem);

	List<Orderitem> findByOid(String oid);

	void removeByoId(String oid);
}
