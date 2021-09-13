package nicecall.domain;

import lombok.Getter;
import lombok.Setter;
import nicecall.event.CallCaught;
import nicecall.event.CatchDenied;
import nicecall.event.CallReceived;
import nicecall.event.CatchCancelled;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Catcher {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long catchId;

    private String driverName;
    private CatchStatus catchStatus;

    //------------------------------------
    // belows are caller's information
    private Long callId;

    private String mobile;
    private String location;
    private Integer payAmount;

    // 콜 접수 불가 지역일 경우 deny 한다.
    @PostPersist
    public void onPostPersist() {
        System.out.println(" ### Catcher.onPostPersist ###");

        if (this.getCatchStatus() == CatchStatus.RECEIVED) {
            System.out.println(" ### 서비스 가능 지역으로 접수되었습니다. ###");
            CallReceived callReceived = new CallReceived();
            BeanUtils.copyProperties(this, callReceived);
            callReceived.publishAfterCommit();

        } else if (this.getCatchStatus() == CatchStatus.DENIED) {
            System.out.println(" ### 서비스 불가 지역입니다. ###");
            CatchDenied catchDenied = new CatchDenied();
            BeanUtils.copyProperties(this, catchDenied);
            catchDenied.publishAfterCommit();
        }
    }


    // 콜을 잡거나 취소하면 이벤트를 발생시킨다.
    @PostUpdate
    public void onPostUpdate() {
        System.out.println(" ### Catcher.onPostUpdate ###");

        if (this.getCatchStatus() == CatchStatus.CAUGHT) {
            System.out.println(" ### Catcher.CallCaught ###");
            CallCaught callCaught = new CallCaught();
            BeanUtils.copyProperties(this, callCaught);
            callCaught.publishAfterCommit();

        } else if (this.getCatchStatus() == CatchStatus.DENIED) {
            System.out.println(" ### 서비스 불가 지역입니다. ###");
            CatchDenied catchDenied = new CatchDenied();
            BeanUtils.copyProperties(this, catchDenied);
            catchDenied.publishAfterCommit();

        } else  if (this.getCatchStatus() == CatchStatus.CANCELLED) {
            System.out.println(" ### Catcher.CatchCancelled ###");
            CatchCancelled catchCancelled = new CatchCancelled();
            BeanUtils.copyProperties(this, catchCancelled);
            catchCancelled.publishAfterCommit();
        }

    }

}
