package nicecall.domain;

public enum CatchStatus {
    RECEIVED,
    DENIED,
    INVALID,    // RECEIVED에서 취소 요청 수신시 INVALID가 된다.
    CAUGHT,
    CANCELLED   // CAUGHT 상태에서 취소 요청 수신시 CANCELLED가 된다.
}
