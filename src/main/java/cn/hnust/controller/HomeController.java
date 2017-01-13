package cn.hnust.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.hnust.domain.Book;
import cn.hnust.domain.Role;
import cn.hnust.service.BookService;
import cn.hnust.service.UserService;
import cn.hnust.util.SecurityUtil;
import cn.hnust.util.UserException;

@Controller
public class HomeController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	private String showPath="/resource/showImg?path=";
	
	@RequestMapping(value = "/")
    public String index() throws Exception {
		Role role=SecurityUtil.getCurrentRole();
		if(role !=null && "admin".equals(role.getName())){
			return "admin/index";
		}
        return "redirect:/index";
    }
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String loginPost(HttpServletRequest request,ModelMap m){
		String username=get(request, "username");
		String password=get(request, "password");
		String validate=get(request, "j_captcha");
		String sValidate=request.getSession().getAttribute("validate").toString();
		if(!sValidate.toLowerCase().equals(validate.toLowerCase())){
			m.addAttribute("message", "验证码不正确!");
			return "login";
		}
		try{
			userService.login(username,password);
			Role role=SecurityUtil.getCurrentRole();
			if(role !=null &&"admin".equals(role.getName())){
				return "admin/index";
			}
		}catch (UnknownAccountException uae) {
            m.addAttribute("message", "账号不存在!");
            return "login";
        } catch (IncorrectCredentialsException ice) {
            m.addAttribute("message", "密码错误!");
            return "login";
        } catch (LockedAccountException lae) {
            m.addAttribute("message", "账号被锁定!");
            return "login";
        } catch (UserException userexception) {
            m.addAttribute("message", userexception.getMessage());
            return "login";
        }catch (Exception e) {
            m.addAttribute("message", "未知错误,请联系管理员.");
            return "login";
        }
		return "redirect:/index";  
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap m){
        if (SecurityUtils.getSubject().isAuthenticated()) {
        	Role role=SecurityUtil.getCurrentRole();
    		if("admin".equals(role.getName())){
    			return "admin/index";
    		}
            return "redirect:/index";
        }
        //默认赋值message,避免freemarker尝试从session取值,造成异常
        m.addAttribute("message", "");
        return "login";
    }
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request,ModelMap m){
		Subject subject=SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/login";
	}
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,ModelMap m){
		String showImgUrl=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+showPath;
		Role role=SecurityUtil.getCurrentRole();
		if(role !=null){
			if("admin".equals(role.getName())){
				return "admin/index";
			}
		}
		m.put("hotBooks", bookService.queryhotBooks());
		Book book=new Book();
		book.setRecommend(1);
		m.put("recommendbooks", bookService.query(book, getPageNum(request), getPageSize(request), " sellcount desc"));
		m.put("showImgUrl", showImgUrl);
		return "index";
	}
	@RequestMapping("/unauth")
	public String unauth(ModelMap m){
		if(!SecurityUtils.getSubject().isAuthenticated()){
			return "login";
		}
		return "login";
	}
}
