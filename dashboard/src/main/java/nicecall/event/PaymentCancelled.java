package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentCancelled extends CallEvent {

    private Long paymentId;
    private String paymentStatus;
//    private Long callId; // inherited

}
