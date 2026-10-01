package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    protected static final ApiClient api = new ApiClient();

    @BeforeAll
    static void setupEnvironment() {

        RestAssured.baseURI = System.getProperty("url", "https://book-club.qa.guru");

    }
}
