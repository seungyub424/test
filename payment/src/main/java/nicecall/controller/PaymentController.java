package nicecall.controller;

import nicecall.domain.PayType;
import nicecall.domain.Payment;
import nicecall.domain.PaymentRepository;
import nicecall.util.PaymentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    @RequestMapping(value = "/payments/approve",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public PaymentResult approve(@RequestBody HashMap<String, String> map) {

        PaymentResult pr = new PaymentResult();
        try {
            String callId    = this.getParam(map, "callId", true);
            String payType   = this.getParam(map, "payType", true);
            String payAmount = this.getParam(map, "payAmount", false);
            String mobile    = this.getParam(map, "mobile", true);

            // Circuit break 테스트를 위해 일부러 sleep을 발생시킨다.
            if (payType.equals("TMONEY")) {
                System.out.println("<<<<< ------------ SLEEPING (4 seconds) for Hystrix Test ------------ >>>>> ");
                Thread.sleep(4000);
                pr.setResultCode(-2L);
                pr.setResultMessage("TMONEY 결제시 Timeout이 발생되었습니다.");
                return pr;
            }

            Payment payment = Payment.approve(
                    PayType.valueOf(payType),
                    Integer.valueOf(payAmount),
                    Long.valueOf(callId)
            );
            paymentRepository.save(payment);

            pr.setResultCode(1L);
            pr.setResultMessage(String.valueOf(payment.getPaymentId()));
            return pr;

        } catch (Exception e) {
            System.out.println("<<<<< Sorry. Cannot make payment entity >>>>> ");
            System.out.println(e.getMessage());

            pr.setResultCode(-1L);
            pr.setResultMessage(e.getMessage());
            return pr;
        }


    }

    // POST, PUT, PATCH, DELETE method를 제한하고 GET만 허용한다.
    @GetMapping(value = "/payments/{id}",
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(Long.valueOf(id));

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private String getParam(HashMap<String, String> map, String paramKey, Boolean required) {
        String paramVal = "";

        for (String key : map.keySet()) {
            if (key.equalsIgnoreCase(paramKey)) {
                paramVal = map.get(key);
                break;
            }
        }

        if (required && paramVal.equals("")) {
            throw new InvalidParameterException("@@@ PARAMETER (" + paramKey +") is Missing !! @@@");
        }

        System.out.println(" <<< PARSING PARAMS ('" + paramKey + "') : " + paramVal + " ###");

        return paramVal;
    }
}
