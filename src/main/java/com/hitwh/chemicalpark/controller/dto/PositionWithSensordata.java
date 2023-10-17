package com.hitwh.chemicalpark.controller.dto;

import com.hitwh.chemicalpark.entity.Position;
import com.hitwh.chemicalpark.entity.Sensordata;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class PositionWithSensordata implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sdid;

    private Integer pid;

    private String longitude;

    private String latitude;

    private String pname;

    private Integer sid;

    private String cas;

    private String datatype;

    private String sdata;

    private LocalDateTime datatime;
}
