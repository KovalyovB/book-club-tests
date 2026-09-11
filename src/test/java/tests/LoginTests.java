package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {

    String username = "qa_quru_kb";
    String password = "123456";
    String wrongPassword = "123456000";

    @Test
    public void successfulLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(username, password);

        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedToken);
        assertThat(actualRefresh).startsWith(expectedToken);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginRequestModel loginData = new LoginRequestModel(username, wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(wrongCredentialLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        String expectedErrorMessage = "Invalid username or password.";
        String actualErrorMessage = loginResponse.detail();
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }
}
