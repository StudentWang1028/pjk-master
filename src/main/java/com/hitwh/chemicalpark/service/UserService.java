package com.hitwh.chemicalpark.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hitwh.chemicalpark.common.Constants;
import com.hitwh.chemicalpark.controller.dto.UserDTO;
import com.hitwh.chemicalpark.entity.User;
import com.hitwh.chemicalpark.exception.ServiceException;
import com.hitwh.chemicalpark.mapper.UserMapper;
import com.hitwh.chemicalpark.util.TokenUtil;
import org.springframework.stereotype.Service;

//如果导入导出有alias别名，要加上别名
@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    public boolean saveUser(User user){
        return saveOrUpdate(user);
    }

    private static final Log LOG = Log.get();

    public UserDTO login(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try{
            one = getOne(queryWrapper);
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }

        if(one != null){
            BeanUtil.copyProperties(one,userDTO,true);
            String token = TokenUtil.genToken(one.getId().toString(),one.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    public User register(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        User one;
        try{
            one = getOne(queryWrapper);
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }

        if(one == null){
            one = new User();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);
            return one;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}
