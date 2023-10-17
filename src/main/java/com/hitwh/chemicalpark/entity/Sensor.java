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
 * @since 2023-04-18
 */
@Getter
@Setter
  public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "sid", type = IdType.AUTO)
      private Integer sid;

    private Integer slaveadd;

    private String portname;

    private Integer pid;

    private String cas;

    private String devicemodel;

    private String sensortype;

    private Integer baudrate;

    private Integer databits;

    private Integer stopbits;

    private String parity;

    private Boolean enable;
}
