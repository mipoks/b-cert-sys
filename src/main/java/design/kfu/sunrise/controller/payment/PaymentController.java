package design.kfu.sunrise.controller.payment;

import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.dto.payment.PaymentDTO;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PageableAsQueryParam
    @Operation(description = "Get all own payments", summary = "Returns Page of payments")
    @GetMapping("/payments")
    public Page<PaymentDTO> getPayments(@Parameter(hidden = true) Pageable pageable,
                                        @AuthenticationPrincipal(expression = "account") Account account) {
        return paymentService.getPayments(account, pageable).map(PaymentDTO::from);
    }

    @Operation(description = "Make payment for club", summary = "Returns created payment in DTO")
    @PreAuthorize("@access.hasAccessToPayForClub(#account, #club)")
    @Limit(url = "/payment/club", limitType = LimitType.PER_HOUR)
    @PostMapping("/payment/{club_id}")
    public PaymentDTO makePayment(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("club_id") Club club) {
        return PaymentDTO.from(paymentService.makePayment(account, club));
    }

    @Operation(description = "Get payment info", summary = "Returns payment in DTO")
    @PreAuthorize("@access.hasAccessToPayment(#account, #payment)")
    @GetMapping("/payment/{payment_id}")
    public PaymentDTO getPayment(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("payment_id") Payment payment) {
        return PaymentDTO.from(payment);
    }

    @Operation(description = "Set payment as Sent", summary = "Returns payment in DTO")
    @PreAuthorize("@access.hasAccessToSendPayment(#account, #payment)")
    @PostMapping("/payment/{payment_id}/send")
    public PaymentDTO sendPayment(@PathVariable("payment_id") Payment payment, @AuthenticationPrincipal(expression = "account") Account account) {
        return PaymentDTO.from(paymentService.send(payment));
    }

    @Operation(description = "Set payment as Confirmed", summary = "Returns payment in DTO")
    @PreAuthorize("@access.hasAccessToConfirmPayment(#account, #payment)")
    @PostMapping("/payment/{payment_id}/confirm")
    public PaymentDTO confirmPayment(@PathVariable("payment_id") Payment payment, @AuthenticationPrincipal(expression = "account") Account account) {
        return PaymentDTO.from(paymentService.confirm(payment));
    }

    @Operation(description = "Set payment as Rejected", summary = "Returns payment in DTO")
    @PreAuthorize("@access.hasAccessToConfirmPayment(#account, #payment)")
    @PostMapping("/payment/{payment_id}/reject")
    public PaymentDTO rejectPayment(@PathVariable("payment_id") Payment payment, @AuthenticationPrincipal(expression = "account") Account account) {
        return PaymentDTO.from(paymentService.reject(payment));
    }

    @Operation(description = "Set paymnent as Refunded", summary = "Returns payment in DTO")
    @PreAuthorize("@access.hasAccessToRefundPayment(#account, #payment)")
    @PostMapping("/payment/{payment_id}/refund")
    public PaymentDTO refundPayment(@PathVariable("payment_id") Payment payment, @AuthenticationPrincipal(expression = "account") Account account) {
        return PaymentDTO.from(paymentService.refund(payment));
    }


    @Operation(description = "Get payment info by club", summary = "Returns payment in DTO")
    @GetMapping("/club/{club_id}/payment")
    public PaymentDTO getPayment(@AuthenticationPrincipal(expression = "account") Account account, @PathVariable("club_id") Club club) {
        Optional<Payment> paymentOptional = paymentService.findPaymentForClub(account, club);
        return paymentOptional.map(PaymentDTO::from).orElse(new PaymentDTO());
    }
}
