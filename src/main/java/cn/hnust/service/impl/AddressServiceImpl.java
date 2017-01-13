package cn.hnust.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnust.dao.AddressMapper;
import cn.hnust.domain.Address;
import cn.hnust.domain.User;
import cn.hnust.service.AddressService;
import cn.hnust.util.UUIDutil;

@Service
public class AddressServiceImpl implements AddressService{
	@Autowired
	private AddressMapper addressMapper;

	@Override
	public void save(Address address) {
		if("1".equals(address.getIsDefault())){
			addressMapper.resetAddressDefault();
		}
		if(StringUtils.isEmpty(address.getId())){
			User user=(User)SecurityUtils.getSubject().getSession().getAttribute("currentUser");
			address.setId(UUIDutil.getUUID());
			address.setUsername(user.getUsername());
			addressMapper.insert(address);
		}else {
			addressMapper.update(address);
		}
	}

	@Override
	public List<Address> queryByCurrentUser() {
		User user=(User)SecurityUtils.getSubject().getSession().getAttribute("currentUser");
		Address address=new Address();
		address.setUsername(user.getUsername());
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("dto", address);
		params.put("orderBy", "is_default desc");
		return addressMapper.query(params);
	}

	@Override
	public void remove(String id) {
		addressMapper.deleteById(id);
	}

	@Override
	public Address findById(String id) {
		return addressMapper.findById(id);
	}
}
