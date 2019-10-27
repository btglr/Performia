package utils;

/**
 * Specifications of the different request codes/response codes
 * Response codes : >= 500 ; < 1000
 * Error codes : >= 1000
 */
public enum MessageCode {
    UNKNOWN(MessageType.REQUEST, ProtocolType.BOTH, -1, "Unknown request"),

    // Requests
    CONNECTION(MessageType.REQUEST, ProtocolType.BOTH, 1, "Request sent by the web interface or the TCP socket when a user/AI tries to connect"),
    CHOOSE_CHALLENGE(MessageType.REQUEST, ProtocolType.HTTP, 2, "Request sent by the web interface when choosing a challenge"),
    PLAY_TURN(MessageType.REQUEST, ProtocolType.BOTH, 3, "Request sent by the web interface or the TCP socket when the user/AI has made a move"),
    GET_GAME_STATE(MessageType.REQUEST, ProtocolType.HTTP, 4, "Request sent by the web interface regularly to get the game state"),

    // Responses
    INITIAL_GAME_STATE(MessageType.RESPONSE, ProtocolType.HTTP, 500, "Answer sent by the HTTP server with the initial state of the game"),
    ACTION_OK(MessageType.RESPONSE, ProtocolType.HTTP, 501, "Answer sent by the HTTP server if the action was successful"),
    GAME_STATE(MessageType.RESPONSE, ProtocolType.HTTP, 502, "Answer sent by the HTTP server with the game state"),

    // Error responses
    ACTION_NOT_OK(MessageType.RESPONSE, ProtocolType.BOTH, 1000, "Answer sent by the HTTP server if the action was not successful")
    ;

    private final MessageType messageType;
    private final ProtocolType protocolType;
    private final int code;
    private final String description;

    MessageCode(MessageType messageType, ProtocolType protocolType, int code, String description) {
        this.messageType = messageType;
        this.protocolType = protocolType;
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public static MessageCode getRequest(int code) {
        for (MessageCode req : values()) {
            if (req.getCode() == code) {
                return req;
            }
        }
        return UNKNOWN;
    }

    public String getDescription() {
        return description;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }
}