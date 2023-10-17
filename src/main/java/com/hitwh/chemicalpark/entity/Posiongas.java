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
 * @since 2023-04-09
 */
@Getter
@Setter
  public class Posiongas implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "gasid", type = IdType.AUTO)
      private Integer gasid;

    private String cas;

    private String gasnamezh;

    private String gasnameen;

    private String mw;

    private String mac;

    private String pctwa;

    private String pcstel;

    private String idlh;

    private String cahe;
}
