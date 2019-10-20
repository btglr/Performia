package utils;

public enum RequestCode {
    UNKNOWN(-1, "Unknown request"),
    CONNECTION(1, "Request sent by the web interface when a user tries to connect"),
    CHOOSE_CHALLENGE(2, "Request sent by the web interface when choosing a challenge"),
    INITIAL_GAME_STATE(3, "Answer sent by the HTTP server with the initial state of the game"),
    PLAY_TURN(4, "Request sent by the web interface when the user has made a move"),
    ACTION_OK(5, "Answer sent by the HTTP server if the action was successful"),
    ACTION_NOT_OK(6, "Answer sent by the HTTP server if the action was not successful"),
    GET_GAME_STATE(7, "Request sent by the web interface regularly to get the game state"),
    GAME_STATE(8, "Answer sent by the HTTP server with the game state")
    ;

    private final int code;
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