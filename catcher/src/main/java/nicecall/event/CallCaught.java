package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallCaught extends AbstractEvent {
    private Long catchId;
    private String driverName;

    private String mobile;
    private String location;
    private Long callId;

}
