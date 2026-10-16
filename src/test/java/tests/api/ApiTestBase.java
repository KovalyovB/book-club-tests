package tests.api;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;


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

        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browsersize", "1920x1080");
        Configuration.baseUrl = System.getProperty(
                "url",
                "https://www.libroroom.ru"
        );
        Configuration.browserVersion = System.getProperty("version");
        Configuration.headless = Boolean.parseBoolean(
                System.getProperty("headless", "false")
        );

        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--disable-dev-shm-usage",
                "--no-sandbox"
        );

        options.addArguments("--disable-blink-features=AutomationControlled");

        options.setExperimentalOption(
                "excludeSwitches",
                List.of("enable-automation")
        );

        options.setExperimentalOption(
                "useAutomationExtension",
                false
        );

        options.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true,
                        "enableVideo", true
                )
        );

        Configuration.browserCapabilities = options;

        Configuration.remote = System.getProperty("remote");
    }

    @BeforeEach

    public void prepareTests() {
        Faker faker = new Faker();
        userName = faker.name().firstName() + "1235";

        bookTitle = faker.book().title() + " №66";
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

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

        closeWebDriver();
    }
}