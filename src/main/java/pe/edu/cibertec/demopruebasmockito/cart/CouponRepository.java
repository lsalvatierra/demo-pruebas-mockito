package pe.edu.cibertec.demopruebasmockito.cart;

import java.util.Optional;

interface CouponRepository {
    Optional<Coupon> findByCode(String code);
}

