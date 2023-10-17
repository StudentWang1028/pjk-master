package com.hitwh.chemicalpark.util;



import com.hitwh.chemicalpark.entity.Sensordata;
import com.hitwh.chemicalpark.mapper.SensorMapper;
import com.hitwh.chemicalpark.mapper.SensordataMapper;
import com.hitwh.chemicalpark.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;

import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ModbusUtils {
    @Autowired
    SensordataMapper sensordataMapper;
    @Autowired
    SensorMapper sensorMapper;

    @Resource
    private ISensorService sensorService;


    @Scheduled(fixedRate = 5000)
//    @PostConstruct
    public void getTempAndHumi() {



        SerialParameters params = new SerialParameters();
        params.setPortName("COM12"); //
        params.setBaudRate(9600); // 波特率
        params.setDatabits(8); // 数据位
        params.setParity("None"); // 奇偶校验
        params.setStopbits(1); // 停止位
        params.setEncoding(Modbus.SERIAL_ENCODING_RTU);
        // 创建串口连接
        SerialConnection con = new SerialConnection(params);
        try {
            con.open();



            // 创建读取请求
            //ReadInputRegistersRequest req = new ReadInputRegistersRequest(0, 1);
            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(0, 2);
            req.setUnitID(2);


            // 创建事务并设置请求
            ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);

            // 执行事务并获取响应
            trans.execute();
            //ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
            ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();


            //存入数据库
            Sensordata sensordata = new Sensordata();
            sensordata.setSdata(String.valueOf(res.getRegisterValue(0)));
            sensordata.setDatatype("湿度");
            sensordata.setSid(2);
            sensordata.setPid(1);
            sensordata.setCas("Test");
            sensordataMapper.insert(sensordata);


            // 输出结果
            System.out.println(res.getRegisterValue(0));
            System.out.println(res.getRegisterValue(1));
            Logger log = LoggerFactory.getLogger(ModbusUtils.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            log.info("任务执行时间: " + dateFormat.format(new Date()) + res.getRegisterValue(0) + res.getRegisterValue(1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }
}