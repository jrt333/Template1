package com.shanzhu.st.service;

import com.shanzhu.st.entity.Address;

import java.util.List;

/**
 * 地址相关 服务层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
public interface AddressService {

    /**
     * 获取一个用户的所有地址信息
     *
     * @param userId 用户id
     * @return 地址列表
     */
    List<Address> getAddressByUser(Long userId);

    /**
     * 获取单个地址的信息
     *
     * @param id     地址id
     * @param userId 用户id
     * @return 地址信息
     */
    Address getAddressById(Long id, Long userId);

    /**
     * 新增地址信息
     *
     * @param address 地址信息
     * @return 结果
     */
    boolean addAddress(Address address);

    /**
     * 修改地址信息
     *
     * @param address 地址信息
     * @return 结果
     */
    boolean updateAddress(Address address);

    /**
     * 删除地址信息
     *
     * @param address 地址信息
     * @return 结果
     */
    boolean deleteAddress(Address address);
}
