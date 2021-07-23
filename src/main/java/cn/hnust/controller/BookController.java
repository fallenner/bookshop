package cn.hnust.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.hnust.domain.Book;
import cn.hnust.domain.Category;
import cn.hnust.service.BookService;
import cn.hnust.service.CategoryService;
import cn.hnust.util.Pager;
import cn.hnust.util.SecurityUtil;

@Controller
@RequestMapping("/book")
public class BookController extends BaseController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;

    private String showPath = "/resource/showImg?path=";

    @RequestMapping("/admin/list")
    public String adminList(ModelMap m) {
        if (SecurityUtil.getCurrentRole() != null && "admin".equals(SecurityUtil.getCurrentRole().getName())) {
            return "admin/book/list";
        } else {
            m.put("errorMsg", "权限不足，只有管理员才能访问此页面");
            return "common/500";
        }
    }

    @RequestMapping("/list")
    public String list(Book book, HttpServletRequest request, ModelMap m) throws Exception {
        int pageNum = getPageNum(request);
        int pageSize = getPageSize(request);
        String showImgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + showPath;
        Category category = null;
        if (StringUtils.isNotEmpty(book.getCid())) {
            category = categoryService.findById(book.getCid());
        }
        Pager books = bookService.query(book, pageNum, pageSize, " sellcount DESC");
        m.put("books", books);
        m.put("category", category);
        m.put("showImgUrl", showImgUrl);
        return "category";
    }

    @RequestMapping("/get")
    public String get(HttpServletRequest request, ModelMap m) {
        String showImgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + showPath;
        String id = get(request, "id");
        String cid = get(request, "cid");
        if (StringUtils.isEmpty(id)) {
            m.put("errorMsg", "id不能为空");
            return "/common/500";
        }
        Book book = bookService.findById(id);
        Book dto = new Book();
        dto.setCid(cid);
        m.put("categoryBooks", bookService.query(dto, getPageNum(request), getPageSize(request), " sellcount DESC"));
        m.put("book", book);
        m.put("hotBooks", bookService.queryhotBooks());
        m.put("showImgUrl", showImgUrl);
        return "single-book";
    }

    @RequestMapping("/getPager")
    @ResponseBody
    public Pager getPager(Book book, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotEmpty(book.getBname())) {
            book.setBname("%" + book.getBname() + "%");
        }
        return bookService.query(book, getPageNum(request), getPageSize(request), " sellcount DESC");
    }

    @RequestMapping("admin/to_add")
    public String toAdd(ModelMap m) {
        if (SecurityUtil.getCurrentRole() != null && "admin".equals(SecurityUtil.getCurrentRole().getName())) {
            m.put("categories", categoryService.query(null));
            m.put("book", new Book());//防止前台freemark报错
            return "admin/book/add";
        } else {
            m.put("errorMsg", "权限不足，只有管理员才能访问此页面");
            return "common/500";
        }
    }

    @RequestMapping("admin/save")
    public String save(Book book, HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        if (SecurityUtil.getCurrentRole() != null && "admin".equals(SecurityUtil.getCurrentRole().getName())) {
            MultipartFile multipartFile = null;
            MultipartRequest multipartRequest = null;
            try {
                multipartRequest = (MultipartRequest) request;
                multipartFile = multipartRequest.getFile("imgFile");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bookService.save(book, multipartFile);
                msg.setSuccessMsg("保存成功");
            } catch (Exception e) {
                msg.setErrorMsg("保存失败");
            }
            m.put("msg", msg);
            return "admin/common/jump";
        } else {
            m.put("errorMsg", "权限不足，只有管理员才能访问此页面");
            return "common/500";
        }
    }

    @RequestMapping("/admin/to_edit")
    public String toEdit(HttpServletRequest request, ModelMap m) {
        if (SecurityUtil.getCurrentRole() != null && "admin".equals(SecurityUtil.getCurrentRole().getName())) {
            String showImgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + showPath;
            String bid = get(request, "bid");
            m.put("categories", categoryService.query(null));
            m.put("book", bookService.findById(bid));
            m.put("showImgUrl", showImgUrl);
            return "admin/book/add";
        } else {
            m.put("errorMsg", "权限不足，只有管理员才能访问此页面");
            return "common/500";
        }
    }

    @RequestMapping("/admin/remove")
    @ResponseBody
    public Map<String, Object> remove(HttpServletRequest request, HttpServletResponse response) {
        String bid = get(request, "bid");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            bookService.remove(bid);
            msg.setSuccessMsg("删除成功");
        } catch (Exception e) {
            msg.setErrorMsg("删除失败");
        }
        resultMap.put("msg", msg);
        return resultMap;
    }
}
