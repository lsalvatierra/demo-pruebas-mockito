package pe.edu.cibertec.demopruebasmockito.payment;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
