package nicecall.event;

import lombok.Getter;
import lombok.Setter;
import nicecall.domain.CallerStatus;
import nicecall.domain.PayType;

@Getter @Setter
public class PaymentApproved extends AbstractEvent {

    private Long paymentId;

}
