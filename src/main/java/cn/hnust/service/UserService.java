package cn.hnust.service;

import cn.hnust.domain.User;
import cn.hnust.util.Pager;
import cn.hnust.util.UserException;

public interface UserService {

	void login(String username, String password)throws UserException;
	User findByUserName(String username);
	Pager query(User user, String entitySQL, String orderBy, int pageNum,
			int pageSize);
	void add(User user)throws Exception;
	void activeAccount(String username, String code)throws Exception ;
	void grantAdmin(String uid);
	void removeUserFromAdmin(String uid);

}
