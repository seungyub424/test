package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentApproved extends CallEvent {
    private Long paymentId;
    private String paymentStatus;
//    private Long callId; // inherited

}
