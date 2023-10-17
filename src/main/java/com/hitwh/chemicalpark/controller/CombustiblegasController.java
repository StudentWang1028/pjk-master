package com.hitwh.chemicalpark.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.common.Result;
import com.hitwh.chemicalpark.entity.Posiongas;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.ICombustiblegasService;
import com.hitwh.chemicalpark.entity.Combustiblegas;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/combustiblegas")
        public class CombustiblegasController {
    
        @Resource
        private ICombustiblegasService combustiblegasService;

        @PostMapping
        public Boolean save(@RequestBody Combustiblegas combustiblegas) {
                return combustiblegasService.saveOrUpdate(combustiblegas);
                }

        @DeleteMapping("/{gasid}")
        public Boolean delete(@PathVariable Integer gasid) {
                return combustiblegasService.removeById(gasid);
                }

        @GetMapping
        public List<Combustiblegas> findAll() {
                return combustiblegasService.list();
                }

        @GetMapping("/{gasid}")
        public Combustiblegas findOne(@PathVariable Integer gasid) {
                return combustiblegasService.getById(gasid);
                }

        @GetMapping("/page")
        public Page<Combustiblegas> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam(defaultValue = "") String cas,
                                             @RequestParam(defaultValue = "") String gasnamezh,
                                             @RequestParam(defaultValue = "") String gasnameen) {
                QueryWrapper<Combustiblegas> queryWrapper = new QueryWrapper<>();
                if(!"".equals(cas)){
                        queryWrapper.like("cas", cas);
                }
                if(!"".equals(gasnamezh)){
                        queryWrapper.like("gasnamezh", gasnamezh);
                }
                if(!"".equals(gasnameen)){
                        queryWrapper.like("gasnameen", gasnameen);
                }
                queryWrapper.orderByAsc("gasid");
                return combustiblegasService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> gasids){
                return combustiblegasService.removeBatchByIds(gasids);
                }

        @PostMapping("/import")
        public Result imp(MultipartFile file) throws Exception {
                InputStream inputStream = file.getInputStream();
                ExcelReader reader = ExcelUtil.getReader(inputStream);
                // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//         List<User> list = reader.readAll(User.class);

                // 方式2：忽略表头的中文，直接读取表的内容
                List<List<Object>> list = reader.read(1); //忽略第一行
                List<Combustiblegas> combustiblegases = CollUtil.newArrayList();
                for (List<Object> row : list) {
                        Combustiblegas combustiblegas = new Combustiblegas();
                        combustiblegas.setCas(row.get(0).toString());
                        combustiblegas.setGasnamezh(row.get(1).toString());
                        combustiblegas.setGasnameen(row.get(2).toString());
                        combustiblegas.setMw(row.get(3).toString());
                        combustiblegas.setBoilingpoint(row.get(4).toString());
                        combustiblegas.setFlashpoint(row.get(5).toString());
                        combustiblegas.setLel(row.get(6).toString());
                        combustiblegas.setUel(row.get(7).toString());
                        combustiblegas.setDanger(row.get(8).toString());
                        combustiblegas.setSteamdensity(row.get(9).toString());
                        combustiblegases.add(combustiblegas);
                }
                combustiblegasService.saveBatch(combustiblegases);
                return Result.success(true);
        }

    }

