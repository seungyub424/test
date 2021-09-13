package nicecall.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallPayed extends AbstractEvent {
    private Long callId;

    private String mobile;
    private String location;
    private Integer payAmount;

    private Long pamentId;
}
