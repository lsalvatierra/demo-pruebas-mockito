package pe.edu.cibertec.demopruebasmockito.payment;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentServiceImpl {
    private final AccountRepository accountRepository;

    public PaymentServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        BigDecimal balance = accountRepository.getBalance(request.accountId());
        if (balance.compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar el pago");
        }

        String transactionId = UUID.randomUUID().toString();
        accountRepository.saveTransaction(request.accountId(), request.amount(), transactionId);

        return new PaymentResponse(transactionId, true);
    }


}
