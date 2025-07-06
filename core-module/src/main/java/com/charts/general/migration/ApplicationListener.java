package com.charts.general.migration;

import com.charts.api.coupon.entity.v1.CouponEntityV1;
import com.charts.api.coupon.repository.JpaCouponRepository;
import com.charts.api.coupon.repository.JpaCouponV2Repository;
import com.charts.api.coupon.utils.CouponConvertor;
import com.charts.api.ticket.entity.v1.TicketEntityV1;
import com.charts.api.ticket.repository.JpaTicketRepository;
import com.charts.api.ticket.repository.JpaTicketV2Repository;
import com.charts.api.ticket.utils.TicketConverter;
import com.charts.general.conditions.MigrationCondition;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Conditional(MigrationCondition.class)
@AllArgsConstructor
public class ApplicationListener {

    private final JpaCouponRepository couponRepository;
    private final JpaCouponV2Repository couponV2Repository;
    private final JpaTicketRepository ticketRepository;
    private final JpaTicketV2Repository ticketV2Repository;

    @EventListener
    public void onApplicationEvent(SpringApplicationEvent event) {
        List<CouponEntityV1> coupons = couponRepository.findAll();
        couponV2Repository.saveAll(CouponConvertor.convertCouponEntity(coupons));

        List<TicketEntityV1> tickets = ticketRepository.findAll();
        ticketV2Repository.saveAll(TicketConverter.convertTicketEntity(tickets));
    }

}
