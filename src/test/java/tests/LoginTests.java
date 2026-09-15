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

        step("Успешная авторизация пользователя и проверка параметров ответа", () -> {
            SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/api/v1/auth/token/")
                    .then()
                    .spec(successfulLoginResponseSpec)
                    .extract()
                    .as(SuccessfulLoginResponseModel.class);

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

        step("Проверки тела ответа и кода 401 при ошибке при авторизации", () -> {
            WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/api/v1/auth/token/")
                    .then()
                    .spec(wrongCredentialLoginResponseSpec)
                    .extract()
                    .as(WrongCredentialsLoginResponseModel.class);

            String actualErrorMessage = loginResponse.detail();
            assertThat(actualErrorMessage).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR_MESSAGE);
        });
    }
}
