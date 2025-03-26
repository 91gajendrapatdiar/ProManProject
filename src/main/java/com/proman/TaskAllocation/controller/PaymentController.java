package com.proman.TaskAllocation.controller;

import com.proman.TaskAllocation.model.PlanType;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.response.PaymentLinkResponse;
import com.proman.TaskAllocation.services.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${rzp_key_id}")
    private String apiKey;

    @Value("${rzp_key_secret}")
    private String apiSecret;

    @Autowired
    private UserService userService;

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable PlanType planType,
                                                                 @RequestHeader("Authorization") String jwt)
            throws Exception {
        // Find user based on the JWT token
        User user = userService.findUserProfileByJwt(jwt);

        // Default amount
        int amount = 799 * 100;

        // Adjust the amount based on the plan type (Annual vs Monthly)
        if (planType == PlanType.ANNUALLY) {
            amount *= 12;
            amount = (int) (amount * 0.7); // Applying 30% discount for annual plan
        }

        try {
            // Initialize Razorpay client
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            // Prepare payment link request
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);

            // Adding phone notification (if required)
//            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
//                notify.put("sms", true);
//                customer.put("contact", user.getPhoneNumber());
//            }

            paymentLinkRequest.put("notify", notify);

            // Adding callback URL with the plan type
            paymentLinkRequest.put("callback_url", "http://localhost:5173/upgrade/success?planType=" + planType);

            // Create payment link
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            // Prepare response
            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_url(paymentLinkUrl);
            res.setPayment_link_id(paymentLinkId);

            // Return response with payment link details
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions and return appropriate error response
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
