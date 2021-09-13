package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallReceived extends AbstractEvent {
    private Long callId;

    private String mobile;
    private String location;

    private Long paymentId;
}
