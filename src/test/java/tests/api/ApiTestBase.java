package tests.api;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class ApiTestBase {
    static final ApiClient api = new ApiClient();

    String userName;
    String userPassword = "123456";

    String bookTitle;
    String bookAuthors;
    Integer publicationYear;
    String description;
    String telegramChatLink;

    String firstName;
    String lastName;
    String email;

    String clubReview;
    Integer assessment;
    Integer readPages;

    @BeforeAll
    static void setupEnvironment() {
        RestAssured.baseURI = System.getProperty("url", "https://book-club.qa.guru");

        Configuration.remote = System.getProperty("remote");
    }

    @BeforeEach

    public void prepareTests() {
        Faker faker = new Faker();
        userName = faker.name().firstName() + faker.number().numberBetween(13000, 19000);

        bookTitle = faker.book().title() + faker.number().numberBetween(13000, 19000);;
        bookAuthors = faker.book().author();
        publicationYear = faker.number().numberBetween(1900, 2025);
        description = faker.book().title();
        telegramChatLink = faker.internet().url();

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();

        clubReview = faker.lorem().sentence();
        assessment = faker.number().numberBetween(1, 6);
        readPages = faker.number().numberBetween(5, 150);
    }

    void addListener() {
        SelenideLogger.addListener(
                "Allure Selenide",
                new AllureSelenide()
        );
    }
}