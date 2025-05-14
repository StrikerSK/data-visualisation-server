package com.charts.files.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.enums.types.SellType;
import com.charts.api.coupon.enums.types.Validity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.enums.TicketType;
import com.charts.files.conditions.FileCondition;
import com.charts.general.entity.enums.EnumUtils;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Conditional(FileCondition.class)
public class DefaultDataGenerator implements IDataGenerator {

    public List<UpdateCouponEntity> generateCoupons(Integer counts) {
        List<UpdateCouponEntity> enitityList = new java.util.ArrayList<>();

        for (int i = 0; i < counts; i++) {
            UpdateCouponEntity coupon = UpdateCouponEntity.builder()
                    .year(generateYear())
                    .month(getRandomElement(Months.class))
                    .personType(getRandomElement(PersonType.class))
                    .validity(getRandomElement(Validity.class))
                    .sellType(getRandomElement(SellType.class))
                    .value(generateValue())
                    .build();
            enitityList.add(coupon);
        }

        return enitityList;
    }

    public List<UpdateTicketEntity> generateTickets(Integer counts) {
        List<UpdateTicketEntity> enitityList = new java.util.ArrayList<>();
        List<Boolean> discounted = Stream.of(true, false).collect(Collectors.toList());

        for (int i = 0; i < counts; i++) {
            UpdateTicketEntity ticket = UpdateTicketEntity.builder()
                    .year(generateYear())
                    .month(getRandomElement(Months.class))
                    .ticketType(getRandomElement(TicketType.class))
                    .discounted(getRandomElement(discounted))
                    .value(generateValue())
                    .build();
            enitityList.add(ticket);
        }

        return enitityList;
    }

    private static <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    private static <T extends IEnum> T getRandomElement(Class<T> clazz) {
        List<T> valueList = EnumUtils.getValueList(clazz);
        return getRandomElement(valueList);
    }

    private static Integer generateYear() {
        Random random = new Random();
        return random.nextInt(15) + 2010;
    }

    private static Integer generateValue() {
        Random random = new Random();
        return random.nextInt(100000);
    }

}
