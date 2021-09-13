package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallCaught extends CallEvent {
    //    private Long callId; // inherited
    private Long catchId;
    private String driverName;

}
