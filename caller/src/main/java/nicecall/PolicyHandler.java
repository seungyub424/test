package nicecall;

import lombok.extern.slf4j.Slf4j;
import nicecall.config.kafka.KafkaProcessor;
import nicecall.domain.Caller;
import nicecall.domain.CallerRepository;
import nicecall.domain.CallerStatus;
import nicecall.event.CatchDenied;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@Slf4j
public class PolicyHandler {

    @Autowired
    CallerRepository callerRepository;

    // 요청한 콜이 거절되었기 때문에 콜 취소를 자체적으로 수행한다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCatchDenied_updateStatus(@Payload CatchDenied catchDenied){
        if(!catchDenied.validate()) return;

        log.info("\n\n##### listener Dashboard catchDenied : " + catchDenied.toJson() + "\n\n");

        Long callId = Long.valueOf(catchDenied.getCallId());
        Optional<Caller> caller = callerRepository.findById(callId);

        if (!caller.isPresent()) {
            throw new InvalidParameterException("<<< 대상 콜을 찾을 수 없습니다 (Wrong callerId : " + catchDenied.getCallId() + " ) >>> ");
        }
        Caller theCaller = caller.get();

        theCaller.setStatus(CallerStatus.DENIED);
        callerRepository.save(theCaller);

    }

//    @StreamListener(KafkaProcessor.INPUT)
//    public void whenever(@Payload String str){
//        log.info ("DashboardHandler Listening ..... " + str);
//    }

}
