//package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;
//
//import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
//import OnlineBookingSystem.OnlineBookingSystem.exceptions.ApprovalUrlNotFoundException;
//import OnlineBookingSystem.OnlineBookingSystem.exceptions.PaymentProcessingException;
//import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
//import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
//import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
//import OnlineBookingSystem.OnlineBookingSystem.model.User;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.*;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.Currency;
//import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
//import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
//import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
//import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PayPalServiceImpl implements PayPalService {
//
//
//    private final PaymentRepository paymentRepository;
//    private final BookingRepository bookingRepository;
//    private final SeatService seatService;
//
//
//
//
//    private final APIContext apiContext;
//
//    private final String cancelUrl = "http://localhost:8080/api/paypal/pay/cancel";
//    private final String successUrl = "http://localhost:8080/api/paypal/pay/success";
//
//
//
//    public String processPaypalPayment(Double totalFare, User user, Booking booking) {
//        String approvalUrl = null;
//        try {
//            // Prepare the payment request
//            PaymentRequest paymentRequest = new PaymentRequest();
//            paymentRequest.setCurrency(Currency.EUR);
//            paymentRequest.setTotal(totalFare);
//            paymentRequest.setPaymentMethod(PaymentMethod.paypal);
//            paymentRequest.setDescription("Train ticket booking");
//            paymentRequest.setIntent("sale");
//            paymentRequest.setCancelUrl(cancelUrl);
//            paymentRequest.setSuccessUrl(successUrl);
//
//            // Create PayPal Payment
//            Payment payment = createPaypalPayment(paymentRequest);
//            // Extract approval URL
//            approvalUrl = payment.getLinks().stream()
//                    .filter(link -> "approval_url".equals(link.getRel()))
//                    .findFirst()
//                    .map(com.paypal.api.payments.Links::getHref)
//                    .orElseThrow(() -> new ApprovalUrlNotFoundException("Approval URL not found in the payment response."));
//            String transactionReference = payment.getId();
//            booking.setBookingStatus(BookingStatus.PENDING);
//
//            BookingPayment bookingPayment = BookingPayment.builder()
//                    .totalPrice(totalFare)
//                    .paymentStatus(PaymentStatus.PENDING)
//                    .paymentMethod(PaymentMethod.paypal)
//                    .transactionReference(transactionReference)
//                    .currency(String.valueOf(paymentRequest.getCurrency()))
//                    .user(user)
//                    .booking(booking)
//                    .build();
//            booking.setBookingPayment(bookingPayment);
//           paymentRepository.save(bookingPayment);
//        } catch (PayPalRESTException e) {
//            log.info("Payment failed: " + e.getMessage());
//            throw new PaymentProcessingException("Payment Failed");
//        }
//
//        if (approvalUrl == null) {
//            throw new ApprovalUrlNotFoundException("Approval URL not found. Payment creation might have failed.");
//        }
//
//        return approvalUrl;
//    }
//
//    private Payment createPaypalPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
//        Amount amount = new Amount();
//        amount.setCurrency(String.valueOf(paymentRequest.getCurrency()));
//        amount.setTotal(String.valueOf(paymentRequest.getTotal()));
//
//        Transaction transaction = new Transaction();
//        transaction.setDescription(paymentRequest.getDescription());
//        transaction.setAmount(amount);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//
//        Payer payer = new Payer();
//        payer.setPaymentMethod(paymentRequest.getPaymentMethod().toString());
//
//        Payment newPayment = new Payment();
//        newPayment.setIntent(paymentRequest.getIntent());
//        newPayment.setPayer(payer);
//        newPayment.setTransactions(transactions);
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl(paymentRequest.getCancelUrl());
//        redirectUrls.setReturnUrl(paymentRequest.getSuccessUrl());
//        newPayment.setRedirectUrls(redirectUrls);
//        return newPayment.create(apiContext);
//    }
//
//    public Payment executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException {
//        Payment payment = new Payment();
//        payment.setId(paymentId);
//        PaymentExecution paymentExecution = new PaymentExecution();
//        paymentExecution.setPayerId(payerId);
//
//        Payment executedPayment = payment.execute(apiContext, paymentExecution);
//        log.info("Executed Payment State: " + executedPayment.getState());
//
//        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
//            BookingPayment bookingPayment = paymentRepository.findBytransactionReference(executedPayment.getId());
//
//            if (bookingPayment !=null ) {
//                bookingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
//                bookingPayment.setSuccessUrl(successUrl);
//                bookingPayment.setPaymentMethod(PaymentMethod.paypal);
//                bookingPayment.setPaymentDate(LocalDateTime.now());
//                paymentRepository.save(bookingPayment);
//
//
//                Booking booking = bookingPayment.getBooking();
//                booking.setBookingStatus(BookingStatus.BOOKED);
//                bookingRepository.save(booking);
//
//                // Book the seat
//                Seat bookedSeat = seatService.bookSeat(booking.getTrainClass().getClassName(), booking.getSeatNumber());
//                bookedSeat.setStatus(SeatStatus.BOOKED);
//                seatService.updateSeat(bookedSeat);
//
//
//                log.info("Payment status updated to COMPLETED.");
//            }
//
//            else {
//                log.error("Booking payment not found for transaction reference: " + executedPayment.getId());
//                throw new PaymentProcessingException("Payment not found for transaction reference: " + executedPayment.getId());
//            }
//        } else {
//            log.info("Payment was not successful: " + executedPayment.getState());
//            throw new PaymentProcessingException("Payment was not successful: " + executedPayment.getState());
//        }
//
//        return executedPayment;
//    }
//
//    public String handleCancelledPayment(String transactionReference) {
//        BookingPayment bookingPayment = paymentRepository.findBytransactionReference(transactionReference);
//
//        if (bookingPayment != null) {
//            bookingPayment.setPaymentStatus(PaymentStatus.CANCELLED);
//            bookingPayment.setCancelUrl(cancelUrl);
//            paymentRepository.save(bookingPayment);
//
//            Booking booking = bookingPayment.getBooking();
//            booking.setBookingStatus(BookingStatus.AVAILABLE);
//
//            bookingRepository.save(booking);
//
//
//            log.info("Payment was canceled. Booking status set to AVAILABLE.");
//            return "Payment successfully cancelled";
//        } else {
//            log.error("Booking payment not found for transaction reference: " + transactionReference);
//            throw new PaymentProcessingException("Payment not found for transaction reference: " + transactionReference);
//        }
//    }
//
//
//}


