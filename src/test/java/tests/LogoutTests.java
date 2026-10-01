package tests;

import models.login.LoginRequestModel;
import models.logout.SuccessfulLogoutRequestModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@DisplayName("Тесты по выходу из системы")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из системы")
    public void successfulLogoutTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        SuccessfulLogoutRequestModel logoutData = new SuccessfulLogoutRequestModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test
    @DisplayName("Попытка выхода из системы с некорректным токеном")
    public void wrongRefreshTokenLogoutTest() {
        SuccessfulLogoutRequestModel logoutData = new SuccessfulLogoutRequestModel(WRONG_REFRESH_TOKEN);

        WrongRefreshTokenLogoutResponseModel logoutResponse = api.auth.logoutWithWrongToken(logoutData);

        step("Проверка возврата ошибки о не валидном токене", () -> {
            assertThat(logoutResponse.detail()).isEqualTo(EXPECTED_TOKEN_DETAIL_ERROR_MESSAGE);
            assertThat(logoutResponse.code()).isEqualTo(EXPECTED_TOKEN_CODE_ERROR_MESSAGE);
        });
    }
}
