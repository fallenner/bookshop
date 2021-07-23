package cn.hnust.service;

import cn.hnust.domain.Order;
import cn.hnust.util.Pager;

public interface OrderService {

    void save(Order order);

    Pager query(Order order, String orderBy, String entitySQL, int pageNum, int pageSize);

    Order get(String id);

    void edit(Order order);

    void remove(String oid);

}
