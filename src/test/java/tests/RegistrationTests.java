package tests;

import models.registration.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.registration.RegistrationSpec.*;
import static tests.TestData.*;

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

        assertThat(registrationResponse.remoteAddr().matches(REGISTRATION_IP_REGEXP));
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

        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(EXISTS_USER_REGISTRATION_MESSAGE);
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

        assertEquals(REQUIRED_REGISTRATION_PARAMETER_ERROR, response.password().get(0));
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

        assertEquals(INVALID_REGISTRATION_METHOD_ERROR_MESSAGE, response.detail());
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

        assertEquals(INVALID_REGISTRATION_FORMAT_ERROR_MESSAGE, response.detail());
    }
}
