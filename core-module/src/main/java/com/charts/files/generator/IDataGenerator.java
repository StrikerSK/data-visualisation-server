package com.charts.files.generator;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;

import java.util.List;

public interface IDataGenerator {

    /**
     * Method should generate coupons for the output
     * @param counts Number of coupons to be generated
     * @return List of coupons generated
     */
    List<UpdateCouponEntity> generateCoupons(Integer counts);

    /**
     * Method should generate coupons for the output
     * @param counts Number of tickets to be generated
     * @return List of tickets generated
     */
    List<UpdateTicketEntity> generateTickets(Integer counts);

}
