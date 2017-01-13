package cn.hnust.util;

import org.apache.shiro.SecurityUtils;

import cn.hnust.domain.Role;
import cn.hnust.domain.User;

public class SecurityUtil {
	public static User getCurrentUser(){
		return (User)SecurityUtils.getSubject().getSession().getAttribute("currentUser");
	}
	public static Role getCurrentRole(){
		return (Role)SecurityUtils.getSubject().getSession().getAttribute("currentRole");
	}
}
