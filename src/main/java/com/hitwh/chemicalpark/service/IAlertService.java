package com.hitwh.chemicalpark.service;

import com.hitwh.chemicalpark.entity.Alert;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hitwh.chemicalpark.entity.Combustiblegas;
import com.hitwh.chemicalpark.entity.Posiongas;
import com.hitwh.chemicalpark.entity.Sensordata;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author PJK
 * @since 2023-04-28
 */
public interface IAlertService extends IService<Alert> {
    boolean tempAlert(Sensordata sensordata);
    boolean posiongasAlert(Sensordata sensordata, Posiongas posiongas);
    boolean combustiblegasAlert(Sensordata sensordata, Combustiblegas combustiblegas);

}
