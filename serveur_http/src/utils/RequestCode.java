package utils;

public enum RequestCode {
    UNKNOWN(-1, "Unknown request"),
    CHOOSE_CHALLENGE(2, "Request sent by the web interface when choosing a challenge"),
    PLAY_TURN(4, "Request sent by the web interface when the user has made a move")
    ;

    public final int code;
    private final String description;

    RequestCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public static RequestCode getRequest(int code) {
        for (RequestCode req : values()) {
            if (req.getCode() == code) {
                return req;
            }
        }
        return UNKNOWN;
    }

    public String getDescription() {
        return description;
    }
}