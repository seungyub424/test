package nicecall;

import lombok.extern.slf4j.Slf4j;
import nicecall.config.kafka.KafkaProcessor;


import nicecall.domain.Dashboard;
import nicecall.domain.DashboardRepository;
import nicecall.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
@Slf4j
public class PolicyHandler{
    @Autowired
    DashboardRepository dashboardRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever(@Payload String str){
        log.info ("DashboardHandler Listening ..... " + str);
    }

    // 대시보드를 생성한다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallMade_inputRecord(@Payload CallMade callMade){
        if(!callMade.validate()) return;

        System.out.println("\n\n##### listener Dashboard callMade : " + callMade.toJson() + "\n\n");

        String extraIdName = "";
        Long   extraIdValue = null;
        String description = " mobile=" + callMade.getMobile() +
                            ", location=" + callMade.getLocation() +
                            ", payType=" + callMade.getPayType() +
                            ", payAmount=" + callMade.getPayAmount() ;
        inputRecord(callMade, extraIdName, extraIdValue, description);
    }


    // Payment가 승인되면 Event를 수신한다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_inputRecord(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener Dashboard paymentApproved : " + paymentApproved.toJson() + "\n\n");

        String extraIdName = "paymentId";
        Long   extraIdValue = paymentApproved.getPaymentId();
        String description = "" ;
        inputRecord(paymentApproved, extraIdName, extraIdValue, description);

    }

    // 콜 접수 대시보드 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallReceived_inputRecord(@Payload CallReceived callReceived){
        if(!callReceived.validate()) return;

        System.out.println("\n\n##### listener Dashboard callReceived : " + callReceived.toJson() + "\n\n");

        String extraIdName = "";
        Long   extraIdValue = null;
        String description = " mobile=" + callReceived.getMobile() +
                ", location=" + callReceived.getLocation() + " : 콜 접수 대기중입니다. ";
        inputRecord(callReceived, extraIdName, extraIdValue, description);
    }

    // 콜 거절 대시보드 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCatchDenied_inputRecord(@Payload CatchDenied catchDenied){
        if(!catchDenied.validate()) return;

        System.out.println("\n\n##### listener Dashboard catchDenied : " + catchDenied.toJson() + "\n\n");

        String extraIdName = "";
        Long   extraIdValue = null;
        String description = " mobile=" + catchDenied.getMobile() +
                ", location=" + catchDenied.getLocation() + " : 서비스 불가 지역입니다."  ;
        inputRecord(catchDenied, extraIdName, extraIdValue, description);
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallCancelled_inputRecord(@Payload CallCancelled callCancelled){
        if(!callCancelled.validate()) return;

        System.out.println("\n\n##### listener Dashboard callCancelled : " + callCancelled.toJson() + "\n\n");

        String extraIdName = "callId";
        Long   extraIdValue = callCancelled.getCallId();
        String description = "" ;
        inputRecord(callCancelled, extraIdName, extraIdValue, description);

    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCancelled_inputRecord(@Payload PaymentCancelled paymentCancelled){
        if(!paymentCancelled.validate()) return;

        System.out.println("\n\n##### listener Dashboard paymentCancelled : " + paymentCancelled.toJson() + "\n\n");

        String extraIdName = "paymentId";
        Long   extraIdValue = paymentCancelled.getPaymentId();
        String description = "사용자에 의해 결제가 취소되었습니다." ;
        inputRecord(paymentCancelled, extraIdName, extraIdValue, description);

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentDisabled_inputRecord(@Payload PaymentDisabled paymentDisabled){
        if(!paymentDisabled.validate()) return;

        System.out.println("\n\n##### listener Dashboard paymentDisabled : " + paymentDisabled.toJson() + "\n\n");

        String extraIdName = "paymentId";
        Long   extraIdValue = paymentDisabled.getPaymentId();
        String description = "공급자에 의해 결제가 취소되었습니다." ;
        inputRecord(paymentDisabled, extraIdName, extraIdValue, description);

    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallCaught_inputRecord(@Payload CallCaught callCaught){
        if(!callCaught.validate()) return;

        System.out.println("\n\n##### listener Dashboard callCaught : " + callCaught.toJson() + "\n\n");

        String extraIdName = "catchId";
        Long   extraIdValue = callCaught.getCatchId();
        String description = "driverName=" + callCaught.getDriverName() + " : 대리기사가 콜을 받았습니다.";
        inputRecord(callCaught, extraIdName, extraIdValue, description);

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCatchCancelled_inputRecord(@Payload CatchCancelled catchCancelled){
        if(!catchCancelled.validate()) return;

        System.out.println("\n\n##### listener Dashboard catchCancelled : " + catchCancelled.toJson() + "\n\n");

        String extraIdName = "catchId";
        Long   extraIdValue = catchCancelled.getCatchId();
        String description = "" ;
        inputRecord(catchCancelled, extraIdName, extraIdValue, description);

    }


    public void inputRecord(@NotNull CallEvent callEvent, String extraIdName, Long extraIdValue, String description) {
        Timestamp timestamp = convertToTimestamp(callEvent.getTimestamp());
        
        Dashboard dashboard = new Dashboard();
        dashboard.setCallId(callEvent.getCallId());
        dashboard.setTimestamp(timestamp);
        dashboard.setEvent(callEvent.getEventType());
        dashboard.setExtraIdName(extraIdName);
        dashboard.setExtraIdValue(extraIdValue);
        dashboard.setDescription(description);
        dashboardRepository.save(dashboard);

    }

    public Timestamp convertToTimestamp(String date)  {
        try {
            SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
            long time = defaultSimpleDateFormat.parse(date).getTime();
            Timestamp ts = new Timestamp(time);
            return ts;
        } catch (Exception e) {
            throw new RuntimeException("Event timestamp 값을 파싱할 수 없습니다. " + date);
        }

    }
}


