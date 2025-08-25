package com.shanzhu.st.mapper;

import com.shanzhu.st.entity.IdleItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 闲置商品 数据持久层
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@Mapper
public interface IdleItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IdleItem record);

    int insertSelective(IdleItem record);

    IdleItem selectByPrimaryKey(Long id);

    List<IdleItem> getAllIdleItem(Long userId);

    int countIdleItem(String findValue);

    int countIdleItemByLable(int idleLabel);

    int countIdleItemByStatus(int status);

    List<IdleItem> findIdleItem(String findValue, int begin, int nums);

    List<IdleItem> findIdleItemByLable(int idleLabel, int begin, int nums);

    List<IdleItem> getIdleItemByStatus(int status, int begin, int nums);

    int updateByPrimaryKeySelective(IdleItem record);

    int updateByPrimaryKey(IdleItem record);

    List<IdleItem> findIdleByList(List<Long> idList);
}