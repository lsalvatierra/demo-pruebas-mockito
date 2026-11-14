package pe.edu.cibertec.demopruebasmockito.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Debe lanzar Excepcion cuando el cupón no existe")
    void testDebeLanzarExcepcionCuandoCuponNoExiste() {
        String cupon = "PROM050";
        when(couponRepository.findByCode(cupon))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() ->
                cartService.applyCoupon(
                        new BigDecimal("100.00"), cupon))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining("no existe");
    }


    @Test
    @DisplayName("Debe lanzar Excepcion cuando el cupón está expirado")
    void testDebeLanzarExcepcionCuandoCuponEstaExpirado() {
        String cupon = "CADUCADO";
        Coupon cuponExpirado = new Coupon(cupon,
                new BigDecimal("10"),
                LocalDate.now().minusDays(1));
        when(couponRepository.findByCode(cupon))
                .thenReturn(Optional.of(cuponExpirado));

        assertThatThrownBy(() ->
                cartService.applyCoupon(
                        new BigDecimal("100.00"), cupon))
                .isInstanceOf(CouponExpiredException.class)
                .hasMessageContaining("expirado");
    }

    @Test
    @DisplayName("Debe retornar el total a pagar aplicando descuento")
    void testDebeRetornarTotalAPagarAplicandoDescuento() {
        String cupon = "DESCUENTO20";
        Coupon cuponValido = new Coupon(cupon,
                new BigDecimal("20.00"),
                LocalDate.now().plusDays(5));
        when(couponRepository.findByCode(cupon))
                .thenReturn(Optional.of(cuponValido));

        BigDecimal totalAPagar = cartService.applyCoupon(
                new BigDecimal("200.00"), cupon);

        assertThat(totalAPagar).isEqualByComparingTo(
                new BigDecimal("160.00"));
    }

}
