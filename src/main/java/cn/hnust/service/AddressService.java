package cn.hnust.service;

import java.util.List;

import cn.hnust.domain.Address;

public interface AddressService {

    void save(Address address);

    List<Address> queryByCurrentUser();

    void remove(String id);

    Address findById(String id);

}
