package cn.hnust.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.hnust.dao.BookMapper;
import cn.hnust.domain.Book;
import cn.hnust.service.BookService;
import cn.hnust.service.CategoryService;
import cn.hnust.util.Pager;
import cn.hnust.util.ResourceUtil;
import cn.hnust.util.UUIDutil;
import cn.hnust.util.VFSUtil;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookMapper bookMapper;
	@Autowired
	private CategoryService categoryService;
	@Override
	public Pager query(Book book,int pageNum,int pageSize,String orderBy) {
		Map<String,Object> params=new HashMap<String, Object>();
		Pager pager=new Pager(pageNum, pageSize);
		params.put("dto", book);
		params.put("orderBy", orderBy);
		RowBounds rowBounds=new RowBounds((pageNum-1)*pageSize, pageSize);
		List<Book> list= bookMapper.query(params,rowBounds);
		for(Book dto:list){
			dto.setCname(categoryService.findById(dto.getCid()).getCname());
		}
		int count=bookMapper.queryCount(params);
		pager.setRows(list);
		pager.setRecords(count);
		return pager;
	}
	@Override
	public Book findById(String id) {
		Book book= bookMapper.findById(id);
		book.setCname(categoryService.findById(book.getCid()).getCname());
		return book;
	}
	@Override
	public Pager queryhotBooks() {
		Map<String,Object> params=new HashMap<String, Object>();
		Pager pager=new Pager(1,5);
		params.put("orderBy", " sellcount DESC");
		RowBounds rowBounds=new RowBounds(0,5);
		pager.setRows(bookMapper.query(params,rowBounds));
		return pager;
	}
	@Override
	public void save(Book book, MultipartFile multipartFile) {
		if(StringUtils.isEmpty(book.getBid())){
			book.setBid(UUIDutil.getUUID());
			if(multipartFile !=null &&!multipartFile.isEmpty()){
				book.setImage(ResourceUtil.upload(multipartFile));
			}
			bookMapper.insert(book);
		}else{
			if(multipartFile !=null &&!multipartFile.isEmpty()){
				VFSUtil.removeFile(book.getImage());
				book.setImage(ResourceUtil.upload(multipartFile));
			}
			bookMapper.update(book);
		}
	}
	@Override
	public void remove(String bid) {
		Book book=bookMapper.findById(bid);
		if(book !=null && StringUtils.isNotEmpty(book.getImage())){
			VFSUtil.removeFile(book.getImage());
		}
		bookMapper.deleteById(bid);
	}

}
