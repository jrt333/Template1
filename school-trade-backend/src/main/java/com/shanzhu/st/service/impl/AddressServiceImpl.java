package com.shanzhu.st.service.impl;

import com.shanzhu.st.entity.Address;
import com.shanzhu.st.mapper.AddressMapper;
import com.shanzhu.st.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    public List<Address> getAddressByUser(Long userId){
        return addressMapper.getAddressByUser(userId);
    }

    public Address getAddressById(Long id, Long userId){
        Address address = addressMapper.selectByPrimaryKey(id);
        if(userId.equals(address.getUserId())){
            return address;
        }
        return null;
    }

    public boolean addAddress(Address address){
        if(address.getDefaultFlag()){
            Address a=new Address();
            a.setDefaultFlag(false);
            a.setUserId(address.getUserId());
            //将一个用户的所有地址改为非默认地址，需要优化，sql增加判断条件default_flag=1，减少更新记录的数目
            addressMapper.updateByUserIdSelective(a);
        }else {
            //判断是否有默认地址，若无，则将当前地址设为默认地址
            List<Address> list= addressMapper.getDefaultAddress(address.getUserId());
            //可优化，改为count统计，不用返回地址数据，减少io
            if(null==list||0==list.size()){
                address.setDefaultFlag(true);
            }
        }
        return addressMapper.insert(address)==1;
    }

    public boolean updateAddress(Address address){
        if(address.getDefaultFlag()){
            //同新增地址时的逻辑
            Address a=new Address();
            a.setDefaultFlag(false);
            a.setUserId(address.getUserId());
            addressMapper.updateByUserIdSelective(a);
        }else{
            //若取消默认地址，则将第一个地址设置为默认地址
            List<Address> list= addressMapper.getAddressByUser(address.getUserId());
            for(Address a:list){
                if(a.getDefaultFlag()&& a.getId().equals(address.getId())){
                    Address a1=new Address();
                    a1.setId(list.get(0).getId());
                    a1.setDefaultFlag(true);
                    return addressMapper.updateByPrimaryKeySelective(address)==1&&
                            addressMapper.updateByPrimaryKeySelective(a1)==1;
                }
            }
        }
        return addressMapper.updateByPrimaryKeySelective(address)==1;
    }

    public boolean deleteAddress(Address address){
        return addressMapper.deleteByPrimaryKeyAndUser(address.getId(), address.getUserId())==1;
    }
}
