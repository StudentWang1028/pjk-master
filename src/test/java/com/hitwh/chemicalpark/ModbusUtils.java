package com.hitwh.chemicalpark;



import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.hitwh.chemicalpark.entity.Sensordata;
import com.hitwh.chemicalpark.mapper.SensordataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;

import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ModbusUtils {
    public static void main(String[] args) {
        SensordataMapper sensordataMapper;

            SerialParameters params = new SerialParameters();
            params.setPortName("COM9"); // 串口名称
            params.setBaudRate(9600); // 波特率
            params.setDatabits(8); // 数据位
            params.setParity("None"); // 奇偶校验
            params.setStopbits(1); // 停止位

            params.setEncoding(Modbus.SERIAL_ENCODING_RTU);
            // 创建串口连接
            SerialConnection con = new SerialConnection(params);
            try {
                con.open();



                //创建读取请求
                //ReadInputRegistersRequest req = new ReadInputRegistersRequest(0, 1);
                ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(0, 2);


                // 设置从站地址
                req.setUnitID(1);


                // 创建事务并设置请求
                ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
                trans.setRequest(req);

                // 执行事务并获取响应
                trans.execute();
                //ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
                ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();

                // 输出结果
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
