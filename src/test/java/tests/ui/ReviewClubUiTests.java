package tests.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.book_club.SuccessfulAddBookRequestModel;
import models.book_club.SuccessfulAddBookResponseModel;
import models.book_club.SuccessfulAddClubReviewRequestModel;
import models.book_club.SuccessfulAddClubReviewResponseModel;
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

@DisplayName("Тесты на блок отзывов о книжном клубе")
public class ReviewClubUiTests extends UiTestBase {

    @Test
    @DisplayName("[UI] Проверка на корректность отображения отзыва")
    public void reviewsDisplayedCorrectly() {
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

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(accessToken, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse = api.clubs.addBookClubReview(accessToken, addClubReviewData);

        ReviewClubPage reviewClubPage = new ReviewClubPage();

        reviewClubPage.openPageWithPreparedStorage(localStorageAuthBody)
                .openBookClubByUrl(addBookResponse.id())
                .checkReviewsBlockDisplayed()
                .checkReviewRating(addClubReviewResponse.review(), addClubReviewResponse.assessment());
    }

    @Test
    @DisplayName("[UI] Проверка удаления отзыва о книжном клубе")
    public void reviewDeleteCorrectly() {
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

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(accessToken, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse =
                api.clubs.addBookClubReview(accessToken, addClubReviewData);

        ReviewClubPage reviewClubPage = new ReviewClubPage();

        reviewClubPage.openPageWithPreparedStorage(localStorageAuthBody)
                .openBookClubByUrl(addBookResponse.id())
                .checkReviewsBlockDisplayed()
                .deleteClubReview(addClubReviewResponse.review())
                .checkReviewDeleted(addClubReviewResponse.review());
    }
}
