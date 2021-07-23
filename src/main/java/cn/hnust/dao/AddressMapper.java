package cn.hnust.dao;

import java.util.List;
import java.util.Map;

import cn.hnust.domain.Address;

public interface AddressMapper {
    void insert(Address address);

    List<Address> query(Map<String, Object> params);

    Address findById(String id);

    void update(Address address);

    void deleteById(String id);

    void resetAddressDefault();
}
