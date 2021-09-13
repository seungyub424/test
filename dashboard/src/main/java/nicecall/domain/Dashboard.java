package nicecall.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter @Setter
public class Dashboard {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long callId;
    private Timestamp timestamp;
    private String event;
    private String description;
    private String extraIdName;
    private Long extraIdValue;

    public static Dashboard create(Long callId, Timestamp ts) {
        Dashboard d = new Dashboard();
        d.setCallId(callId);
        d.setTimestamp(ts);
        return d;
    }

}
