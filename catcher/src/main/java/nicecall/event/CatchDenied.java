package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CatchDenied extends AbstractEvent {

    private Long callId;
    private String mobile;
    private String location;
}
