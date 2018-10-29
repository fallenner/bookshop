package cn.hnust.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnust.dao.RoleMapper;
import cn.hnust.dao.UserMapper;
import cn.hnust.domain.Role;
import cn.hnust.domain.User;
import cn.hnust.service.UserService;
import cn.hnust.util.CryptUtil;
import cn.hnust.util.Digests;
import cn.hnust.util.Encodes;
import cn.hnust.util.Pager;
import cn.hnust.util.UUIDutil;
import cn.hnust.util.UserException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void login(String username, String password) throws UserException {
        Subject currentUser = SecurityUtils.getSubject();
        User user = findByUserName(username);
        if (user != null) {
            password = CryptUtil.cryptPwd(password, user.getSalt());
        } else {
            throw new UserException("用户名或密码错误");
        }
        if (user.getState() == 0) {
            throw new UserException("用户未激活！");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        currentUser.login(token);
        currentUser.getSession().setAttribute("currentUser", user);
        List<Role> roles = roleMapper.findByusername(username);
        currentUser.getSession().setAttribute("currentRole", roles.isEmpty() == true ? null : roles.get(0));
    }

    @Override
    public User findByUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return userMapper.findByUserName(username);
    }

    @Override
    public Pager query(User user, String entitySQL, String orderBy,
                       int pageNum, int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dto", user);
        params.put("entitySQL", entitySQL);
        params.put("orderBy", orderBy);
        RowBounds rowBounds = new RowBounds((pageNum - 1) * pageSize, pageSize);
        List<User> list = userMapper.query(params, rowBounds);
        Integer count = userMapper.queryCount(params);
        Pager pager = new Pager(pageNum, pageSize);
        pager.setRecords(count);
        pager.setRows(list);
        return pager;
    }

    @Override
    public void add(User user) throws Exception {
        User oldUser = userMapper.findByUserName(user.getUsername());
        if (oldUser == null) {
            user.setUid(UUIDutil.getUUID());
            user.setCode(UUIDutil.getUUID());
            user.setSalt(Encodes.encodeHex(Digests.generateSalt(8)));
            user.setPassword(CryptUtil.cryptPwd(user.getPassword(), user.getSalt()));
            userMapper.insert(user);
        } else {
            throw new Exception("用户名已存在");
        }
    }

    @Override
    public void activeAccount(String username, String code) throws Exception {
        User user = userMapper.findByUserName(username);
        if (user == null) {
            throw new Exception("用户不存在");
        }
        if (user.getState() == 1) {
            throw new Exception("用户已激活，请直接登录");
        }
        if (!code.equals(user.getCode())) {
            throw new Exception("验证码不正确");
        }
        user.setState(1);
        userMapper.update(user);
    }

    @Override
    public void grantAdmin(String uid) {
        Role role = roleMapper.findByRoleName("admin");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", role.getId());
        params.put("userId", uid);
        roleMapper.addUser(params);
    }

    @Override
    public void removeUserFromAdmin(String uid) {
        roleMapper.removeUserFromAdmin(uid);
    }

}
