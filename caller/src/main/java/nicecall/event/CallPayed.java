package nicecall.event;

import lombok.Getter;
import lombok.Setter;
import nicecall.domain.CallerStatus;
import nicecall.domain.PayType;

@Getter @Setter
public class CallPayed extends AbstractEvent {
    private Long callId;

    private String mobile;
    private String location;

    private PayType payType;
    private Integer payAmount;

    private CallerStatus status;
    private Long paymentId;
}
