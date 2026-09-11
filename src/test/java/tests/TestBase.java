package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    @BeforeAll
    static void setupEnvironment() {
        RestAssured.baseURI = System.getProperty("url", "https://book-club.qa.guru");

    }
}
