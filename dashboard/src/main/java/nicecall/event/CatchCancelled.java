package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CatchCancelled extends CallEvent {
    private Long catchId;

    private String mobile;
    private String location;
//    private Long callId; // inherited

}
