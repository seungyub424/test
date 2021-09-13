package nicecall.domain;

import lombok.extern.slf4j.Slf4j;
import nicecall.event.CallCancelled;
import lombok.Getter;
import lombok.Setter;
import nicecall.event.CallMade;
import nicecall.event.CallPayed;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.security.InvalidParameterException;

@Entity
@Getter @Setter
@Slf4j
public class Caller {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long callId;

    private String mobile;
    private String location;

    private PayType payType;
    private Integer payAmount;

    private CallerStatus status;
    private Long paymentId;

    // call 요청시 먼저 Payment 정보가 있어야 한다.
    @PrePersist
    public void onPrePersist() {
        System.out.println(" ### Caller.onPrePersist ###");

        if (this.payType == null ||
                this.payAmount == null ) {
            throw new InvalidParameterException("### FAILURE occurred in Caller saving : payType or payAmount is null. ###");
        }
        this.setStatus(CallerStatus.CREATED);
    }

    // call이 요청되면 CallMade 이벤트를 발생시킨다.
    @PostPersist
    public void onPostPersist() {

        System.out.println(" ### Caller.onPostPersist ###");

        // ------------------------------------------------------------------//
        //   Generate Event
        // ------------------------------------------------------------------//
        CallMade callMade = new CallMade();
        BeanUtils.copyProperties(this, callMade);
        callMade.publishAfterCommit();

        System.out.println(" ### CallMade Event Created ###");
        log.info(" ### CallMade Event Created ###");
    }

    // approve 로 상태가 바뀐경우 CallPayed 이벤트를 발생시킨다.
    @PostUpdate
    public void onPostUpdate() {

        if (this.getStatus() != CallerStatus.APPROVED)
            return;

        System.out.println(" ### Caller.onPostUpdate ### " + this.getStatus());

        // ------------------------------------------------------------------//
        //   Generate Event
        // ------------------------------------------------------------------//
        if (this.getStatus() == CallerStatus.APPROVED) {
            CallPayed callPayed = new CallPayed();
            BeanUtils.copyProperties(this, callPayed);
            callPayed.publishAfterCommit();

            log.info(" ### CallPayed Event Created ###");
        }
    }

    // call이 삭제되면서 CallCancelled 이벤트를 발생시킨다.
    @PostRemove
    public void onPostRemove() {
        System.out.println(" ### Caller.onPostRemove ###");

        // ------------------------------------------------------------------//
        //   Generate Event
        // ------------------------------------------------------------------//
        CallCancelled callCancelled = new CallCancelled();
        BeanUtils.copyProperties(this, callCancelled);
        callCancelled.publishAfterCommit();

        log.info(" ### CallCancelled Event Created ###");
    }

    @Override
    public String toString() {
        return "Caller{" +
                "callId=" + callId +
                ", mobile='" + mobile + '\'' +
                ", location='" + location + '\'' +
                ", payType=" + payType +
                ", paymentId=" + paymentId +
                ", status=" + status +
                '}';
    }
}
