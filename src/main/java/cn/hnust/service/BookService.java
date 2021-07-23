package cn.hnust.service;

import org.springframework.web.multipart.MultipartFile;

import cn.hnust.domain.Book;
import cn.hnust.util.Pager;

public interface BookService {

    Pager query(Book book, int pageNum, int pageSize, String orderBy);

    Book findById(String id);

    Pager queryhotBooks();

    void save(Book book, MultipartFile multipartFile);

    void remove(String bid);

}
