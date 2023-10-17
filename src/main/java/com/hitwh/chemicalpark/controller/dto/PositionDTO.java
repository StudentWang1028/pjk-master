package com.hitwh.chemicalpark.controller.dto;

import com.hitwh.chemicalpark.entity.Position;
import lombok.Data;

@Data
public class PositionDTO {
    private Float plng;  // 新的属性名
    private Float plat;  // 新的属性名
    private String pname;  // 新的属性名\
    private Integer  pid;

    // 在构造函数中转换属性名
    public PositionDTO(Position position) {
        this.plng = Float.parseFloat(position.getLongitude());
        this.plat = Float.parseFloat(position.getLatitude());
        this.pname = position.getPname();
        this.pid = position.getPid();
    }
}