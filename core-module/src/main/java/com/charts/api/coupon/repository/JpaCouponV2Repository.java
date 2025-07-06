package com.charts.api.coupon.repository;

import com.charts.general.entity.GroupingEntity;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.enums.types.Months;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaCouponV2Repository extends JpaRepository<UpdateCouponEntity, Long> {

    List<UpdateCouponEntity> findAllByPersonTypeInAndValidityInAndSellTypeInAndMonthInAndYearIn (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.personType, SUM(c.value)) "
            + "FROM UpdateCouponEntity AS c "
            + "WHERE c.personType IN ?1 AND c.validity IN ?2 AND c.sellType IN ?3 AND c.month IN ?4 AND c.year IN ?5 "
            + "GROUP BY c.personType"
    )
    List<GroupingEntity<PersonType>> findGroupedByPersonTypeValues (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.month, SUM(c.value)) "
                    + "FROM UpdateCouponEntity AS c "
                    + "WHERE c.personType IN ?1 AND c.validity IN ?2 AND c.sellType IN ?3 AND c.month IN ?4 AND c.year IN ?5 "
                    + "GROUP BY c.month"
    )
    List<GroupingEntity<Months>> findGroupedByMonthValues (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.validity, SUM(c.value)) "
                    + "FROM UpdateCouponEntity AS c "
                    + "WHERE c.personType IN ?1 AND c.validity IN ?2 AND c.sellType IN ?3 AND c.month IN ?4 AND c.year IN ?5 "
                    + "GROUP BY c.validity"
    )
    List<GroupingEntity<Validity>> findGroupedByValidityValues (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.sellType, SUM(c.value)) "
                    + "FROM UpdateCouponEntity AS c "
                    + "WHERE c.personType IN ?1 AND c.validity IN ?2 AND c.sellType IN ?3 AND c.month IN ?4 AND c.year IN ?5 "
                    + "GROUP BY c.sellType"
    )
    List<GroupingEntity<SellType>> findGroupedBySellTypeValues (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

    @Query(
            "SELECT new com.charts.general.entity.GroupingEntity(c.year, SUM(c.value)) "
                    + "FROM UpdateCouponEntity AS c "
                    + "WHERE c.personType IN ?1 AND c.validity IN ?2 AND c.sellType IN ?3 AND c.month IN ?4 AND c.year IN ?5 "
                    + "GROUP BY c.year"
    )
    List<GroupingEntity<Integer>> findGroupedByYearValues (
            List<PersonType> personTypes,
            List<Validity> validityList,
            List<SellType> sellTypes,
            List<Months> months,
            List<Integer> years
    );

}
