package pe.edu.cibertec.demopruebasmockito.payment;

public record PaymentResponse(
        String transactionId,
        boolean success) {}
