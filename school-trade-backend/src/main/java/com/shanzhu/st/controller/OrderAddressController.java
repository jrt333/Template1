package com.shanzhu.st.controller;

import com.shanzhu.st.entity.OrderAddress;
import com.shanzhu.st.enums.ErrorMsg;
import com.shanzhu.st.service.OrderAddressService;
import com.shanzhu.st.vo.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 订单地址 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@CrossOrigin
@RestController
@RequestMapping("/order-address")
public class OrderAddressController {

    @Resource
    private OrderAddressService orderAddressService;

    /**
     * 添加订单地址信息
     *
     * @param orderAddress 地址信息科
     * @return 结果
     */
    @PostMapping("/add")
    public R addOrderAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @RequestBody OrderAddress orderAddress
    ) {
        return R.success(orderAddressService.addOrderAddress(orderAddress));
    }

    /**
     * 更新订单地址信息
     *
     * @param orderAddress 订单地址信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public R updateOrderAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @RequestBody OrderAddress orderAddress
    ) {
        if (orderAddressService.updateOrderAddress(orderAddress)) {
            return R.success(orderAddress);
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

    /**
     * 获取订单地址信息
     *
     * @param orderId 订单id
     * @return 订单地址信息
     */
    @GetMapping("/info")
    public R getOrderAddress(
            @CookieValue("shUserId")
            @NotNull(message = "登录异常 请重新登录")
            @RequestParam Long orderId
    ) {
        return R.success(orderAddressService.getOrderAddress(orderId));
    }
}
