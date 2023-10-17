package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.ICameraService;
import com.hitwh.chemicalpark.entity.Camera;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/camera")
        public class CameraController {
    
        @Resource
        private ICameraService cameraService;

        @PostMapping
        public Boolean save(@RequestBody Camera camera) {
                return cameraService.saveOrUpdate(camera);
                }

        @DeleteMapping("/{cid}")
        public Boolean delete(@PathVariable Integer cid) {
                return cameraService.removeById(cid);
                }

        @GetMapping
        public List<Camera> findAll() {
                return cameraService.list();
                }

        @GetMapping("/{cid}")
        public Camera findOne(@PathVariable Integer cid) {
                return cameraService.getById(cid);
                }

        @GetMapping("/page")
        public Page<Camera> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String devicemodel,
                                     @RequestParam(defaultValue = "") String cuser) {
                QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
                if(!"".equals(devicemodel)){
                        queryWrapper.like("devicemodel",devicemodel);
                }
                if(!"".equals(cuser)){
                        queryWrapper.like("cuser", cuser);
                }
                queryWrapper.orderByAsc("cid");
                return cameraService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> cids){
                return cameraService.removeBatchByIds(cids);
                }

    }

