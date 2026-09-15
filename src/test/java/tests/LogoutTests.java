package tests;

import models.login.LoginRequestModel;
import models.logout.SuccessfulLogoutRequestModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("refresh");

        SuccessfulLogoutRequestModel logoutData = new SuccessfulLogoutRequestModel(refreshToken);

        given(successfulLogoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/api/v1/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Test
    public void wrongRefreshTokenLogoutTest() {
        SuccessfulLogoutRequestModel LogoutData = new SuccessfulLogoutRequestModel(WRONG_REFRESH_TOKEN);

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


    }
}
