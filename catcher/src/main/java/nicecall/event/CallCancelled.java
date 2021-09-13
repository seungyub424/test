package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallCancelled extends AbstractEvent {

    private Long callId;

}
