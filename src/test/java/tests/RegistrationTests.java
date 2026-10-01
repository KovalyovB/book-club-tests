package tests;

import models.registration.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.TestData.*;

@DisplayName("Тесты регистрации пользователя")
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
    @DisplayName("Успешная регистрация пользователя")
    public void successfulRegistrationTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(registrationData);

        step("Проверка корректности данных нового пользователя", () -> {
            String actualUserName = registrationResponse.username();
            assertThat(actualUserName).isEqualTo(username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");

            assertThat(registrationResponse.remoteAddr().matches(REGISTRATION_IP_REGEXP));
        });
    }

    @DisplayName("Проверка контроля уникальности пользователей")
    @Test
    public void existingUserWrongRegistrationTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = api.users.register(registrationData);
        step("Проверка имени зарегестрованного пользователя", () -> {
            assertThat(firstRegistrationResponse.username()).isEqualTo(username);
        });

        RegResponseExistingUserModel secondRegistrationResponse = api.users.existingUserRegister(registrationData);
        step("Проверка возврата ошибки о не уникальных данных", () -> {
            String actualError = secondRegistrationResponse.username().get(0);
            assertThat(actualError).isEqualTo(EXISTS_USER_REGISTRATION_MESSAGE);
        });
    }

    @Test
    @DisplayName("Проверка валидации обязательных параметров при регистрации")
    public void isRequiredFieldMissingTest() {
        RegRequestWithoutRequiredParamModel missingParameterData = new RegRequestWithoutRequiredParamModel(username);

        RegResponseWithoutRequiredParamModel response = api.users.missingParameterRegister(missingParameterData);

        step("Проверка возврата ошибки в ответе", () -> {
            assertEquals(REQUIRED_REGISTRATION_PARAMETER_ERROR, response.password().get(0));
        });
    }

    @Test
    @DisplayName("Проверка контроля метода запроса на регистрацию")
    public void invalidRegistrationMethodTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        RegResponseInvalidMethodModel response = api.users.invalidMethodRegister(registrationData);

        step("Проверка возврата ошибки в ответе", () -> {
            assertEquals(INVALID_REGISTRATION_METHOD_ERROR_MESSAGE, response.detail());
        });
    }

    @Test
    @DisplayName("Проверка контроля типа передаваемых данных при регистрации")
    public void unsupportedMediaTypeTest() {
        SuccessfulRegistrationRequestModel registrationData = new SuccessfulRegistrationRequestModel(username, password);

        RegResponseUnsupportedMediaTypeModel response = api.users.invalidMediaTypeRegister(registrationData);

        step("Проверка возврата ошибки в ответе", () -> {
            assertEquals(INVALID_REGISTRATION_FORMAT_ERROR_MESSAGE, response.detail());
        });
    }
}
