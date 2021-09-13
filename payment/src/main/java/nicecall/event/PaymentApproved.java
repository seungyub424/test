package nicecall.event;

import lombok.Getter;
import lombok.Setter;
import nicecall.domain.PaymentStatus;

@Getter @Setter
public class PaymentApproved extends AbstractEvent {

    private Long paymentId;
    private PaymentStatus paymentStatus;
    private Long callId;

}
