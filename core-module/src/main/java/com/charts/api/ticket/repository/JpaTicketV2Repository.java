package com.charts.api.ticket.repository;

import com.charts.general.entity.GroupingEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.entity.enums.types.Months;
import com.charts.api.ticket.entity.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTicketV2Repository extends JpaRepository<UpdateTicketEntity, Long> {

    List<UpdateTicketEntity> findAllByMonthInAndYearInAndTicketTypeInAndDiscountedIn(
            List<Months> monthList,
            List<Integer> yearList,
            List<TicketType> ticketTypeList,
            List<Boolean> discountedList
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.ticketType, SUM(c.value)) "
                    + "FROM UpdateTicketEntity AS c "
                    + "WHERE c.month IN ?1 AND c.discounted IN ?2 AND c.ticketType IN ?3 AND c.year IN ?4 "
                    + "GROUP BY c.ticketType"
    )
    List<GroupingEntity<TicketType>> findGroupedByTicketType (
            List<Months> months,
            List<Boolean> discounted,
            List<TicketType> ticketTypes,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.discounted, SUM(c.value)) "
                    + "FROM UpdateTicketEntity AS c "
                    + "WHERE c.month IN ?1 AND c.discounted IN ?2 AND c.ticketType IN ?3 AND c.year IN ?4 "
                    + "GROUP BY c.discounted"
    )
    List<GroupingEntity<Boolean>> findGroupedByDiscounted (
            List<Months> months,
            List<Boolean> discounted,
            List<TicketType> ticketTypes,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.year, SUM(c.value)) "
                    + "FROM UpdateTicketEntity AS c "
                    + "WHERE c.month IN ?1 AND c.discounted IN ?2 AND c.ticketType IN ?3 AND c.year IN ?4 "
                    + "GROUP BY c.year"
    )
    List<GroupingEntity<Integer>> findGroupedByYear (
            List<Months> months,
            List<Boolean> discounted,
            List<TicketType> ticketTypes,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.month, SUM(c.value)) "
                    + "FROM UpdateTicketEntity AS c "
                    + "WHERE c.month IN ?1 AND c.discounted IN ?2 AND c.ticketType IN ?3 AND c.year IN ?4 "
                    + "GROUP BY c.month"
    )
    List<GroupingEntity<Months>> findGroupedByMonth (
            List<Months> months,
            List<Boolean> discounted,
            List<TicketType> ticketTypes,
            List<Integer> years
    );

}
