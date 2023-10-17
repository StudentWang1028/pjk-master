package com.hitwh.chemicalpark.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.common.Result;
import com.hitwh.chemicalpark.entity.*;
import com.hitwh.chemicalpark.service.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-12
 */
@RestController
@RequestMapping("/sensor")
        public class SensorController {

        @Resource
        private ISensorService sensorService;

        @Resource
        private ISensordataService sensordataService;

        @Resource
        private ICombustiblegasService combustiblegasService;

        @Resource
        private IPosiongasService posiongasService;

        @Resource
        private IAlertService alertService;

        @PostMapping
        public Boolean save(@RequestBody Sensor sensor) {
                return sensorService.saveOrUpdate(sensor);
                }

        @PostMapping("/update")
        public Result update(@RequestBody Sensor sensor) {
                return Result.success(sensorService.updateById(sensor));
        }

        @DeleteMapping("/{sid}")
        public Boolean delete(@PathVariable Integer sid) {
                return sensorService.removeById(sid);
                }

        @GetMapping
        public List<Sensor> findAll() {
                return sensorService.list();
                }

        @GetMapping("/{sid}")
        public Sensor findOne(@PathVariable Integer sid) {
                return sensorService.getById(sid);
                }



        @GetMapping("/page")
        public Page<Sensor> findPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String cas) {
                QueryWrapper<Sensor> queryWrapper = new QueryWrapper<>();
                if(!"".equals(cas)){
                        queryWrapper.like("cas", cas);
                }
                queryWrapper.orderByAsc("sid");
                return sensorService.page(new Page<>(pageNum, pageSize), queryWrapper);
        }
        @PostMapping("/import")
        public void imp(MultipartFile file) throws Exception {
                InputStream inputStream = file.getInputStream();
                ExcelReader reader = ExcelUtil.getReader(inputStream);
                // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//         List<User> list = reader.readAll(User.class);

                // 方式2：忽略表头的中文，直接读取表的内容
                List<List<Object>> list = reader.read(1); //忽略第一行
                List<Sensor> sensors = CollUtil.newArrayList();
                for (List<Object> row : list) {
                        Sensor sensor = new Sensor();
                        sensor.setPid((Integer) row.get(0));
                        sensor.setCas(row.get(1).toString());
                        sensor.setDevicemodel(row.get(2).toString());
                        sensor.setSensortype(row.get(3).toString());
                        sensor.setBaudrate((Integer) row.get(4));
                        sensor.setDatabits((Integer) row.get(5));
                        sensor.setStopbits((Integer) row.get(6));
                        sensor.setParity(row.get(7).toString());
                        sensor.setPortname((String) row.get(8));
                        sensor.setSlaveadd((Integer) row.get(9));
                        sensors.add(sensor);
                }
                sensorService.saveBatch(sensors);
        }


        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> sids){
                return sensorService.removeBatchByIds(sids);
        }

        @PostMapping("/read")
        public boolean modbusRead(){
                int count = (int)sensorService.count();
                int i = 0;
                for(i = 1; i <= count; i++){
                        Sensor sensor = sensorService.getById(i);
                        List<Sensordata> datalist = sensorService.modbusRead(sensor);
                        for(Sensordata sensordata : datalist){
                                sensordataService.saveOrUpdate(sensordata);
                        }
                }
                return true;
        }

        static Boolean readFlag = false;
        private BlockingQueue<Sensordata> sharedQueue = new LinkedBlockingQueue<>();
        private final Object lock = new Object();
        @RequestMapping("/read/start")
        public Boolean startRead() {
                // 将标志设置为true，表示需要执行任务
                readFlag = true;
                // 启动一个新线程执行snap任务
                new Thread(() -> {
                                while (readFlag) {
                                        int count = (int)sensorService.count();
                                        int i = 0;
                                        for(i = 1; i <= count; i++){
                                                Sensor sensor = sensorService.getById(i);
                                                List<Sensordata> datalist = sensorService.modbusRead(sensor);
                                                if(datalist==null){
                                                        continue;
                                                }
                                                int size = datalist.size();
                                                int j = 0;
                                                for(j = 0; j < size; j++){
                                                        sensordataService.saveOrUpdate(datalist.get(j));
                                                        sharedQueue.offer(datalist.get(j));
                                                }
                                        }
                                        try {
                                                Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                }
                }).start();
                return true;
        }


        @RequestMapping("/read/stop")
        public Boolean stopRead() {
                // 将标志设置为false，表示停止任务
                readFlag = false;
                return true;
        }


        @RequestMapping("/test")
        public void test() {
                Sensordata sensordata = sharedQueue.poll();
                LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
                while (sensordata != null){
                        System.out.println(sensordata.getDatatime().toString());
                        System.out.println("拿到一条队列的数据，数据ID为"+sensordata.getSdid());
                        sensordata = sharedQueue.poll();
                }

        }



        @RequestMapping("/process")
        public void anaylsisSensordata() {
                LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

                Sensordata sensordata = sharedQueue.poll();
                //分别处理温湿度和气体报警
                while (sensordata != null) {
                        System.out.println("————————————测试队列数据——————————"+sensordata.getDatatime());
                        LocalDateTime datatime = sensordata.getDatatime();
                        //温度预警判定
                        if(sensordata.getDatatype().equals("温度")){
                                alertService.tempAlert(sensordata);
                                //跳出查询下一条数据
                                continue;
                        }
                        //气体预警判定，一个数据查两遍，因为可能既是有毒气体又是可燃气体。
                        String cas = sensordata.getCas();
                        //查询有毒气体
                        QueryWrapper<Posiongas> posiongasQueryWrapper = new QueryWrapper<>();
                        if(!"".equals(cas)){
                                posiongasQueryWrapper.like("cas",cas);
                        }
                        if(posiongasService.count(posiongasQueryWrapper) == 1){
                                Posiongas posiongas = posiongasService.getOne(posiongasQueryWrapper);
                                boolean pAlert = alertService.posiongasAlert(sensordata,posiongas);

                        }

                        //查询可燃气体
                        QueryWrapper<Combustiblegas> combustiblegasQueryWrapper = new QueryWrapper<>();
                        if(!"".equals(cas)){
                                combustiblegasQueryWrapper.like("cas",cas);
                        }
                        if(combustiblegasService.count(combustiblegasQueryWrapper) == 1){
                                Combustiblegas combustiblegas = combustiblegasService.getOne(combustiblegasQueryWrapper);
                                alertService.combustiblegasAlert(sensordata,combustiblegas);
                        }
                        //进入下一条数据
                        sensordata = sharedQueue.poll();
                }
        }



    }

