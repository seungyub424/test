package nicecall;

import nicecall.config.kafka.KafkaProcessor;

import java.util.Optional;

import nicecall.domain.CatchStatus;
import nicecall.domain.Catcher;
import nicecall.domain.CatcherRepository;
import nicecall.event.CallCancelled;
import nicecall.event.CallPayed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired
    CatcherRepository catcherRepository;


    // Caller에서 지불 완료된 콜에 대해 receive 처리한다(catcher를 생성).
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallPayed_receiveCall(@Payload CallPayed callPayed){

        if(!callPayed.validate()) return;

        System.out.println("\n\n##### listener receiveCall of Catcher : " + callPayed.toJson() + "\n\n");

        Optional<Catcher> optionalCatcher = catcherRepository.findByCallId(callPayed.getCallId());
        
        if (optionalCatcher.isPresent()) {
            throw new IllegalStateException("<<< 이미 접수된 콜 정보입니다 : >>>" );
        } else {
            Catcher catcher = new Catcher();
            catcher.setCallId(callPayed.getCallId());
            catcher.setMobile(callPayed.getMobile());
            catcher.setLocation(callPayed.getLocation());
            catcher.setPayAmount(callPayed.getPayAmount());

            String location = catcher.getLocation();
            if (!outOfService(location)) {
                catcher.setCatchStatus(CatchStatus.RECEIVED);
            } else {
                catcher.setCatchStatus(CatchStatus.DENIED);
            }
            catcherRepository.save(catcher);
        }
    }

    // Caller에서 취소 처리된 콜의 catcher 상태를 취소한다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallCancelled_cancelCatch(@Payload CallCancelled callCancelled){

        if(!callCancelled.validate()) return;

        System.out.println("\n\n##### listener receiveCall of Catcher : " + callCancelled.toJson() + "\n\n");

        Optional<Catcher> optionalCatcher = catcherRepository.findByCallId(callCancelled.getCallId());

        if (optionalCatcher.isPresent()) {
            Catcher catcher = optionalCatcher.get();
            if (catcher.getCatchStatus() == CatchStatus.RECEIVED) {
                catcher.setCatchStatus(CatchStatus.INVALID);
            } else if (catcher.getCatchStatus() == CatchStatus.CAUGHT) {
                catcher.setCatchStatus(CatchStatus.CANCELLED);
            }
            catcherRepository.save(catcher);
        }
    }

//    @StreamListener(KafkaProcessor.INPUT)
//    public void whatever(@Payload String eventString){}

    @Value("${catcher.service.area}")
    String svcAreas;

    private Boolean outOfService(String location) {

        System.out.println(" CATCHER SERVICE AREA : " + svcAreas);
        String [] arrSvcArea = svcAreas.split(",");

        for (int i = 0; i < arrSvcArea.length ; i++) {
            String s = arrSvcArea[i].toLowerCase();

            // 서비스 지역이면 false 리턴
            if (s.equals(location.toLowerCase())) return false;

        }
        return true;
    }
}
