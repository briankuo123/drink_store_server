package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Coupon;
import com.mycompany.myapp.repository.CouponRepository;
import com.mycompany.myapp.service.dto.CouponDTO;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void createCoupon(CouponDTO couponDTO) {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon();
        Instant instantNow = Instant.now();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        coupon.setCouponId(couponId);
        coupon.setCouponCode(couponDTO.getCouponCode());
        coupon.setCouponValue(couponDTO.getCouponValue());
        coupon.setCouponUseTimes(couponDTO.getCouponUseTimes());
        coupon.setCouponExpireDatetime(couponDTO.getCouponExpireDateTime());
        coupon.setCreateBy(auth.getName());
        coupon.setCreateDatetime(instantNow);
        coupon.setLastModifiedBy(auth.getName());
        coupon.setLastModifiedDatetime(instantNow);
        couponRepository.save(coupon);
    }

    public void useCoupon(String couponCode) {
        Coupon coupon = couponRepository.getCouponByCouponCode(couponCode);
        if (coupon.getCouponUseTimes() >= 1) {
            coupon.setCouponUseTimes(coupon.getCouponUseTimes() - 1);
            couponRepository.save(coupon);
        } else {
            log.error("優惠碼已沒有使用次數");
        }
    }
}
