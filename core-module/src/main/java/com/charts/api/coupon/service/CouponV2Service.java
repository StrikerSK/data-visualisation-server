package com.charts.api.coupon.service;

import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.general.entity.GroupingEntity;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.repository.JpaCouponV2Repository;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CouponV2Service {

    private final JpaCouponV2Repository couponRepository;

    public List<UpdateCouponEntity> findAll(Integer size) {
        Pageable pageable = PageRequest.ofSize(size);
        return couponRepository.findAll(pageable).getContent();
    }

    public void saveAll(List<UpdateCouponEntity> couponEntityList) { couponRepository.saveAll(couponEntityList); }

    public List<UpdateCouponEntity> findCouponEntities(CouponsParameters couponsParameters) {
        return couponRepository.findAllByPersonTypeInAndValidityInAndSellTypeInAndMonthInAndYearIn(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        );
    }

    public List<GroupingEntity<Months>> findByValidityAndGroupedByMonth(CouponsParameters couponsParameters) {
        return couponRepository.findGroupedByMonthValues(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        );
    }

    public List<GroupingEntity<PersonType>> findByValidityAndGroupedByPersonType(CouponsParameters couponsParameters) {
        return couponRepository.findGroupedByPersonTypeValues(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        );
    }

    public List<GroupingEntity<Validity>> findByValidityAndGroupedByValidity(CouponsParameters couponsParameters) {
        return couponRepository.findGroupedByValidityValues(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        );
    }

    public List<GroupingEntity<SellType>> findByValidityAndGroupedBySellType(CouponsParameters couponsParameters) {
        return couponRepository.findGroupedBySellTypeValues(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        );
    }

    public List<GroupingEntity<EnumAdapter>> findByValidityAndGroupedByYear(CouponsParameters couponsParameters) {
        return couponRepository.findGroupedByYearValues(
                couponsParameters.getPersonTypeList(),
                couponsParameters.getValidity(),
                couponsParameters.getSellTypes(),
                couponsParameters.getMonths(),
                couponsParameters.getYearInteger()
        )
                .stream()
                .map(e -> new GroupingEntity<>(new EnumAdapter(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
    }

}
