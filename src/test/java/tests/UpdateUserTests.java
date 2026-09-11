package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.NotProvidedTokenOnUpdateUserResponseModel;
import models.update_user.SuccessfulUpdateUserRequestModel;
import models.update_user.SuccessfulUpdateUserResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.update_user.UpdateUserSpec.*;

public class UpdateUserTests extends TestBase {


    String username = "qa_quru_kb";
    String password = "123456";
    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
    }

    @Test
    public void successfulUpdateUserInfoTest() {
        LoginRequestModel loginData = new LoginRequestModel(username, password);

        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (username, firstName, lastName, email);

        SuccessfulUpdateUserResponseModel userDataResponse = given(updateUserRequestSpec)
                .auth()
                .oauth2(loginResponse.access())
                .body(updateData)
                .when()
                .put("/api/v1/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(SuccessfulUpdateUserResponseModel.class);

        assertThat(userDataResponse.username()).isEqualTo(username);
        assertThat(userDataResponse.firstName()).isEqualTo(firstName);
        assertThat(userDataResponse.lastName()).isEqualTo(lastName);
        assertThat(userDataResponse.email()).isEqualTo(email);
        assertThat(userDataResponse.username()).isNotEmpty();
    }

    @Test
    public void notProvidedTokenOnUpdateUserTest() {
        LoginRequestModel loginData = new LoginRequestModel(username, password);

        given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/api/v1/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (username, firstName, lastName, email);

        NotProvidedTokenOnUpdateUserResponseModel missingTokenResponse = given(updateUserRequestSpec)
                .body(updateData)
                .when()
                .put("/api/v1/users/me/")
                .then()
                .spec(notProvidedTokenOnUpdateUserResponseSpec)
                .extract()
                .as(NotProvidedTokenOnUpdateUserResponseModel.class);

        String expectedError = "Authentication credentials were not provided.";
        assertThat(missingTokenResponse.detail()).isEqualTo(expectedError);

    }
}
