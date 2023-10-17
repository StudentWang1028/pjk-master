package com.hitwh.chemicalpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author PJK
 * @since 2023-04-11
 */


@Data
@TableName("sys_file")
public class Files {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private Long size;

    private String url;

    private String md5;

    private Boolean isDelete;

    private Boolean enable;

}
