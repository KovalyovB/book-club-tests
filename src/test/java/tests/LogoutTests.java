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

public class LogoutTests extends TestBase {

    String username = "qa_quru_kb";
    String password = "123456";
    String wrongRefreshToken = "111";

    @Test
    public void successfulLogoutTest() {
        LoginRequestModel loginData = new LoginRequestModel(username, password);

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
        SuccessfulLogoutRequestModel LogoutData = new SuccessfulLogoutRequestModel(wrongRefreshToken);

        WrongRefreshTokenLogoutResponseModel logoutResponse = given(successfulLogoutRequestSpec)
                .body(LogoutData)
                .when()
                .post("/api/v1/auth/logout/")
                .then()
                .spec(wrongRefreshTokenResponseSpec)
                .extract()
                .as(WrongRefreshTokenLogoutResponseModel.class);

        String expectedDetailMessage = "Token is invalid";
        String expectedCodeMessage = "token_not_valid";
        assertThat(logoutResponse.detail()).isEqualTo(expectedDetailMessage);
        assertThat(logoutResponse.code()).isEqualTo(expectedCodeMessage);


    }
}
