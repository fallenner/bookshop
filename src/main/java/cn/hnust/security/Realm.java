package cn.hnust.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hnust.domain.User;
import cn.hnust.service.UserService;

public class Realm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

	public void setUserService(UserService userService){
		this.userService=userService;
	}
	 //授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		return null;
	}
	 //登录信息和用户验证信息验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token=(UsernamePasswordToken)authcToken;
		String username=token.getUsername();
		if(StringUtils.isEmpty(username)){
			throw new AccountException("用户名不能为空");
		}
		User user=userService.findByUserName(username);
		if(user!=null){
			return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),getName());
		}
		return null;
	}

}
