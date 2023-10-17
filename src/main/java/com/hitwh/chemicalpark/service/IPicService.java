package com.hitwh.chemicalpark.service;

import com.hitwh.chemicalpark.entity.Camera;
import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Pic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author PJK
 * @since 2023-04-22
 */
public interface IPicService extends IService<Pic> {
    Alert snap(Camera camera);

}
