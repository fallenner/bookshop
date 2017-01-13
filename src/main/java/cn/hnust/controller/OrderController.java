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

import cn.hnust.domain.Order;
import cn.hnust.service.OrderService;
import cn.hnust.service.OrderitemService;
import cn.hnust.util.Pager;
import cn.hnust.util.SecurityUtil;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderitemService orderitemService;
	private String showPath="/resource/showImg?path=";
	@RequestMapping("/list")
	public String list(Order order,HttpServletRequest request,HttpServletResponse response,ModelMap m){
		String orderBy=" state ";
		Pager orders=orderService.query(order,orderBy,"",getPageNum(request),getPageSize(request));
		m.put("orders", orders);
		return "orders/list";
	}
	@RequestMapping("/admin/list")
	public String adminList(Order order,HttpServletRequest request,HttpServletResponse response,ModelMap m){
		if(SecurityUtil.getCurrentRole()!=null &&"admin".equals(SecurityUtil.getCurrentRole().getName())){
			return "admin/orders/list";
		}else{
			m.put("errorMsg","权限不足，只有管理员才能访问此页面");
			return "common/500";
		}
	}
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(Order order, HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result=new HashMap<String, Object>();
		try{
			orderService.save(order);
			msg.setSuccessMsg("下单成功!");
			result.put("msg", msg);
			return result;
		}catch(Exception e){
			msg.setErrorMsg("下单失败!");
			result.put("msg", msg);
			return result;
		}
	}
	@RequestMapping("/get")
	public String get(HttpServletRequest request,HttpServletResponse response,ModelMap m){
		String id=get(request, "id");
		String showImgUrl=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+showPath;
		m.put("order", orderService.get(id));
		m.put("showImgUrl", showImgUrl);
		return "orders/detail";
		
	}
	@RequestMapping("/to_edit")
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap m){
		if(SecurityUtil.getCurrentRole()!=null &&"admin".equals(SecurityUtil.getCurrentRole().getName())){
			String oid=get(request, "oid");
			String showImgUrl=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+showPath;
			m.put("order", orderService.get(oid));
			m.put("showImgUrl", showImgUrl);
			return "admin/orders/edit";
		}else{
			m.put("errorMsg","权限不足，只有管理员才能访问此页面");
			return "common/500";
		}
		
	}
	@RequestMapping("/remove")
	@ResponseBody
	public Map<String,Object> remove(HttpServletRequest request,HttpServletResponse response,ModelMap m){
		Map<String,Object> resultMap=new HashMap<String, Object>();
			String oid=get(request, "oid");
			try{
				orderService.remove(oid);
				msg.setSuccessMsg("删除成功");
			}catch(Exception e){
				msg.setErrorMsg("删除成功");
			}
			resultMap.put("msg", msg);
			return resultMap;
	}
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String,Object> edit(Order order,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> resultMap=new HashMap<String, Object>();
		try{
			orderService.edit(order);
			if(StringUtils.isNotEmpty(order.getState())){
				if(order.getState().equals("1")){
					msg.setSuccessMsg("付款成功");
				}else if(order.getState().equals("2")){
					msg.setSuccessMsg("发货成功");
				}else if(order.getState().equals("4")){
				msg.setSuccessMsg("确认收货成功！欢迎继续购物");
				}else if(order.getState().equals("3")){
					msg.setSuccessMsg("取消成功,我们将继续努力提升服务质量");
				}
				
			}
		}catch(Exception e){
			msg.setErrorMsg("系统繁忙，请稍后再试");
		}
			resultMap.put("msg", msg);
			return resultMap;
	}
	@RequestMapping("/getPager")
	@ResponseBody
	public Pager getPager(Order order,HttpServletRequest request,HttpServletResponse response){
		
		String type=get(request, "type");
		if(StringUtils.isEmpty(type)){
			type="0";
		}
		String entitySQL="";
		if("1".equals(type)){
			entitySQL=" state !='1' and state !='0'";
		}else if("0".equals(type)){
			order.setState("1");
		}
		
		return orderService.query(order,null,entitySQL,getPageNum(request),getPageSize(request));
	}
}
