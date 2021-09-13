package nicecall.event;

public enum CallerStatus {
    CREATED,
    APPROVING,
    APPROVED,
    DENIED,
    CANCELLED,  // however this is never used, it remains for the future-needs
}
