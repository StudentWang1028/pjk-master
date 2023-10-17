package com.hitwh.chemicalpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author PJK
 * @since 2023-05-02
 */
@Getter
@Setter
  public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "pid", type = IdType.AUTO)
      private Integer pid;

    private String longitude;

    private String latitude;

    private String pname;
}
