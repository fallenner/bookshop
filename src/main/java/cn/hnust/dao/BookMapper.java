package cn.hnust.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.hnust.domain.Book;

public interface BookMapper {

    public List<Book> query(Map<String, Object> params, RowBounds rowBounds);

    public Book findById(String id);

    public int queryCount(Map<String, Object> params);

    public void insert(Book book);

    public void update(Book book);

    public void deleteById(String bid);
}
