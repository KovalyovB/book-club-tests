package tests;

import models.login.LoginRequestModel;
import models.update_user.NotProvidedTokenOnUpdateUserResponseModel;
import models.update_user.SuccessfulUpdateUserRequestModel;
import models.update_user.SuccessfulUpdateUserResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
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

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (LOGIN_USERNAME, firstName, lastName, email);

        SuccessfulUpdateUserResponseModel userDataResponse = api.users.updateExistingUser(token, updateData);

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

        api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulUpdateUserRequestModel updateData = new SuccessfulUpdateUserRequestModel
                (LOGIN_USERNAME, firstName, lastName, email);

        NotProvidedTokenOnUpdateUserResponseModel missingTokenResponse = api.users.updateExistingUserWithoutToken(updateData);

        step("Проверка возврата ошибки об отсутствующем токене", () -> {
            assertThat(missingTokenResponse.detail()).isEqualTo(MISSING_TOKEN_UPDATE_USER_ERROR_MESSAGE);
        });
    }
}
