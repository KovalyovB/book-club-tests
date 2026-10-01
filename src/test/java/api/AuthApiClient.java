package api;

import io.qameta.allure.Step;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.SuccessfulLogoutRequestModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {

    @Step("Запрос на авторизацию пользователя")
    public SuccessfulLoginResponseModel login(LoginRequestModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    @Step("Авторизация с не валидным паролем")
    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginRequestModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(wrongCredentialLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Успешная авторизация и получение токена")
    public String loginAndGetRefreshToken(LoginRequestModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("refresh");
    }

    @Step("Успешный выход из системы с токеном, проверка статуса 200")
    public void logout(SuccessfulLogoutRequestModel logoutData) {
        given(successfulLogoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/api/v1/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Запрос на разавторизацию с не валидным токеном")
    public WrongRefreshTokenLogoutResponseModel logoutWithWrongToken(SuccessfulLogoutRequestModel logoutData) {
        return given(successfulLogoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/api/v1/auth/logout/")
                .then()
                .spec(wrongRefreshTokenResponseSpec)
                .extract()
                .as(WrongRefreshTokenLogoutResponseModel.class);
    }

    @Step("Успешная авторизация и получение токена для обновления")
    public String loginAndGetRefreshTokenForUpdate(LoginRequestModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("access");
    }
}
