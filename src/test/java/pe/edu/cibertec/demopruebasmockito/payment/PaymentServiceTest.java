package pe.edu.cibertec.demopruebasmockito.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    //Clonando AccountRepository, para simular funciones
    @Mock
    private AccountRepository accountRepository;
    //Creando el escenario PaymentServiceImpl para simular funciones del repository
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Debe lanzar la excepcion de saldo insuficiente")
    void testDebeLanzarExcepcionDeSaldoInsuficiente() {
        Long accountId = 1L;
        BigDecimal montoAPagar = new BigDecimal("100.00");
        BigDecimal montoSaldo = new BigDecimal("50.00");
        PaymentRequest request = new PaymentRequest(accountId,
                montoAPagar);
        //Siempre el when
        when(accountRepository.getBalance(accountId)).thenReturn(montoSaldo);

        assertThatThrownBy(() ->
                paymentService.processPayment(request))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Saldo insuficiente");
        //Siempre el verify
        verify(accountRepository, never()).saveTransaction(any(),
                any(), any());
    }

    @Test
    @DisplayName("Validar si la cuenta tiene fondos generar la transaccion")
    void testValidarSiCuentaTieneFondosGenerarLaTransaccion() {
        Long accountId = 1L;
        BigDecimal montoAPagar = new BigDecimal("100.00");
        BigDecimal saldoActual = new BigDecimal("150.00");
        PaymentRequest request = new PaymentRequest(accountId,
                montoAPagar);
        //Cuando se ejecute el metodo getBalance con el
        // ID=1L retornamos el saldoActual
        when(accountRepository.getBalance(accountId)).thenReturn(saldoActual);

        PaymentResponse response = paymentService
                .processPayment(request);

        assertThat(response).isNotNull();
        assertThat(response.success()).isTrue();
        assertThat(response.transactionId()).isNotBlank();
        //Verificar que el metodo saveTransaction se ejecutó
        //al menos una vez con la coindicencia de parámetros.
        verify(accountRepository, times(1))
                .saveTransaction(eq(accountId),
                        eq(montoAPagar), anyString());

    }

}
