package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;
import static tests.TestData.*;

@DisplayName("Тесты авторизации пользователя")
public class LoginTests extends TestBase {

    @Test
    @DisplayName("Успешная авторизация пользователя")
    public void successfulLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = step("Запрос на авторизацию пользователя", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/api/v1/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract()
                        .as(SuccessfulLoginResponseModel.class)
        );

        step("Проверка токенов в ответе", () -> {
            String actualAccess = loginResponse.access();
            String actualRefresh = loginResponse.refresh();

            assertThat(actualAccess).startsWith(LOGIN_EXPECTED_TOKEN);
            assertThat(actualRefresh).startsWith(LOGIN_EXPECTED_TOKEN);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    @DisplayName("Тест ошибки при авторизации. Некорректный пароль.")
    public void wrongCredentialsLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = step("Авторизация с не валидным паролем", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/api/v1/auth/token/")
                        .then()
                        .spec(wrongCredentialLoginResponseSpec)
                        .extract()
                        .as(WrongCredentialsLoginResponseModel.class)
        );

        step("Проверка возврата ошибки в ответе", () -> {
            String actualErrorMessage = loginResponse.detail();
            assertThat(actualErrorMessage).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR_MESSAGE);
        });
    }
}

