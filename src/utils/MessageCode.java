package utils;

import static utils.MessageType.*;

/**
 * Specifications of the different request codes/response codes
 * Response codes : >= 500 ; < 1000
 * Error codes : >= 1000
 */
public enum MessageCode {
    UNKNOWN(REQUEST, -1, "Unknown request"),

    // Requests
    CONNECTION(REQUEST, 1, "Request sent by the web interface or the TCP socket when a user/AI tries to connect"),
    CHOOSE_CHALLENGE(REQUEST, 2, "Request sent by the web interface when choosing a challenge"),
    PLAY_TURN(REQUEST, 3, "Request sent by the web interface or the TCP socket when the user/AI has made a move"),
    GET_CHALLENGE_STATE(REQUEST, 4, "Request sent by the web interface regularly to get the challenge state"),
    WAIT_CHALLENGE_START(REQUEST, 5, "Request sent by the web interface/AI when waiting for the challenge to start under certain conditions"),
    GET_LIST_CHALLENGE(REQUEST, 6,"Request sent by the web interface to get the list of challenges"),
    GET_CHALLENGE_DETAILS(REQUEST, 7, "Request sent by the web interface to get the details of a challenge"),
    REGISTER(REQUEST, 8, "Request sent by the web interface/AI to register"),
    GUESS_IS_AI(REQUEST, 9, "Request sent by the web interface if the user think his challenger is an AI."),
    GET_STATS(REQUEST, 10, "Request sent by the web interface to get the stats on their AI"),
    GET_AI_LIST(REQUEST, 11, "Request sent by the admin page of the web interface to get the different types of AIs"),

    // Responses
    INITIAL_CHALLENGE_STATE(RESPONSE, 500, "Answer sent by the HTTP server with the initial state of the challenge"),
    ACTION_OK(RESPONSE, 501, "Answer sent by the HTTP server if the action was successful"),
    CHALLENGE_STATE(RESPONSE, 502, "Answer sent by the HTTP server with the challenge state"),
    CONNECTION_OK(RESPONSE, 503, "Answer sent by the HTTP/TCP server when the user/AI successfully connected"),
    CHALLENGE_CAN_START(RESPONSE, 504, "Answer sent by the HTTP/TCP server as a response to the WAIT_CHALLENGE_START request when the challenge can start"),
    CHALLENGE_CANNOT_START(RESPONSE, 505, "Answer sent by the HTTP/TCP server as a response to the WAIT_CHALLENGE_START when the challenge cannot start"),
    ROOM_NOT_FULL(RESPONSE, 506, "Answer sent by the HTTP/TCP server when a challenge has been chosen but the room is not full"),
    // Not used in the Java part, only the interface
    CHALLENGE_OVER(RESPONSE, 507, "Answer sent by the interface when it detects the game is over"),
    LIST_CHALLENGE(RESPONSE, 508, "Answer sent by the HTTP server with the list of challenges"),
    CHALLENGE_DETAILS(RESPONSE, 509, "Answer sent by the HTTP server with the details of a challenge"),
    REGISTRATION_OK(RESPONSE, 510, "Answer sent by the HTTP server when a user successfully registered"),
    NO_STATS(RESPONSE, 511, "Answer sent by the HTTP server with no stats"),
    SEND_STATS(RESPONSE, 512, "Answer sent by the HTTP server with the details of stats"),
    AI_TYPES(RESPONSE, 513, "Answer sent by the HTTP server with the list of AI types"),

    // Error responses
    ACTION_NOT_OK(RESPONSE, 1000, "Answer sent by the HTTP server if the action was not successful"),
    CONNECTION_ERROR(RESPONSE, 1001, "Answer sent by the HTTP/TCP server if the connection was not successful"),
    WRONG_CHALLENGE(RESPONSE, 1002, "Answer sent by the HTTP/TCP server if the chose challenge doesn't exist"),
    USER_NOT_PLAYING(RESPONSE, 1003, "Answer sent by the HTTP server if the user isn't playing a challenge"),
    MISSING_PARAMETERS(RESPONSE, 1004, "Answer sent by the HTTP/TCP server if the request is missing parameters"),
    MISSING_REQUEST_CODE(RESPONSE, 1005, "Answer sent by the HTTP/TCP server if the request is missing its code"),
	UNKNOWN_USER(RESPONSE, 1006, "Answer sent by the HTTP/TCP server if the user id is unknown or missing"),
    REGISTRATION_ERROR(RESPONSE, 1007, "Answer sent by the HTTP server if the registration failed")
    ;

    private final MessageType messageType;
    private final int code;
    private final String description;

    MessageCode(MessageType messageType, int code, String description) {
        this.messageType = messageType;
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
}