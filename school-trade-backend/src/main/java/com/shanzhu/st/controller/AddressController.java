package com.shanzhu.st.controller;

import com.shanzhu.st.entity.Address;
import com.shanzhu.st.enums.ErrorMsg;
import com.shanzhu.st.service.AddressService;
import com.shanzhu.st.vo.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 地址相关 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@CrossOrigin
@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 通过id查询地址
     *
     * @param shUserId 用户id
     * @param id       地址id
     * @return 地址信息
     */
    @GetMapping("/info")
    public R getAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @NotEmpty(message = "登录异常 请重新登录") String shUserId,
            @RequestParam(value = "id", required = false) Long id
    ) {
        if (null == id) {
            return R.success(addressService.getAddressByUser(Long.valueOf(shUserId)));
        } else {
            return R.success(addressService.getAddressById(id, Long.valueOf(shUserId)));
        }
    }

    /**
     * 添加地址
     *
     * @param shUserId 用户id
     * @param address  地址信息
     * @return 结果
     */
    @PostMapping("/add")
    public R addAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @NotEmpty(message = "登录异常 请重新登录") String shUserId,
            @RequestBody Address address
    ) {
        address.setUserId(Long.valueOf(shUserId));
        if (addressService.addAddress(address)) {
            return R.success(address);
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

    /**
     * 更新地址信息
     *
     * @param shUserId 用户id
     * @param address  地址信息
     * @return 结果
     */
    @PostMapping("/update")
    public R updateAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @NotEmpty(message = "登录异常 请重新登录") String shUserId,
            @RequestBody Address address
    ) {
        address.setUserId(Long.valueOf(shUserId));
        if (addressService.updateAddress(address)) {
            return R.success();
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

    /**
     * 删除地址
     *
     * @param shUserId 用户id
     * @param address  地址信息
     * @return 结果
     */
    @PostMapping("/delete")
    public R deleteAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @NotEmpty(message = "登录异常 请重新登录") String shUserId,
            @RequestBody Address address
    ) {
        address.setUserId(Long.valueOf(shUserId));
        if (addressService.deleteAddress(address)) {
            return R.success();
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

}
