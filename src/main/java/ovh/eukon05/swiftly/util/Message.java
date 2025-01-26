package ovh.eukon05.swiftly.util;

public class Message {
    public static final String SUCCESS = "Success";
    public static final String BANK_NOT_FOUND = "Bank with the given SWIFT code not found";
    public static final String BANK_ALREADY_EXISTS = "Bank with the given SWIFT code already exists";
    public static final String INVALID_SWIFT_BLANK = "The bank's swift code cannot be null or blank";
    public static final String INVALID_SWIFT_FORMAT = "The bank's swift code must be between 8 and 11 characters long";
    public static final String INVALID_NAME = "The bank's name cannot be null or blank";
    public static final String INVALID_ISO2_BLANK = "The bank's country code cannot be null or blank";
    public static final String INVALID_ISO2_FORMAT = "The bank's country code must be a valid ISO2 code";
    public static final String INVALID_COUNTRY = "The bank's country name cannot be null or blank";
    public static final String INVALID_ADDRESS = "The bank's address cannot be null or blank";
}
