package com.charts.general.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Data;

@Data
public class EnumAdapter implements IEnum {

    private String value;
    private String systemValue;
    private Integer orderValue;

    public EnumAdapter(Integer value) {
        this.value = value.toString();
        this.orderValue = value;
    }

    public EnumAdapter(String value, Integer orderValue) {
        this.value = value;
        this.orderValue = orderValue;
    }

    public EnumAdapter(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

    public EnumAdapter(Boolean value) {
        this.value = value.toString();
        this.orderValue = value ? 1 : 0;
    }

}
