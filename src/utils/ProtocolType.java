package utils;

public enum ProtocolType {
    HTTP,
    TCP,
    BOTH;

    private static final ProtocolType[] enumValues = ProtocolType.values();

    public static ProtocolType getProtocolType(int value) {
        return enumValues[value];
    }

    public static int getValue(ProtocolType protocolType) {
        return protocolType.ordinal();
    }
}
