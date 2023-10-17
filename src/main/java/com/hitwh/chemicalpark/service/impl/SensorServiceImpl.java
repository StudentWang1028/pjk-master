package com.hitwh.chemicalpark.service.impl;

import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Sensor;
import com.hitwh.chemicalpark.entity.Sensordata;
import com.hitwh.chemicalpark.mapper.SensorMapper;
import com.hitwh.chemicalpark.service.ISensorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitwh.chemicalpark.util.ModbusUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PJK
 * @since 2023-04-
 */
@Service
public class SensorServiceImpl extends ServiceImpl<SensorMapper, Sensor> implements ISensorService {
    public List<Sensordata> modbusRead(Sensor sensor){
        if(!sensor.getEnable()){
            return null;
        }

        ModbusUtil modbusUtil = new ModbusUtil();
        if(sensor.getSensortype().equals("温湿度")){
            return modbusUtil.modbusTH(sensor);
        }else{
            return modbusUtil.modbusGas(sensor);
        }
    }

}
