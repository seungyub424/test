package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallEvent extends AbstractEvent {
    private Long callId;

}
