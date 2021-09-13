package nicecall;

import nicecall.config.kafka.KafkaProcessor;

import java.util.Optional;

import nicecall.domain.Payment;
import nicecall.domain.PaymentRepository;
import nicecall.domain.PaymentStatus;
import nicecall.event.CallCancelled;
import nicecall.event.CatchDenied;
import nicecall.event.CallerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired
    PaymentRepository paymentRepository;

    // CallCancelled 이벤트가 수신되면 Payment를 취소시킨다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCallCancelled_cancelPayment(@Payload CallCancelled callCancelled){

        if(!callCancelled.validate()) return;

        System.out.println("\n\n##### listener cancelPayment of payment : " + callCancelled.toJson() + "\n\n");

        Optional<Payment> payment = paymentRepository.findByCallId(callCancelled.getCallId());
        
        if(!payment.isPresent()) return;

        Payment thePayment = payment.get();

        System.out.println("callCancelled.getStatus():"+callCancelled.getStatus() +"$" + (callCancelled.getStatus() == CallerStatus.DENIED));
        thePayment.setStatus(PaymentStatus.CANCELLED);

        System.out.println("PAYMENT CANCELLED >>>>>>>>> " + thePayment.getPaymentStatus());
        paymentRepository.save(thePayment);
            
    }

    // CatchDenied 이벤트가 수신되면 Payment를 취소시킨다.
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCatchDenied_disablePayment(@Payload CatchDenied catchDenied){

        if(!catchDenied.validate()) return;

        System.out.println("\n\n##### listener disablePayment of payment : " + catchDenied.toJson() + "\n\n");

        Optional<Payment> payment = paymentRepository.findByCallId(catchDenied.getCallId());

        if(!payment.isPresent()) return;

        Payment thePayment = payment.get();
        thePayment.setStatus(PaymentStatus.DISABLED);

        paymentRepository.save(thePayment);

    }

//    @StreamListener(KafkaProcessor.INPUT)
//    public void whatever(@Payload String eventString){}

}
