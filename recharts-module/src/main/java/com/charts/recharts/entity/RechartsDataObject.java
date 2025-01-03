package com.charts.recharts.entity;

import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RechartsDataObject {

    @JsonIgnore
    private Integer upperOrderValue;
    @JsonIgnore
    private Integer lowerOrderValue;

    private String upperGroup;
    private String lowerGroup;
    private Integer value;

    public RechartsDataObject(String upperGroup, String lowerGroup, Integer value) {
        this.upperGroup = upperGroup;
        this.lowerGroup = lowerGroup;
        this.value = value;
    }

    public RechartsDataObject(IEnum upperGroup, Months lowerGroup, Integer value) {
        this.upperGroup = upperGroup.getValue();
        this.upperOrderValue = upperGroup.getOrderValue();
        this.lowerGroup = lowerGroup.getValue();
        this.value = value;
    }

    public RechartsDataObject(IEnum upperGroup, IEnum lowerGroup, Integer value) {
        this.upperGroup = upperGroup.getValue();
        this.upperOrderValue = upperGroup.getOrderValue();

        this.lowerGroup = lowerGroup.getValue();
        this.lowerOrderValue = lowerGroup.getOrderValue();

        this.value = value;
    }

}
