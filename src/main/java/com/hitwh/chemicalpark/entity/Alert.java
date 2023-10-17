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
 * @since 2023-04-28
 */
@Getter
@Setter
  public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "aid", type = IdType.AUTO)
      private Integer aid;

    private Integer cid;

    private Integer pid;

    private Integer sid;

    private String alertlevel;

    private LocalDateTime alerttime;

    private String alerttype;

    private String alertdata;
}
