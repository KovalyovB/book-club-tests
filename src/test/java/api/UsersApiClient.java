package api;

import io.qameta.allure.Step;
import models.registration.*;
import models.update_user.NotProvidedTokenOnUpdateUserResponseModel;
import models.update_user.SuccessfulUpdateUserRequestModel;
import models.update_user.SuccessfulUpdateUserResponseModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;
import static specs.update_user.UpdateUserSpec.*;

public class UsersApiClient {

    @Step("Запрос на регистрацию")
    public SuccessfulRegistrationResponseModel register(SuccessfulRegistrationRequestModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Попытка регистрации существующего пользователя")
    public RegResponseExistingUserModel existingUserRegister(SuccessfulRegistrationRequestModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(RegResponseExistingUserModel.class);
    }

    @Step("Запрос на регистрацию с не полным набором параметров")
    public RegResponseWithoutRequiredParamModel missingParameterRegister(RegRequestWithoutRequiredParamModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(requiredFieldMissingResponseSpec)
                .extract()
                .as(RegResponseWithoutRequiredParamModel.class);

    }

    @Step("Запрос на регистрацию с методом GET")
    public RegResponseInvalidMethodModel invalidMethodRegister(SuccessfulRegistrationRequestModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .get("/api/v1/users/register/")
                .then()
                .spec(invalidRegistrationMethodResponseSpec)
                .extract()
                .as(RegResponseInvalidMethodModel.class);
    }

    @Step("Проверка контроля типа передаваемых данных при регистрации")
    public RegResponseUnsupportedMediaTypeModel invalidMediaTypeRegister(SuccessfulRegistrationRequestModel body) {
        return given(wrongMediaTypeRequestSpec)
                .body(body)
                .when()
                .post("/api/v1/users/register/")
                .then()
                .spec(unsupportedMediaTypeResponseSpec)
                .extract()
                .as(RegResponseUnsupportedMediaTypeModel.class);
    }

    @Step("Запрос на обновление данных пользователя")
    public SuccessfulUpdateUserResponseModel updateExistingUser
            (String token, SuccessfulUpdateUserRequestModel updateData) {
        return given(updateUserRequestSpec)
                .auth()
                .oauth2(token)
                .body(updateData)
                .when()
                .put("/api/v1/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(SuccessfulUpdateUserResponseModel.class);
    }

    @Step("Запрос на обновление данных пользователя без токена")
    public NotProvidedTokenOnUpdateUserResponseModel updateExistingUserWithoutToken
            (SuccessfulUpdateUserRequestModel updateData) {
        return given(updateUserRequestSpec)
                .body(updateData)
                .when()
                .put("/api/v1/users/me/")
                .then()
                .spec(notProvidedTokenOnUpdateUserResponseSpec)
                .extract()
                .as(NotProvidedTokenOnUpdateUserResponseModel.class);
    }
}
