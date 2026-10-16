package tests.api;

import models.book_club.*;
import models.login.LoginRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static tests.TestData.*;

@DisplayName("Тесты по оценкам книжных клубов")
public class ReviewClubTests extends ApiTestBase {

    @Test
    @DisplayName("[API] Добавление отзыва по книжному клубу")
    public void successfulAddBookReviewTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse = api.clubs.addBookClubReview(token, addClubReviewData);

        step("Проверка возврата корректных данных по отзыву на клуб", () -> {
            assertThat(clubReview, is(addClubReviewResponse.review()));
            assertThat(assessment, is(addClubReviewResponse.assessment()));
            assertThat(readPages, is(addClubReviewResponse.readPages()));
            assertThat(assessment, is(addClubReviewResponse.assessment()));
        });
    }

    @Test
    @DisplayName("[API] Проверка возврата отзыва на книжный клуб")
    public void successfulGetBookReviewsTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse = api.clubs.addBookClubReview(token, addClubReviewData);

        SuccessfulGetClubReviewsResponseModel getClubReviews = api.clubs.getClubReviewsData(token, addClubReviewResponse.id());

        step("Проверка возврата добавленных отзывов на книжный клуб", () -> {
            assertThat(addClubReviewResponse.id(), is(getClubReviews.id()));
            assertThat(addClubReviewResponse.club(), is(getClubReviews.club()));
            assertThat(addClubReviewResponse.user(), is(getClubReviews.user()));
            assertThat(addClubReviewResponse.review(), is(getClubReviews.review()));
            assertThat(addClubReviewResponse.assessment(), is(getClubReviews.assessment()));
            assertThat(addClubReviewResponse.readPages(), is(getClubReviews.readPages()));
            assertThat(addClubReviewResponse.created(), is(getClubReviews.created()));
            assertThat(addClubReviewResponse.modified(), is(getClubReviews.modified()));
        });
    }

    @Test
    @DisplayName("[API] Проверка успешного удаления отзыва на книжный клуб")
    public void successfulDeleteClubReviewTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse = api.clubs.addBookClubReview(token, addClubReviewData);

        api.clubs.deleteClubReview(token, addClubReviewResponse.id());

        WrongGetClubReviewsResponseModel wrongGetClubReviews = api.clubs.getWrongClubReviewsData(token, addClubReviewResponse.id());

        step("Проверка возврата ошибки - отзыв не существует", () -> {
            assertThat(NO_CLUB_REVIEW_MESSAGE, is(wrongGetClubReviews.detail()));
        });
    }

    @Test
    @DisplayName("[API] Проверка успешного удаления отзыва на книжный клуб")
    public void wrongDeleteClubReviewWithoutTokenTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        SuccessfulAddClubReviewRequestModel addClubReviewData =
                new SuccessfulAddClubReviewRequestModel(addBookResponse.id(), clubReview, assessment, readPages);

        SuccessfulAddClubReviewResponseModel addClubReviewResponse = api.clubs.addBookClubReview(token, addClubReviewData);

        String deleteReviewWithoutTokenResponse = api.clubs.deleteClubReviewWithoutToken(addClubReviewResponse.id());

        step("Проверка ошибки при поптыке удаления отзыва без токена", () -> {
            assertThat(DELETE_REVIEW_WITHOUT_TOKEN_MESSAGE, is(deleteReviewWithoutTokenResponse));
        });
    }
}
