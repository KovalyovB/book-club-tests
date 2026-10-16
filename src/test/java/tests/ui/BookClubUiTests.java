package tests.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.book_club.*;
import models.localstorage.AuthModel;
import models.localstorage.UserLocalStorageModel;
import models.login.LoginRequestModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.SuccessfulRegistrationRequestModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ReviewClubPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Тесты по работе с библиотекой книжного клуба")
public class BookClubUiTests extends UiTestBase {

    @Test
    @DisplayName("[UI] Проверка ошибки - администратор не может покинуть клуб")
    public void cantLeaveClubAsAdmin() {
        SuccessfulRegistrationRequestModel registrationRequest = new SuccessfulRegistrationRequestModel(userName, userPassword);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(registrationRequest);

        LoginRequestModel loginData = new LoginRequestModel(userName, userPassword);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        String localStorageAuthBody = step("Создание JSON авторизации для localStorage", () -> {
            UserLocalStorageModel user = new UserLocalStorageModel(
                    registrationResponse.id(),
                    registrationResponse.username(),
                    registrationResponse.firstName(),
                    registrationResponse.lastName(),
                    registrationResponse.email(),
                    registrationResponse.remoteAddr()
            );
            AuthModel auth = new AuthModel(user, accessToken, refreshToken, true);
            return new ObjectMapper().writeValueAsString(auth);
        });

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        api.clubs.addBook(accessToken, requestBookData);

        ReviewClubPage reviewClubPage = new ReviewClubPage();

        reviewClubPage.openPageWithPreparedStorage(localStorageAuthBody)
                .openClubList()
                .searchClub(bookTitle)
                .navigateToBookClubPage(bookTitle)
                .checkClubContent()
                .leaveBookClub()
                .checkLeaveAdminError();
    }
}
