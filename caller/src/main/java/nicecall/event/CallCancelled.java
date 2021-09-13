package nicecall.event;

import lombok.Getter;
import lombok.Setter;
import nicecall.domain.CallerStatus;
import nicecall.domain.PayType;

import java.lang.reflect.Constructor;

@Getter @Setter
public class CallCancelled extends AbstractEvent {
    private Long callId;

    private String mobile;
    private String location;

    private PayType payType;
    private Integer payAmount;

    private CallerStatus status;
    private Long paymentId;

    public CallCancelled() {
        this.status = CallerStatus.CANCELLED;
    }
}
