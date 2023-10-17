package com.hitwh.chemicalpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

//MybatisPlus默认使用实体类为表名进行查询
//@TableName(value = "user") 指定表名
@Data
@TableName("sys_user")
@ToString
public class User {
    @TableId(value = "id", type = IdType.AUTO) //告诉MP哪一个是ID，@TableField指定字段别名
    private Integer id;

    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
}