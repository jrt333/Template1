package com.shanzhu.st.controller;

import com.shanzhu.st.entity.Favorite;
import com.shanzhu.st.enums.ErrorMsg;
import com.shanzhu.st.service.FavoriteService;
import com.shanzhu.st.vo.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 收藏 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@CrossOrigin
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    /**
     * 添加收藏
     *
     * @param shUserId 用户id
     * @param favorite 收藏信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public R addFavorite(
            @CookieValue(value = "shUserId", defaultValue = "") String shUserId,
            @RequestBody Favorite favorite
    ) {
        if (shUserId.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        favorite.setUserId(Long.valueOf(shUserId));
        favorite.setCreateTime(new Date());
        if (favoriteService.addFavorite(favorite)) {
            return R.success(favorite.getId());
        }
        return R.fail(ErrorMsg.FAVORITE_EXIT);
    }

    /**
     * 删除收藏
     *
     * @param id 收藏id
     * @return 结果
     */
    @GetMapping("/delete")
    public R deleteFavorite(
            @CookieValue(value = "shUserId", defaultValue = "") String shUserId,
            @RequestParam Long id
    ) {
        if (shUserId.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        if (favoriteService.deleteFavorite(id)) {
            return R.success();
        }
        return R.fail(ErrorMsg.SYSTEM_ERROR);
    }

    /**
     * 检查收藏状态
     *
     * @param shUserId 用户id
     * @param idleId   收藏id
     * @return 结果
     */
    @GetMapping("/check")
    public R checkFavorite(
            @CookieValue(value = "shUserId", defaultValue = "") String shUserId,
            @RequestParam Long idleId
    ) {
        if (shUserId.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        return R.success(favoriteService.isFavorite(Long.valueOf(shUserId), idleId));
    }

    /**
     * 我的收藏
     *
     * @param shUserId 用户d
     * @return 收藏列表
     */
    @GetMapping("/my")
    public R getMyFavorite(
            @CookieValue(value = "shUserId", defaultValue = "") String shUserId
    ) {
        if (shUserId.isEmpty()) {
            return R.fail(ErrorMsg.COOKIE_ERROR);
        }
        return R.success(favoriteService.getAllFavorite(Long.valueOf(shUserId)));
    }
}
