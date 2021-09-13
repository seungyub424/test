package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CatchDenied extends CallEvent {

//    private Long callId;
    private String mobile;
    private String location;
}
