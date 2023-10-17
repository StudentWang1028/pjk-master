package com.hitwh.chemicalpark.mapper;

import com.hitwh.chemicalpark.controller.dto.PositionWithSensordata;
import com.hitwh.chemicalpark.entity.Position;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author PJK
 * @since 2023-05-02
 */
@Mapper
public interface PositionMapper extends BaseMapper<Position> {
    List<PositionWithSensordata> selectPositionWithSensordata();

}
