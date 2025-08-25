package com.shanzhu.st.mapper;

import com.shanzhu.st.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收藏 数据库持久层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@Mapper
public interface FavoriteMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Favorite record);

    int insertSelective(Favorite record);

    Favorite selectByPrimaryKey(Long id);

    List<Favorite> getMyFavorite(Long userId);

    Integer checkFavorite(Long userId, Long idleId);

    int updateByPrimaryKeySelective(Favorite record);

    int updateByPrimaryKey(Favorite record);
}