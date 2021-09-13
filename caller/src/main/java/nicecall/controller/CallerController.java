package nicecall.controller;

import nicecall.CallerApplication;
import nicecall.domain.Caller;
import nicecall.domain.CallerRepository;
import nicecall.domain.CallerStatus;
import nicecall.exception.PaymentException;
import nicecall.util.PaymentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class CallerController {

    @Autowired
    CallerRepository callerRepository;
//    CallerService callerService;

    @RequestMapping(value = "/calls/payCall/{callId}",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public void payCall(@PathVariable Long callId) throws Exception {

        Optional<Caller> caller = callerRepository.findById(callId);

        if (!caller.isPresent()) {
            throw new InvalidParameterException("<<< 대상 콜을 찾을 수 없습니다 (Wrong callerId) >>>");
        }
        Caller theCaller = caller.get();

        if (theCaller.getStatus() == CallerStatus.APPROVED) {
            throw new RuntimeException("<<< 해당 콜은 이미 결제된 상태입니다. >>>");
        }

        HashMap<String, String > map = new HashMap<String, String>();
        map.put("callId",    String.valueOf(theCaller.getCallId()));
        map.put("mobile",    theCaller.getMobile());
        map.put("payType",   String.valueOf(theCaller.getPayType()));
        map.put("payAmount", String.valueOf(theCaller.getPayAmount()));

        // PaymentService에게 승인을 요청한다.
        // PaymentService 호출에 실패할 경우 -2를 리턴받고, PaymetService 자체에서 오류가 날 경우 -1을 리턴한다.
        PaymentResult pr = CallerApplication.applicationContext.getBean(nicecall.external.PaymentService.class)
                .approve(map);

        System.out.println("### PaymentService.process() returns : " + pr);

        // Payment에 실패한 경우 Exception 처리
        if (pr.getResultCode().equals(-2L)) {
            throw new PaymentException("<<< PaymentService : No-Response or Timed-out. please, try later... >>>");
        } else if (pr.getResultCode().equals(-1L)) {
            throw new PaymentException("<<< PaymentService : 결제 처리에 실패하였습니다. :: " + pr.getResultMessage() + " >>>");
        } else {
            // 성공할 경우 ResultCode가 paymentId이다.
            theCaller.setStatus(CallerStatus.APPROVED);
            theCaller.setPaymentId(pr.getResultCode());
            callerRepository.save(theCaller);
        }

    }


}
