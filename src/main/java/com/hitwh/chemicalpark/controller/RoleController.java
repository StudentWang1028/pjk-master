package com.hitwh.chemicalpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hitwh.chemicalpark.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitwh.chemicalpark.service.IRoleService;
import com.hitwh.chemicalpark.entity.Role;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author PJK
 * @since 2023-04-11
 */
@RestController
@RequestMapping("/role")
public class RoleController {

        @Resource
        private IRoleService roleService;

        // 新增或者更新
        @PostMapping
        public Result save(@RequestBody Role role) {
                roleService.saveOrUpdate(role);
                return Result.success();
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
                roleService.removeById(id);
                return Result.success();
        }

        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                roleService.removeByIds(ids);
                return Result.success();
        }

        @GetMapping
        public Result findAll() {
                return Result.success(roleService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(roleService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam String name,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
                QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
                queryWrapper.like("name", name);
                queryWrapper.orderByDesc("id");
                return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

        /**
         * 绑定角色和菜单的关系
         * @param roleId 角色id
         * @param menuIds 菜单id数组
         * @return
         */
//        @PostMapping("/roleMenu/{roleId}")
//        public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
//                roleService.setRoleMenu(roleId, menuIds);
//                return Result.success();
//        }
//
//        @GetMapping("/roleMenu/{roleId}")
//        public Result getRoleMenu(@PathVariable Integer roleId) {
//                return Result.success( roleService.getRoleMenu(roleId));
//        }

}