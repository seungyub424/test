package nicecall.util;

import java.util.Objects;

public class PaymentResult {
    Long   resultCode ;
    String resultMessage ;

    public Long getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultCode(Long resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResult that = (PaymentResult) o;
        return Objects.equals(resultCode, that.resultCode) && Objects.equals(resultMessage, that.resultMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultCode, resultMessage);
    }

    @Override
    public String toString() {
        return "PaymentResult{" +
                "resultCode=" + resultCode +
                ", resultMessage='" + resultMessage + '\'' +
                '}';
    }
}
