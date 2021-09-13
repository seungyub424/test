package nicecall.domain;

import lombok.Getter;
import nicecall.event.PaymentApproved;
import nicecall.event.PaymentCancelled;
import nicecall.event.PaymentDisabled;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private PayType payType;
    private Integer payAmount;
    private Long paymentId;
    private PaymentStatus paymentStatus;
    private Long callId;

    protected Payment () {
        // Cannot create outside
    }

    public static Payment approve(PayType payType, Integer payAmount, Long callId) {
        if (payAmount < 5000) {
            throw new RuntimeException("최소 결제금액이 5000원 이상이어야 합니다.");
        }
        Payment payment = new Payment();
        payment.payType = payType;
        payment.payAmount = payAmount;
        payment.callId = callId;
        payment.paymentStatus = PaymentStatus.APPROVED;
        return payment;
    }

    public void setStatus(PaymentStatus status) {
        if (status == PaymentStatus.CANCELLED)
            this.paymentStatus = status;
        else if (status == PaymentStatus.DISABLED)
            this.paymentStatus = status;
    }

    // Payment가 완료되면
    @PostPersist
    public void onPostPersist() {
        System.out.println(" ### Payment.onPostPersist ###");

        this.paymentId = this.id;

        try {
            Thread.currentThread().sleep((long) (400 + Math.random() * 220));
            System.out.println("=============결재 승인 완료=============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PaymentApproved paymentApproved = new PaymentApproved();
        BeanUtils.copyProperties(this, paymentApproved);
        paymentApproved.publishAfterCommit();
    }

    // Payment.status가 CANCELLED로 Update될 경우
    @PostUpdate
    public void OnPostUpdate() {

        if (this.paymentStatus == PaymentStatus.CANCELLED) {

            System.out.println(" ### Payment.OnPostUpdate : CANCELLED ###");

            PaymentCancelled paymentCancelled = new PaymentCancelled();
            BeanUtils.copyProperties(this, paymentCancelled);
            paymentCancelled.publishAfterCommit();

        }
        else if (this.paymentStatus == PaymentStatus.DISABLED) {

            System.out.println(" ### Payment.OnPostUpdate : DISABLED ###");

            PaymentDisabled paymentDisabled = new PaymentDisabled();
            BeanUtils.copyProperties(this, paymentDisabled);
            paymentDisabled.publishAfterCommit();

        }
    }

}
