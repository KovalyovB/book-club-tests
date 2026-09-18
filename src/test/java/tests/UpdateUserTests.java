package tests;

import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.NotProvidedTokenOnUpdateUserResponseModel;
import models.update_user.SuccessfulUpdateUserRequestModel;
import models.update_user.SuccessfulUpdateUserResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.update_user.UpdateUserSpec.*;
import static tests.TestData.*;

@DisplayName("Тесты обновления учетных данных пользователя")
public class UpdateUserTests extends TestBase {

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
    @DisplayName("Успешное обновление учетных данных пользователя")
    public void successfulUpdateUserInfoTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String loginResponse = step("Успешная авторизация и получение токена", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/api/v1/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().path("access"));

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (LOGIN_USERNAME, firstName, lastName, email);

        SuccessfulUpdateUserResponseModel userDataResponse = step("Запрос на обновление данных пользователя", () ->
                given(updateUserRequestSpec)
                        .auth()
                        .oauth2(loginResponse)
                        .body(updateData)
                        .when()
                        .put("/api/v1/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract()
                        .as(SuccessfulUpdateUserResponseModel.class)
        );

        step("Проверка корректности обновленных данных", () -> {
            assertThat(userDataResponse.username()).isEqualTo(LOGIN_USERNAME);
            assertThat(userDataResponse.firstName()).isEqualTo(firstName);
            assertThat(userDataResponse.lastName()).isEqualTo(lastName);
            assertThat(userDataResponse.email()).isEqualTo(email);
            assertThat(userDataResponse.username()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Проверка контроля токена при обновлении")
    public void notProvidedTokenOnUpdateUserTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        step("Успешная авторизация пользователя", () -> {
            given(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/api/v1/auth/token/")
                    .then()
                    .spec(successfulLoginResponseSpec)
                    .extract()
                    .as(SuccessfulLoginResponseModel.class);
        });

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (LOGIN_USERNAME, firstName, lastName, email);

        NotProvidedTokenOnUpdateUserResponseModel missingTokenResponse = step("Запрос на обновление с отсутствующим токеном", () ->
                given(updateUserRequestSpec)
                        .body(updateData)
                        .when()
                        .put("/api/v1/users/me/")
                        .then()
                        .spec(notProvidedTokenOnUpdateUserResponseSpec)
                        .extract()
                        .as(NotProvidedTokenOnUpdateUserResponseModel.class)
        );

        step("Проверка возврата ошибки об отсутствующем токене", () -> {
            assertThat(missingTokenResponse.detail()).isEqualTo(MISSING_TOKEN_UPDATE_USER_ERROR_MESSAGE);
        });
    }
}
