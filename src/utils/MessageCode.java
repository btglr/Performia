package utils;

import static utils.MessageType.*;
import static utils.ProtocolType.*;

/**
 * Specifications of the different request codes/response codes
 * Response codes : >= 500 ; < 1000
 * Error codes : >= 1000
 */
public enum MessageCode {
    UNKNOWN(REQUEST, BOTH, -1, "Unknown request"),

    // Requests
    CONNECTION(REQUEST, BOTH, 1, "Request sent by the web interface or the TCP socket when a user/AI tries to connect"),
    CHOOSE_CHALLENGE(REQUEST, HTTP, 2, "Request sent by the web interface when choosing a challenge"),
    PLAY_TURN(REQUEST, BOTH, 3, "Request sent by the web interface or the TCP socket when the user/AI has made a move"),
    GET_CHALLENGE_STATE(REQUEST, HTTP, 4, "Request sent by the web interface regularly to get the challenge state"),
    WAIT_CHALLENGE_START(REQUEST, BOTH, 5, "Request sent by the web interface/AI when waiting for the challenge to start under certain conditions"),

    // Responses
    INITIAL_CHALLENGE_STATE(RESPONSE, HTTP, 500, "Answer sent by the HTTP server with the initial state of the challenge"),
    ACTION_OK(RESPONSE, HTTP, 501, "Answer sent by the HTTP server if the action was successful"),
    CHALLENGE_STATE(RESPONSE, HTTP, 502, "Answer sent by the HTTP server with the challenge state"),
    CONNECTION_OK(RESPONSE, BOTH, 503, "Answer sent by the HTTP/TCP server when the user/AI successfully connected"),
    CHALLENGE_CAN_START(RESPONSE, BOTH, 504, "Answer sent by the HTTP/TCP server as a response to the WAIT_CHALLENGE_START request when the challenge can start"),
    CHALLENGE_CANNOT_START(RESPONSE, BOTH, 505, "Answer sent by the HTTP/TCP server as a response to the WAIT_CHALLENGE_START when the challenge cannot start"),
    ROOM_NOT_FULL(RESPONSE, BOTH, 506, "Answer sent by the HTTP/TCP server when a challenge has been chosen but the room is not full"),
    // Not used in the Java part, only the interface
    CHALLENGE_OVER(RESPONSE, BOTH, 507, "Answer sent by the interface when it detects the game is over"),

    // Error responses
    ACTION_NOT_OK(RESPONSE, BOTH, 1000, "Answer sent by the HTTP server if the action was not successful"),
    CONNECTION_ERROR(RESPONSE, BOTH, 1001, "Answer sent by the HTTP/TCP server if the connection was not successful"),
    WRONG_CHALLENGE(RESPONSE, BOTH, 1002, "Answer sent by the HTTP/TCP server if the chose challenge doesn't exist"),
    USER_NOT_PLAYING(RESPONSE, HTTP, 1003, "Answer sent by the HTTP server if the user isn't playing a challenge"),
    MISSING_PARAMETERS(RESPONSE, BOTH, 1004, "Answer sent by the HTTP/TCP server if the request is missing parameters"),
    MISSING_REQUEST_CODE(RESPONSE, BOTH, 1005, "Answer sent by the HTTP/TCP server if the request is missing its code"),
	UNKNOWN_USER(RESPONSE, BOTH, 1006, "Answer sent by the HTTP/TCP server if the user id is unknown or missing")
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