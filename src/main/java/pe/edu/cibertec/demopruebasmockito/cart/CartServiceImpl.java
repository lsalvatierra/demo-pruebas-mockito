package pe.edu.cibertec.demopruebasmockito.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CartServiceImpl {
    private final CouponRepository couponRepository;

    public CartServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public BigDecimal applyCoupon(BigDecimal originalTotal, String couponCode) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new CouponNotFoundException("El cupón no existe"));

        if (coupon.expirationDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("El cupón ha expirado");
        }

        BigDecimal discountFactor = BigDecimal.ONE.subtract(
                coupon.discountPercentage().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
        );

        return originalTotal.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }

}

