package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.ISensordataService;
import com.hitwh.chemicalpark.entity.Sensordata;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-12
 */
@RestController
@RequestMapping("/sensordata")
        public class SensordataController {
    
        @Resource
        private ISensordataService sensordataService;

        @PostMapping
        public Boolean save(@RequestBody Sensordata sensordata) {
                return sensordataService.saveOrUpdate(sensordata);
                }

        @DeleteMapping("/{sdid}")
        public Boolean delete(@PathVariable Integer sdid) {
                return sensordataService.removeById(sdid);
                }

        @GetMapping
        public List<Sensordata> findAll() {
                return sensordataService.list();
                }

        @GetMapping("/{sdid}")
        public Sensordata findOne(@PathVariable Integer sdid) {
                return sensordataService.getById(sdid);
                }

        @GetMapping("/page")
        public Page<Sensordata> findPage(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam(defaultValue = "") String datatype,
                                         @RequestParam(defaultValue = "") String cas) {
                QueryWrapper<Sensordata> queryWrapper = new QueryWrapper<>();
                if(!"".equals(cas)){
                        queryWrapper.like("cas", cas);
                }
                if(!"".equals(datatype)){
                        queryWrapper.like("sensortype", datatype);
                }
                queryWrapper.orderByDesc("sdid");
                return sensordataService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> sdids){
                return sensordataService.removeBatchByIds(sdids);
                }

    }

