package tests;

import models.login.LoginRequestModel;
import models.logout.SuccessfulLogoutRequestModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;
import static tests.TestData.*;

@DisplayName("Тесты по выходу из системы")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из системы")
    public void successfulLogoutTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = step("Успешная авторизация и получение токена", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/api/v1/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().path("refresh"));

        SuccessfulLogoutRequestModel logoutData = new SuccessfulLogoutRequestModel(refreshToken);

        step("Успешный выход из системы с токеном, проверка статуса 200", () -> {
            given(successfulLogoutRequestSpec)
                    .body(logoutData)
                    .when()
                    .post("/api/v1/auth/logout/")
                    .then()
                    .spec(successfulLogoutResponseSpec);
        });
    }

    @Test
    @DisplayName("Попытка выхода из системы с некорректным токеном")
    public void wrongRefreshTokenLogoutTest() {
        SuccessfulLogoutRequestModel LogoutData = new SuccessfulLogoutRequestModel(WRONG_REFRESH_TOKEN);

        step("Проверка ошибки при выходе из системы с некорректным токеном", () -> {
            WrongRefreshTokenLogoutResponseModel logoutResponse = given(successfulLogoutRequestSpec)
                    .body(LogoutData)
                    .when()
                    .post("/api/v1/auth/logout/")
                    .then()
                    .spec(wrongRefreshTokenResponseSpec)
                    .extract()
                    .as(WrongRefreshTokenLogoutResponseModel.class);

            assertThat(logoutResponse.detail()).isEqualTo(EXPECTED_TOKEN_DETAIL_ERROR_MESSAGE);
            assertThat(logoutResponse.code()).isEqualTo(EXPECTED_TOKEN_CODE_ERROR_MESSAGE);
        });
    }
}
