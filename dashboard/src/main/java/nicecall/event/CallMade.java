package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallMade extends CallEvent {
//    private Long callId; // inherited

    private String mobile;
    private String location;

    private String payType;
    private Integer payAmount;

    private String status;
    private Long paymentId;
}
