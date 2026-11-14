package pe.edu.cibertec.demopruebasmockito.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        Long accountId,
        BigDecimal amount) {}
