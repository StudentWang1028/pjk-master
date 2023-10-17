package com.hitwh.chemicalpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author PJK
 * @since 2023-04-12
 */
@Getter
@Setter
  public class Sensordata implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "sdid", type = IdType.AUTO)
      private Integer sdid;

    private Integer sid;

    private Integer pid;

    private String cas;

    private String datatype;

    private String sdata;

    private LocalDateTime datatime;
}
