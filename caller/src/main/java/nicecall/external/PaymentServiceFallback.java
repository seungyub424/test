package nicecall.external;

import nicecall.event.CallMade;
import nicecall.util.PaymentResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;


@Component
public class PaymentServiceFallback implements PaymentService {
    @Override
    public PaymentResult approve(@RequestBody HashMap<String, String> map) {

        System.out.println("### Circuit Breaker has been opened. Fallback returned instead ###");

        PaymentResult pr = new PaymentResult();
        pr.setResultCode(-2L);
        pr.setResultMessage("### Circuit Breaker has been opened. Fallback returned instead ###");

        return pr;
    }

}