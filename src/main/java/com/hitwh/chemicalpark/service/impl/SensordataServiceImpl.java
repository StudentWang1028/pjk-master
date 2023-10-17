package com.hitwh.chemicalpark.service.impl;

import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Sensordata;
import com.hitwh.chemicalpark.mapper.SensordataMapper;
import com.hitwh.chemicalpark.service.ISensordataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PJK
 * @since 2023-04-12
 */
@Service
public class SensordataServiceImpl extends ServiceImpl<SensordataMapper, Sensordata> implements ISensordataService {

}
