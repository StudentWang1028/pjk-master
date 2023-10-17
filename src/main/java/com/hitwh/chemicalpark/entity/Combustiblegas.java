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
 * @since 2023-04-10
 */
@Getter
@Setter
  public class Combustiblegas implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "gasid", type = IdType.AUTO)
      private Integer gasid;

    private String cas;

    private String gasnamezh;

    private String gasnameen;

    private String flashpoint;

    private String lel;

    private String uel;

    private String mw;

    private String boilingpoint;

    private String danger;

    private String steamdensity;
}
