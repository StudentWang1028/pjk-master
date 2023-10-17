package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.entity.Camera;
import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.service.ICameraService;
import com.hitwh.chemicalpark.service.IAlertService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.IPicService;
import com.hitwh.chemicalpark.entity.Pic;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-22
 */
@RestController
@RequestMapping("/pic")
        public class PicController {
    
        @Resource
        private IPicService picService;

        @Resource
        private ICameraService cameraService;

        @Resource
        private IAlertService firealertService;



        @PostMapping
        public Boolean save(@RequestBody Pic pic) {
                return picService.saveOrUpdate(pic);
                }

        @DeleteMapping("/{id}")
        public Boolean delete(@PathVariable Integer id) {
                return picService.removeById(id);
                }

        @GetMapping
        public List<Pic> findAll() {
                return picService.list();
                }

        @GetMapping("/{id}")
        public Pic findOne(@PathVariable Integer id) {
                return picService.getById(id);
                }

        @GetMapping("/page")
        public Page<Pic> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
                QueryWrapper<Pic> queryWrapper = new QueryWrapper<>();
                queryWrapper.orderByAsc("id");
                return picService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> ids){
                return picService.removeBatchByIds(ids);
                }

        @RequestMapping("/snap")
        public Boolean snap(){
                int count = (int)cameraService.count();
                int i = 1;
                for(i = 1;i <= count;i++){
                        Camera camera = cameraService.getById(i);
                        Alert firealert = picService.snap(camera);
                        if(firealert != null){
                                System.out.println("生成新预警！");
                                firealertService.saveOrUpdate(firealert);
                        }
                }
                return true;
        }

        Boolean snapFlag = false;
        @RequestMapping("/snap/start")
        public Boolean startSnap() {
            // 将标志设置为true，表示需要执行任务
            snapFlag = true;

            // 启动一个新线程执行snap任务
            new Thread(() -> {
                while (snapFlag) {
                    int count = (int) cameraService.count();
                    int i = 1;
                    for (i = 1; i <= count; i++) {
                        Camera camera = cameraService.getById(i);
                        Alert firealert = picService.snap(camera);
                        if (firealert != null) {
                            System.out.println("生成新预警！");
                            firealertService.saveOrUpdate(firealert);
                        }
                    }
                    try {
                        // 每30秒执行一次
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return true;
        }


        @RequestMapping("/snap/stop")
        public Boolean stopSnap() {
            // 将标志设置为false，表示停止任务
            snapFlag = false;
            return true;
        }




    }

