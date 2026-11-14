package pe.edu.cibertec.demopruebasmockito.payment;

import java.math.BigDecimal;

interface AccountRepository {
    BigDecimal getBalance(Long accountId);
    void saveTransaction(Long accountId, BigDecimal amount, String transactionId);
}

