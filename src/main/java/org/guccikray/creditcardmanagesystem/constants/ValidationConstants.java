package org.guccikray.creditcardmanagesystem.constants;

import java.util.regex.Pattern;

public final class ValidationConstants {

    // registration and sign in constants
    public static final String NAME_FIElD = "Name";
    public static final String SURNAME_FIELD = "Surname";
    public static final String PASSWORD_FIELD = "Password";
    public static final String EMAIL_FIELD = "Email";
    public static final String EMPTY_NAME_OR_WHITESPACES = "Name can not be empty or contain whitespaces";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String EMPTY_SURNAME_OR_WHITESPACES = "Surname can not be empty or contain whitespaces";
    public static final String EMPTY_PASSWORD_OR_WHITESPACES = "Password can not be empty or contain whitespaces";
    public static final String PASSWORD_HAS_DISALLOWED_SEQUENCE = "Password must be between 8 and 20 characters, " +
        "contain one digit, one upper case character and one special character";
    public static final String EMPTY_EMAIL_OR_WHITESPACES = "Email can not be empty or contain whitespaces";
    public static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
        Pattern.CASE_INSENSITIVE);
    public static final Integer MIN_PASSWORD_LENGTH = 8;
    public static final Integer MAX_PASSWORD_LENGTH = 20;

    // cards constants
    public static final String LAST_FOUR_DIGITS_FIELD = "LastFourDigits";
    public static final String STATUS_FIELD = "Status";
    public static final String EMPTY_LAST_DIGITS_OR_WHITESPACES = "Last four digits can not be empty " +
        "or contain whitespaces";
    public static final String LAST_FOUR_DIGITS_CONTAINS_CHAR = "Last four digits must contain only digits";
    public static final String LAST_FOUR_DIGITS_LENGTH = "Last four digits length must be four";
    public static final String EMPTY_STATUS_OR_WHITESPACES = "Status can not be empty or contain whitespaces";
    public static final Pattern DIGITS = Pattern.compile("[0-9]+");

    // transaction constants
    public static final String SOURCE_CARD_FIELD = "SourceCardLastFourDigits";
    public static final String DESTINATION_CARD_FIELD = "DestinationCardLastFourDigits";
    public static final String AMOUNT_FIELD = "Amount";
    public static final String EMPTY_SOURCE_CARD_OR_WHITESPACES = "Source card last four digits can not" +
        " be empty or contain whitespaces";
    public static final String SOURCE_CARD_CONTAINS_CHAR = "Source card must contain only digits";
    public static final String EMPTY_DESTINATION_CARD_OR_WHITESPACES = "Destination card last four digits can not" +
        " be empty or contain whitespaces";
    public static final String DESTINATION_CARD_CONTAINS_CHAR = "Destination card must contain only digits";
    public static final String EMPTY_AMOUNT_OR_WHITESPACES = "Amount can not be empty or contain whitespaces";
    public static final String AMOUNT_CONTAINS_CHAR = "Amount must contain only digits";
    public static final String NEGATIVE_AMOUNT = "Amount can not be negative";
    public static final Pattern INTEGER = Pattern.compile("^-?\\d+$");

    public static Pattern getPasswordRegex() {
        String ONE_DIGIT = "(?=.*[0-9])";
        String LOWER_CASE = "(?=.*[a-z])";
        String UPPER_CASE = "(?=.*[A-Z])";
        String SPECIAL_CHAR = "(?=.*[@#$%^&+=])";
        String NO_SPACE = "(?=\\S+$)";
        String MIN_MAX_CHAR = ".{" + MIN_PASSWORD_LENGTH +
            "," + MAX_PASSWORD_LENGTH + "}";

        return Pattern.compile(ONE_DIGIT + LOWER_CASE + UPPER_CASE + SPECIAL_CHAR + NO_SPACE + MIN_MAX_CHAR);
    }
}
