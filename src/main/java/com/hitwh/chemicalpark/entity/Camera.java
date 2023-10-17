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
 * @since 2023-04-25
 */
@Getter
@Setter
  public class Camera implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "cid", type = IdType.AUTO)
      private Integer cid;

    private String ip;

    private String cuser;

    private String cpsd;

    private Integer channelid;

    private Integer mode;

    private Integer quality;

    private Integer port;

    private Integer intersnap;

    private Integer imagesize;

    private Integer pid;

    private String devicemodel;

    private Boolean enable;
}
