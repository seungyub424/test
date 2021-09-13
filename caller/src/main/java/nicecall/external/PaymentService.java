package nicecall.external;

import nicecall.domain.PayType;
import nicecall.event.CallMade;
import nicecall.util.PaymentResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@FeignClient(name="payment", url="${api.url.payment}", fallback = PaymentServiceFallback.class)
public interface PaymentService {
    @RequestMapping(method= RequestMethod.POST, path="/payments/approve")
    public PaymentResult approve(@RequestBody HashMap<String, String> map);
}