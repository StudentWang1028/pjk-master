package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.controller.dto.PositionDTO;
import com.hitwh.chemicalpark.controller.dto.PositionWithSensordata;
import com.hitwh.chemicalpark.mapper.PositionMapper;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.IPositionService;
import com.hitwh.chemicalpark.entity.Position;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-05-02
 */
@RestController
@RequestMapping("/position")
        public class PositionController {

        @Resource
        private IPositionService positionService;

        @Resource
        private PositionMapper positionMapper;

        @PostMapping
        public Boolean save(@RequestBody Position position) {
                return positionService.saveOrUpdate(position);
        }

        @DeleteMapping("/{pid}")
        public Boolean delete(@PathVariable Integer pid) {
                return positionService.removeById(pid);
        }

        @GetMapping
        public List<Position> findAll() {
                return positionService.list();
        }

        @GetMapping("/{id}")
        public Position findOne(@PathVariable Integer id) {
                return positionService.getById(id);
        }

        @GetMapping("/page")
        public Page<Position> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String pname) {
                QueryWrapper<Position> queryWrapper = new QueryWrapper<>();
                if(!"".equals(pname)){
                        queryWrapper.like("pname", pname);
                }
                queryWrapper.orderByAsc("pid");
                return positionService.page(new Page<>(pageNum, pageSize), queryWrapper);
        }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> pids) {
                return positionService.removeBatchByIds(pids);
        }


        @GetMapping("/getPositions")
        public List<PositionDTO> getPositions() {
                List<Position> positionList = positionService.list();
                List<PositionDTO> resultList = new ArrayList<>();
                for (Position position : positionList) {
                        PositionDTO positionDTO = new PositionDTO(position);
                        resultList.add(positionDTO);
                }
                return resultList;
        }


        @GetMapping("/getPositionData")
        public List<PositionWithSensordata> getPositionData(){
                List<PositionWithSensordata> resultlist = positionMapper.selectPositionWithSensordata();
                return resultlist;
        }
}

