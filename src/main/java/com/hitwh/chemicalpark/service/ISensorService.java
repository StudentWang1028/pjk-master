package com.hitwh.chemicalpark.service;

import com.hitwh.chemicalpark.entity.Sensor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hitwh.chemicalpark.entity.Sensordata;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author PJK
 * @since 2023-04-18
 */
public interface ISensorService extends IService<Sensor> {
    List<Sensordata> modbusRead(Sensor sensor);

}
