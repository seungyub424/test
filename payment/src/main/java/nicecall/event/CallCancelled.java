package nicecall.event;

import lombok.Getter;
import lombok.Setter;
import nicecall.domain.PayType;

@Getter @Setter
public class CallCancelled extends AbstractEvent {
    private Long callId;

    private String mobile;
    private String location;
    private CallerStatus status;

    private PayType payType;
    private Integer payAmount;

}
