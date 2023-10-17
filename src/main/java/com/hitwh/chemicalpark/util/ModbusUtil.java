package com.hitwh.chemicalpark.util;



import com.hitwh.chemicalpark.entity.Sensor;
import com.hitwh.chemicalpark.entity.Sensordata;

import org.springframework.stereotype.Component;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;

import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ModbusUtil{

    public List<Sensordata> modbusGas(Sensor sensor){
        SerialParameters paramsGas = new SerialParameters();
        paramsGas.setPortName(sensor.getPortname()); // 串口名称
        paramsGas.setBaudRate(sensor.getBaudrate()); // 波特率
        paramsGas.setDatabits(sensor.getDatabits()); // 数据位
        paramsGas.setParity(sensor.getParity()); // 奇偶校验
        paramsGas.setStopbits(sensor.getStopbits()); // 停止位
        paramsGas.setEncoding(Modbus.SERIAL_ENCODING_RTU);
        // 创建串口连接
        SerialConnection con = new SerialConnection(paramsGas);
        try {
            con.open();
            // 创建读取请求
            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(0, 7);
            //从站地址设置
            req.setUnitID(sensor.getSlaveadd());
            // 创建事务并设置请求
            ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);
            // 执行事务并获取响应
            trans.execute();
            ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();
            //存入数据库
            Sensordata sensordata = new Sensordata();
            sensordata.setSdata(String.valueOf(res.getRegisterValue(6)));
            sensordata.setDatatype(sensor.getSensortype());
            sensordata.setSid(sensor.getSid());
            sensordata.setPid(sensor.getPid());
            sensordata.setCas(sensor.getCas());
            System.out.println("1111111111111");
            List<Sensordata> datalist = new ArrayList<>();
            datalist.add(sensordata);
            return datalist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
            return null;
    }

    public List<Sensordata> modbusTH(Sensor sensor) {
        SerialParameters paramsTH = new SerialParameters();
        paramsTH.setPortName(sensor.getPortname()); // 串口名称
        paramsTH.setBaudRate(sensor.getBaudrate()); // 波特率
        paramsTH.setDatabits(sensor.getDatabits()); // 数据位
        paramsTH.setParity(sensor.getParity()); // 奇偶校验
        paramsTH.setStopbits(sensor.getStopbits()); // 停止位
        paramsTH.setEncoding(Modbus.SERIAL_ENCODING_RTU);


        // 创建串口连接
        SerialConnection con = new SerialConnection(paramsTH);

        try {
            con.open();


            // 创建读取请求
            //ReadInputRegistersRequest req = new ReadInputRegistersRequest(0, 1);
            //设置读取的位数，从0开始计数
            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(0, 2);

            //从站地址
            req.setUnitID(sensor.getSlaveadd());


            // 创建事务并设置请求
            ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);

            // 执行事务并获取响应
            trans.execute();
            //ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
            ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();


            //存入数据库
            Sensordata sensordata1 = new Sensordata();
            sensordata1.setSdata(String.valueOf(res.getRegisterValue(0)/10.0));
            sensordata1.setDatatype("湿度");
            sensordata1.setSid(sensor.getSid());
            sensordata1.setPid(sensor.getPid());
            sensordata1.setCas(sensor.getCas());

            Sensordata sensordata2 = new Sensordata();
            sensordata2.setSdata(String.valueOf(res.getRegisterValue(1)/10.0));
            sensordata2.setDatatype("温度");
            sensordata2.setSid(sensor.getSid());
            sensordata2.setPid(sensor.getPid());
            sensordata2.setCas(sensor.getCas());


            List<Sensordata> datalist = new ArrayList<>();
            datalist.add(sensordata1);
            datalist.add(sensordata2);
            // 输出结果
//            Logger log = LoggerFactory.getLogger(ModbusUtil.class);
////            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            log.info("任务执行时间: " + dateFormat.format(new Date()) + res.getRegisterValue(0));
            return datalist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return null;
    }
}
