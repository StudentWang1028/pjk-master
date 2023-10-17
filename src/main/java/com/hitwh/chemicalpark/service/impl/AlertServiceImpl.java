package com.hitwh.chemicalpark.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Combustiblegas;
import com.hitwh.chemicalpark.entity.Posiongas;
import com.hitwh.chemicalpark.entity.Sensordata;
import com.hitwh.chemicalpark.mapper.AlertMapper;
import com.hitwh.chemicalpark.service.IAlertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.nio.sctp.SendFailedNotification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author PJK
 * @since 2023-04-28
 */
@Service
public class AlertServiceImpl extends ServiceImpl<AlertMapper, Alert> implements IAlertService {

    //定温阈值报警
    public boolean tempAlert(Sensordata sensordata){
        String temp = sensordata.getSdata();
        Float tempdata = Float.parseFloat(sensordata.getSdata());
        if(tempdata > 55) {
            Alert alert = new Alert();
            alert.setSid(sensordata.getSid());
            alert.setPid(sensordata.getPid());
            alert.setAlertdata(sensordata.getSdata());
            alert.setAlerttype("温感火警");
            QueryWrapper<Alert> queryWrapper = new QueryWrapper<>();
            if (tempdata < 70) {
                alert.setAlertlevel("预警");
            } else {
                alert.setAlertlevel("高报");
            }

            // 查询过去十五分钟内是否有相同级别的预警
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fifteenMinutesAgo = now.minusMinutes(15);
            QueryWrapper<Alert> querywrapper = new QueryWrapper<>();
            querywrapper.eq("alertlevel", alert.getAlertlevel())
                    .ge("alerttime", fifteenMinutesAgo)
                    .lt("alerttime", now);
            Alert existAlert = getOne(querywrapper);
            if (existAlert == null) {
                saveOrUpdate(alert);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //有毒气体报警
    public boolean posiongasAlert(Sensordata sensordata,Posiongas posiongas){
        System.out.println("————————进入有毒气体预警计算——————————");
        //数据转换
        float mw = Float.parseFloat(posiongas.getMw());
        float mac = 0, pcstel = 0,idlh = 0;
        boolean ismac = false, ispcstel = false, isidlh = false;
        //判断数据库有没有值，若有就进入判断
        if(!"".equals(posiongas.getMac())){
            float macCM = Float.parseFloat(posiongas.getMac());
            mac = (float) (macCM * 22.4 / mw);
            ismac = MACAlert(sensordata,mac);
        }

        if(!"".equals(posiongas.getPcstel())){
            float pcstelCM = Float.parseFloat(posiongas.getPcstel());
            pcstel = (float) (pcstelCM * 22.4 / mw);
        }

        if(!"".equals(posiongas.getIdlh())){
            float idlhCM = Float.parseFloat(posiongas.getIdlh());
            idlh = (float) (idlhCM * 22.4 / mw);
        }


        ismac = MACAlert(sensordata,mac);
        ispcstel = PCSETLAlert(sensordata,pcstel);
        isidlh = IDLHAlert(sensordata,idlh);
        return ismac || ispcstel || isidlh;
    }

    public boolean MACAlert(Sensordata sensordata,float mac){
        Float data = Float.parseFloat(sensordata.getSdata());
        Alert alert = new Alert();
        alert.setSid(sensordata.getSid());
        alert.setPid(sensordata.getPid());
        alert.setAlertdata(sensordata.getSdata());
        alert.setAlerttype(sensordata.getDatatype() + " MAC超标");
        alert.setAlerttime(LocalDateTime.now());
        if(data >= 2 * mac) {
            alert.setAlertlevel("高报");
            return true;
        } else if (data >= mac) {
            alert.setAlertlevel("预警");
            return true;
        } else if (data >= 0.5 * mac) {
            alert.setAlertlevel("预报");
            return true;
        } else {
            // 如果数据不超过0.5倍macppm，则不发出警报
            return false;
        }
    }

    public boolean PCSETLAlert(Sensordata sensordata,float pcstel){
        Float data = Float.parseFloat(sensordata.getSdata());
        Alert alert = new Alert();
        alert.setSid(sensordata.getSid());
        alert.setPid(sensordata.getPid());
        alert.setAlertdata(sensordata.getSdata());
        alert.setAlerttype(sensordata.getDatatype() + " PC-STEL超标");
        alert.setAlerttime(LocalDateTime.now());
        if(data >= 2 * pcstel) {
            alert.setAlertlevel("高报");
            return true;
        } else if (data >= pcstel) {
            alert.setAlertlevel("预警");
            return true;
        } else if (data >= 0.5 * pcstel) {
            alert.setAlertlevel("预报");
            return true;
        } else {
            return false;
        }
    }

    public boolean IDLHAlert(Sensordata sensordata,float idlh){
        Float data = Float.parseFloat(sensordata.getSdata());
        Alert alert = new Alert();
        alert.setSid(sensordata.getSid());
        alert.setPid(sensordata.getPid());
        alert.setAlertdata(sensordata.getSdata());
        alert.setAlerttype(sensordata.getDatatype() + " PC-STEL超标");
        alert.setAlerttime(LocalDateTime.now());
        if(data >= 0.10 * idlh) {
            alert.setAlertlevel("高报");
            return true;
        } else if (data >= 0.05 * idlh) {
            alert.setAlertlevel("预警");
            return true;
        } else {
            return false;
        }
    }

    //可燃气体报警
    public boolean combustiblegasAlert(Sensordata sensordata,Combustiblegas combustiblegas){
        //数据转换
        float mw = Float.parseFloat(combustiblegas.getMw());
        float data = Float.parseFloat(sensordata.getSdata());
        float lel = 0;
        float flashpoint = 0;
        boolean islel = false, isflash = false;
        if(!"".equals(combustiblegas.getLel())){
            float lelCM = Float.parseFloat(combustiblegas.getLel());
            lel = (float) (lelCM * 22.4 / mw);
            islel = LELAlert(sensordata,lel);
        }

        //闪点预警
//        if(!"".equals(combustiblegas.getFlashpoint())){
//            flashpoint = Float.parseFloat(combustiblegas.getFlashpoint());
//            isflash = FLASHAlert(sensordata,flashpoint);
//        }
        return islel;
    }

    public boolean LELAlert(Sensordata sensordata,float lel){
        Float data = Float.parseFloat(sensordata.getSdata());
        Alert alert = new Alert();
        alert.setSid(sensordata.getSid());
        alert.setPid(sensordata.getPid());
        alert.setAlertdata(sensordata.getSdata());
        alert.setAlerttime(LocalDateTime.now());
        alert.setAlerttype(sensordata.getDatatype() + " 爆炸风险");
        if(data >= 0.25 * lel) {
            alert.setAlertlevel("高报");
            return true;
        } else if (data >= 0.5 * lel) {
            alert.setAlertlevel("预警");
            return true;
        } else {
            return false;
        }
    }


}
