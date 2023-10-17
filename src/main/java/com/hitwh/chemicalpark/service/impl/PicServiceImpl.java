package com.hitwh.chemicalpark.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hitwh.chemicalpark.entity.Camera;
import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Pic;
import com.hitwh.chemicalpark.mapper.PicMapper;
import com.hitwh.chemicalpark.service.IPicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitwh.chemicalpark.util.CapturePictureUtil;
import com.hitwh.chemicalpark.util.PicRecognitionUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PJK
 * @since 2023-04-22
 */
@Service
public class PicServiceImpl extends ServiceImpl<PicMapper, Pic> implements IPicService {
    @SneakyThrows
    public Alert snap(Camera camera){
        CapturePictureUtil capturePictureUtil = new CapturePictureUtil();

        Pic pic = capturePictureUtil.start(camera);
        //插入日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestring = dateFormat.format(new Date());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime LocalTime = LocalDateTime.parse(datestring,df);
        pic.setDatatime(LocalTime);
        //插入地址
        pic.setPid(camera.getPid());
        pic.setCid(camera.getCid());
        saveOrUpdate(pic);


        //图像识别
        PicRecognitionUtil YOLOUtil = new PicRecognitionUtil();
        Alert firealert = YOLOUtil.YOLO(pic);

        return firealert;
    }

}
