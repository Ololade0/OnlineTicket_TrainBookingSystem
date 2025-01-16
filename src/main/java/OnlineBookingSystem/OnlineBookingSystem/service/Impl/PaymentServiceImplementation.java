package OnlineBookingSystem.OnlineBookingSystem.service.Impl;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImplementation implements PaymentService {

	@Autowired
	private APIContext apiContext;

	public Payment createPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency(paymentRequest.getCurrency());
		amount.setTotal(String.valueOf(paymentRequest.getTotal()));

		Transaction transaction = new Transaction();
		transaction.setDescription(paymentRequest.getDescription());
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(paymentRequest.getPaymentMethod().toString());

		Payment newPayment = new Payment();
		newPayment.setIntent(paymentRequest.getIntent());
		newPayment.setPayer(payer);
		newPayment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(paymentRequest.getCancelUrl());
		redirectUrls.setReturnUrl(paymentRequest.getSuccessUrl());
		newPayment.setRedirectUrls(redirectUrls);
		return newPayment.create(apiContext);
	}


	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecution);
	}
}