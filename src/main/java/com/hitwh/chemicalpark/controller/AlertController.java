package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.entity.User;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.IAlertService;
import com.hitwh.chemicalpark.entity.Alert;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/alert")
        public class AlertController {
    
        @Resource
        private IAlertService alertService;

        @PostMapping
        public Boolean save(@RequestBody Alert alert) {
                return alertService.saveOrUpdate(alert);
                }

        @DeleteMapping("/{aid}")
        public Boolean delete(@PathVariable Integer aid) {
                return alertService.removeById(aid);
                }

        @GetMapping
        public List<Alert> findAll() {
                return alertService.list();
                }

        @GetMapping("/{aid}")
        public Alert findOne(@PathVariable Integer aid) {
                return alertService.getById(aid);
                }

        @GetMapping("/page")
        public Page<Alert> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String alertlevel,
                                    @RequestParam(defaultValue = "") String alerttype) {
                QueryWrapper<Alert> queryWrapper = new QueryWrapper<>();
                if(!"".equals(alertlevel)){
                        queryWrapper.like("alertlevel", alertlevel);
                }
                if(!"".equals(alerttype)){
                        queryWrapper.like("alerttype", alerttype);
                }
                queryWrapper.orderByDesc("aid");
                return alertService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> aids){
                return alertService.removeBatchByIds(aids);
                }

    }

