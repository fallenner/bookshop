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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hnust.domain.User;
import cn.hnust.service.UserService;
import cn.hnust.util.EmailUtil;
import cn.hnust.util.Pager;
import cn.hnust.util.SecurityUtil;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String list(ModelMap m) {
        if (SecurityUtil.getCurrentRole() != null && "admin".equals(SecurityUtil.getCurrentRole().getName())) {
            return "/admin/user/list";
        } else {
            m.put("errorMsg", "权限不足，只有管理员才能访问此页面");
            return "common/500";
        }
    }

    @RequestMapping("/getPager")
    @ResponseBody
    public Pager getPager(User user, HttpServletRequest request, HttpServletResponse response) {
        if (SecurityUtil.getCurrentRole() == null || !"admin".equals(SecurityUtil.getCurrentRole().getName())) {
            return null;
        }
        if (StringUtils.isNotEmpty(user.getTruename())) {
            user.setTruename("%" + user.getTruename() + "%");
        }
        String type = get(request, "type");
        if (StringUtils.isEmpty(type)) {
            type = "common";
        }
        if (type.equals("admin")) {
            return userService.query(user, "u.uid in(SELECT ur.userid FROM user_role ur,role r WHERE r.id=ur.roleid AND r.name='admin' and u.username !='admin')", "", getPageNum(request), getPageSize(request));
        } else if (type.equals("common")) {
            return userService.query(user, "u.uid not in(SELECT ur.userid FROM user_role ur,role r WHERE r.id=ur.roleid AND r.name='admin') and u.username !='admin'", "", getPageNum(request), getPageSize(request));
        } else {
            return null;
        }

    }

    @RequestMapping("/to_regist")
    public String toRegist() {
        return "regist";
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public String regist(User user, HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        try {
            userService.add(user);
            EmailUtil.sendMail(request, user.getTruename(), user.getEmail(), user.getUsername(), user.getCode());
            msg.setSuccessMsg("注册成功");
        } catch (Exception e) {
            msg.setErrorMsg(e.getMessage());
        }
        m.put("msg", msg);
        return "registSuccess";
    }

    @RequestMapping("/validate_username")
    @ResponseBody
    public Map<String, Object> checkUserName(HttpServletRequest request) {
        String username = get(request, "username");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        User user = userService.findByUserName(username);
        if (user == null) {
            resultMap.put("flag", true);
        } else {
            resultMap.put("flag", false);
        }
        return resultMap;
    }

    @RequestMapping("/active_account")
    public String active_account(HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        String username = get(request, "username");
        String code = get(request, "code");
        if (StringUtils.isEmpty(username)) {
            msg.setErrorMsg("用户名不存在");
        }
        if (StringUtils.isEmpty(code)) {
            msg.setErrorMsg("激活码不能为空");
        }
        try {
            userService.activeAccount(username, code);
            msg.setSuccessMsg("激活成功");
        } catch (Exception e) {
            msg.setErrorMsg(e.getMessage());
        }
        m.put("msg", msg);
        return "active_success";
    }

    @RequestMapping("/admin/grant")
    @ResponseBody
    public Map<String, Object> grant(HttpServletRequest request, HttpServletResponse response) {
        String uid = get(request, "uid");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(uid)) {
            msg.setErrorMsg("设置失败");
            resultMap.put("msg", msg);
            return resultMap;
        }
        try {
            userService.grantAdmin(uid);
            msg.setSuccessMsg("设置成功");
        } catch (Exception e) {
            msg.setErrorMsg("设置失败");
            e.printStackTrace();
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    @RequestMapping("/admin/grant_remove")
    @ResponseBody
    public Map<String, Object> removeGrant(HttpServletRequest request, HttpServletResponse response) {
        String uid = get(request, "uid");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(uid)) {
            msg.setErrorMsg("取消失败");
            resultMap.put("msg", msg);
            return resultMap;
        }
        try {
            userService.removeUserFromAdmin(uid);
            msg.setSuccessMsg("取消成功");
        } catch (Exception e) {
            msg.setErrorMsg("取消失败");
            e.printStackTrace();
        }
        resultMap.put("msg", msg);
        return resultMap;
    }
}