package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ApprovalUrlNotFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.PaymentProcessingException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.Currency;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayPalService;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final SeatService seatService;
    private final APIContext apiContext;

    private final String baseUrl = "http://localhost:8080/api/paypal/pay";
@Override
    public String processPaypalPayment(Double totalFare, User user, Booking booking) {
        try {
            // Prepare payment request
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setCurrency(Currency.EUR);
            paymentRequest.setTotal(totalFare);
            paymentRequest.setPaymentMethod(PaymentMethod.paypal);
            paymentRequest.setDescription("Train ticket booking");
            paymentRequest.setIntent("sale");

            // Create PayPal Payment
            Payment payment = createPaypalPayment(paymentRequest);

            // Extract approval URL
            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(com.paypal.api.payments.Links::getHref)
                    .orElseThrow(() -> new ApprovalUrlNotFoundException("Approval URL not found in the payment response."));

            String transactionReference = payment.getId();

            // Update booking details
            booking.setBookingStatus(BookingStatus.PENDING);

            BookingPayment bookingPayment = BookingPayment.builder()
                    .totalPrice(totalFare)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentMethod(PaymentMethod.paypal)
                    .transactionReference(transactionReference)
                    .currency(String.valueOf(paymentRequest.getCurrency()))
                    .user(user)
                    .booking(booking)
                    .build();

            booking.setBookingPayment(bookingPayment);
            paymentRepository.save(bookingPayment);

            return approvalUrl;
        } catch (PayPalRESTException e) {
            log.error("Payment failed: {}", e.getMessage());
            throw new PaymentProcessingException("Payment Failed");
        }
    }

    private Payment createPaypalPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(paymentRequest.getCurrency().toString());
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
        redirectUrls.setCancelUrl(baseUrl + "/cancel?transactionReference=" + newPayment.getId());
        redirectUrls.setReturnUrl(baseUrl + "/success?transactionReference=" + newPayment.getId());

        newPayment.setRedirectUrls(redirectUrls);
        return newPayment.create(apiContext);
    }
    @Override
    public Payment executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);
        log.info("Executed Payment State: {}", executedPayment.getState());

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            BookingPayment bookingPayment = paymentRepository.findBytransactionReference(executedPayment.getId());

            if (bookingPayment != null) {
                bookingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
                bookingPayment.setPaymentMethod(PaymentMethod.paypal);
                bookingPayment.setPaymentDate(LocalDateTime.now());
                paymentRepository.save(bookingPayment);

                Booking booking = bookingPayment.getBooking();
                booking.setBookingStatus(BookingStatus.BOOKED);
                bookingRepository.save(booking);

                // Book the seat
                Seat bookedSeat = seatService.bookSeat(booking.getTrainClass().getClassName(), booking.getSeatNumber());
                bookedSeat.setStatus(SeatStatus.BOOKED);
                seatService.updateSeat(bookedSeat);

                log.info("Payment status updated to COMPLETED.");
            } else {
                log.error("Booking payment not found for transaction reference: {}", executedPayment.getId());
                throw new PaymentProcessingException("Payment not found for transaction reference: " + executedPayment.getId());
            }
        } else {
            log.info("Payment was not successful: {}", executedPayment.getState());
            throw new PaymentProcessingException("Payment was not successful: " + executedPayment.getState());
        }

        return executedPayment;
    }
    @Override
    public String handleCancelledPayment(String transactionReference) {
        BookingPayment bookingPayment = paymentRepository.findBytransactionReference(transactionReference);

        if (bookingPayment != null) {
            bookingPayment.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(bookingPayment);

            Booking booking = bookingPayment.getBooking();
            booking.setBookingStatus(BookingStatus.AVAILABLE);
            bookingRepository.save(booking);

            log.info("Payment cancelled. Booking status set to AVAILABLE.");
            return "Payment sucessfully cancelled";
        } else {
            log.error("Booking payment not found for transaction reference: {}", transactionReference);
            throw new PaymentProcessingException("Booking payment not found for transaction reference: " + transactionReference);
        }
    }
}

