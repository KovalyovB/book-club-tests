package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "qa_quru_kb";
    public static final String LOGIN_PASSWORD = "123456";
    public static final String LOGIN_WRONG_PASSWORD = "123456000";


    public static final String LOGIN_EXPECTED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR_MESSAGE = "Invalid username or password.";
    public static final String WRONG_REFRESH_TOKEN = "111";
    public static final String EXPECTED_TOKEN_DETAIL_ERROR_MESSAGE = "Token is invalid";
    public static final String EXPECTED_TOKEN_CODE_ERROR_MESSAGE = "token_not_valid";

    public static final String REGISTRATION_IP_REGEXP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final String EXISTS_USER_REGISTRATION_MESSAGE = "A user with that username already exists.";
    public static final String REQUIRED_REGISTRATION_PARAMETER_ERROR = "This field is required.";
    public static final String INVALID_REGISTRATION_METHOD_ERROR_MESSAGE = "Method \"GET\" not allowed.";
    public static final String INVALID_REGISTRATION_FORMAT_ERROR_MESSAGE =
            "Unsupported media type \"text/plain; charset=ISO-8859-1\" in request.";

    public static final String MISSING_TOKEN_UPDATE_USER_ERROR_MESSAGE =
            "Authentication credentials were not provided.";

}
