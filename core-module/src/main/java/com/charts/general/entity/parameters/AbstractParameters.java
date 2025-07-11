package com.charts.general.entity.parameters;

import com.charts.general.entity.enums.EnumUtils;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.charts.general.entity.constants.EnumerationConstants.YEAR_LIST;

@SuperBuilder
public abstract class AbstractParameters {

    private final List<String> month;
    private final List<Integer> year;
//    private final String upperGroup;
//    private final String lowerGroup;
//    private final String group;

    protected AbstractParameters(List<String> month, List<Integer> year) {
        this.month = month;
        this.year = year;
    }

    public List<Months> getMonths() {
        return getValueList(month, Months.class);
    }

    public List<Integer> getYearInteger() {
        if (CollectionUtils.isEmpty(year)) {
            return YEAR_LIST;
        }

        return year;
    }

    protected  <T extends IEnum> List<T> getValueList(List<String> searchedValues, Class<T> clazz) {
        if (CollectionUtils.isEmpty(searchedValues)) {
            return EnumUtils.getValueList(clazz);
        } else {
            return EnumUtils.getValueList(searchedValues, clazz);
        }
    }
}
