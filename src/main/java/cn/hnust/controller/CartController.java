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

import cn.hnust.domain.Cart;
import cn.hnust.domain.User;
import cn.hnust.service.AddressService;
import cn.hnust.service.CartService;
import cn.hnust.util.SecurityUtil;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    private String showPath = "/resource/showImg?path=";

    @RequestMapping("/list")
    public String list(Cart cart, HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        String showImgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + showPath;
        m.put("carts", cartService.query(cart, "", "create_time desc"));
        m.put("addresses", addressService.queryByCurrentUser());
        m.put("showImgUrl", showImgUrl);
        return "cart";
    }

    @RequestMapping("/statistics")
    @ResponseBody
    public Map<String, Object> statistics(HttpServletRequest request, HttpServletResponse response) {
        User user = SecurityUtil.getCurrentUser();
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("status", false);
            result.put("msg", "用户不存在");
        }
        Map<String, Object> statisticResult = cartService.statistics(user.getUsername());
        result.put("statistic", statisticResult);
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(Cart cart, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            cartService.save(cart);
            msg.setSuccessMsg("添加成功");
        } catch (Exception e) {
            msg.setErrorMsg("添加失败");
        }
        result.put("msg", msg);
        return result;
    }

    @RequestMapping("/remove")
    @ResponseBody
    public Map<String, Object> remove(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String id = get(request, "id");
        if (StringUtils.isEmpty(id)) {
            msg.setErrorMsg("id不能为空");
            result.put("msg", msg);
            return result;
        }
        try {
            cartService.remove(id);
            msg.setSuccessMsg("删除成功");
        } catch (Exception e) {
            msg.setErrorMsg("删除失败");
            return result;
        }
        result.put("msg", msg);
        return result;
    }
}
