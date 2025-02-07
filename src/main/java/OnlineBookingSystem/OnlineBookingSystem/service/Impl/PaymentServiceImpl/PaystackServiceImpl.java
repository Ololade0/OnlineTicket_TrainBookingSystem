package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Service
public class PaystackServiceImpl implements PayStackService {


    @Value("${paystack.secret.key}")
    private String secretKey;

    @Value("${paystack.base.url}")
    private String baseUrl;

    private final BookingRepository bookingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    // PAYSTACK PAYMENT INTEGRATION
    public String processPaystackPayment(String email, Double amount) throws IOException, InterruptedException {
        String url = baseUrl + "/transaction/initialize";

        // Create Request Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("amount", amount * 100);

        // Convert payload to JSON
        String requestBody = objectMapper.writeValueAsString(payload);

        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + secretKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == HttpStatus.OK.value()) {
            return response.body();
        } else {
            return "Error: " + response.statusCode();
        }
    }

    public String verifyPaystackPaymentTransaction(String reference) throws IOException {
        String url = baseUrl + "/transaction/verify/" + reference;
        log.info("Verifying Paystack payment with reference: {}", reference);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + secretKey);
            request.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(request)) {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
                log.info("Paystack verification response: {}", jsonResponse);

                if (response.getCode() == HttpStatus.OK.value()) {
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                    String status = jsonNode.get("data").get("status").asText();

                    if ("success".equalsIgnoreCase(status)) {
                        String email = jsonNode.get("data").get("customer").get("email").asText();
                        Booking booking = bookingRepository.findByUser_Email(email);
                        if (booking != null) {
                            booking.getBookingPayment().setPaymentStatus(PaymentStatus.COMPLETED);
                            bookingRepository.save(booking);
                            log.info("Booking updated to PAID for user: {}", email);
                        }
                        return "Payment Successful";
                    } else {
                        return "Payment Verification Failed: Payment was not successful";
                    }
                } else {
                    return "Error: Unable to verify payment, response code " + response.getCode();
                }
            }
        }
    }

}
