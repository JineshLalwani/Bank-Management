package banking.management.service;

import banking.management.dto.PaymentAccountDTO;
import banking.management.model.Payment1;

import java.util.List;

public interface PaymentService {
    PaymentAccountDTO createPayment(PaymentAccountDTO paymentAccountDTO);
    PaymentAccountDTO getPaymentById(Long id);
    List<Payment1> getPaymentHistory(Long paymentId);
}
