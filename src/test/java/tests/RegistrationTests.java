package tests;

import models.registration.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.name().firstName();
    }

    @Test
    public void successfulRegistrationTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUserName = registrationResponse.username();
        assertThat(actualUserName).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        String ipAddrRegexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        assertThat(registrationResponse.remoteAddr().matches(ipAddrRegexp));
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        RegResponseExistingUserModel secondRegistrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(RegResponseExistingUserModel.class);

        String expectedError = "A user with that username already exists.";
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    public void isRequiredFieldMissingTest() {
        RegRequestWithoutRequiredParamModel missingParameterData = new RegRequestWithoutRequiredParamModel(username);

        RegResponseWithoutRequiredParamModel response = given(registrationRequestSpec)
                .body(missingParameterData)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(requiredFieldMissingResponseSpec)
                .extract()
                .as(RegResponseWithoutRequiredParamModel.class);

        String expectedError = "This field is required.";
        assertEquals(expectedError, response.password().get(0));
    }

    @Test
    public void invalidRegistrationMethodTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        RegResponseInvalidMethodModel response = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .get("/api/v1/users/register/")
                .then()
                .spec(invalidRegistrationMethodResponseSpec)
                .extract()
                .as(RegResponseInvalidMethodModel.class);

        String expectedError = "Method \"GET\" not allowed.";
        assertEquals(expectedError, response.detail());
    }

    @Test
    public void unsupportedMediaTypeTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        RegResponseUnsupportedMediaTypeModel response = given(wrongMediaTypeRequestSpec)
                .body(registrationData)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(unsupportedMediaTypeResponseSpec)
                .extract()
                .as(RegResponseUnsupportedMediaTypeModel.class);

        String expectedError = "Unsupported media type \"text/plain; charset=ISO-8859-1\" in request.";
        assertEquals(expectedError, response.detail());
    }
}
