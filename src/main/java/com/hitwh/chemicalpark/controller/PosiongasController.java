package com.hitwh.chemicalpark.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.common.Result;
import com.hitwh.chemicalpark.entity.Posiongas;
import com.hitwh.chemicalpark.entity.User;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.IPosiongasService;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/posiongas")
        public class PosiongasController {

        @Resource
        private IPosiongasService posiongasService;

        @PostMapping
        public Boolean save(@RequestBody Posiongas posiongas) {
                return posiongasService.saveOrUpdate(posiongas);
                }

        @DeleteMapping("/{gasid}")
        public Boolean delete(@PathVariable Integer gasid) {
                return posiongasService.removeById(gasid);
                }

        @GetMapping
        public List<Posiongas> findAll() {
                return posiongasService.list();
                }

        @GetMapping("/{gasid}")
        public Posiongas findOne(@PathVariable Integer gasid) {
                return posiongasService.getById(gasid);
                }

        @GetMapping("/page")
        public Page<Posiongas> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String cas,
                                        @RequestParam(defaultValue = "") String gasnamezh,
                                        @RequestParam(defaultValue = "") String gasnameen) {
                QueryWrapper<Posiongas> queryWrapper = new QueryWrapper<>();
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
                return posiongasService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> gasids){
                return posiongasService.removeBatchByIds(gasids);
                }

        @PostMapping("/import")
        public Result imp(MultipartFile file) throws Exception {
                InputStream inputStream = file.getInputStream();
                ExcelReader reader = ExcelUtil.getReader(inputStream);
                // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//         List<User> list = reader.readAll(User.class);

                // 方式2：忽略表头的中文，直接读取表的内容
                List<List<Object>> list = reader.read(1); //忽略第一行
                List<Posiongas> posiongases = CollUtil.newArrayList();
                for (List<Object> row : list) {
                        Posiongas posiongas = new Posiongas();
                        posiongas.setCas(row.get(0).toString());
                        posiongas.setGasnamezh(row.get(1).toString());
                        posiongas.setGasnameen(row.get(2).toString());
                        posiongas.setMw(row.get(3).toString());
                        posiongas.setMac(row.get(4).toString());
                        posiongas.setPctwa(row.get(5).toString());
                        posiongas.setPcstel(row.get(6).toString());
                        posiongas.setIdlh(row.get(7).toString());
                        posiongas.setCahe(row.get(8).toString());
                        posiongases.add(posiongas);
                }
                posiongasService.saveBatch(posiongases);
                return Result.success(true);
        }

        /**
         * 导出接口
         */
        @GetMapping("/export")
        public void export(HttpServletResponse response) throws Exception {
                // 从数据库查询出所有的数据
                List<Posiongas> list = posiongasService.list();
                // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
                // 在内存操作，写出到浏览器
                ExcelWriter writer = ExcelUtil.getWriter(true);
                //自定义标题别名
//                writer.addHeaderAlias("cas", "化学标识符");

                // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
                writer.write(list, true);

                // 设置浏览器响应的格式
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                String fileName = URLEncoder.encode("有毒气体信息", "UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                out.close();
                writer.close();

        }

    }

