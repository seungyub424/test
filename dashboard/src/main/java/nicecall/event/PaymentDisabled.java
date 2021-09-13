package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentDisabled extends CallEvent {

    private Long paymentId;
    private String paymentStatus;
    private Long callId;

}
