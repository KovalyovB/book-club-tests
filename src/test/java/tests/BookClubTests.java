package tests;

import models.book_club.*;
import models.login.LoginRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static tests.TestData.*;

@DisplayName("Тесты по работе с библиотекой книжного клуба")
public class BookClubTests extends TestBase {

    String bookTitle;
    String bookAuthors;
    Integer publicationYear;
    String description;
    String telegramChatLink;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        bookTitle = faker.book().title() + " №1";
        bookAuthors = faker.book().author();
        publicationYear = faker.number().numberBetween(1900, 2025);
        description = faker.book().title();
        telegramChatLink = faker.internet().url();
    }

    @Test
    @DisplayName("Добавление книги в библиотеку клуба")
    public void successfulAddBookTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        step("Проверка корректности добавления книги в библиотеку клуба", () -> {
            assertThat(addBookResponse.bookTitle(), is(bookTitle));
            assertThat(addBookResponse.bookAuthors(), is(bookAuthors));
            assertThat(addBookResponse.publicationYear(), is(publicationYear));
            assertThat(addBookResponse.description(), is(description));
            assertThat(addBookResponse.telegramChatLink(), is(telegramChatLink));
        });
    }

    @Test
    @DisplayName("Поиск добавленой в бибилиотеку клуба книги")
    public void successfulSearchBookTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        SuccessfulSearchBookRequestModel requestSearchData =
                new SuccessfulSearchBookRequestModel(addBookResponse.id());

        SuccessfulSearchBookResponseModel responseSearchData = api.clubs.searchBook(token, requestSearchData, addBookResponse.id());

        step("Проверка возврата корректной информации о книге", () -> {
            assertThat(responseSearchData.bookTitle(), is(bookTitle));
            assertThat(responseSearchData.bookAuthors(), is(bookAuthors));
            assertThat(responseSearchData.publicationYear(), is(publicationYear));
            assertThat(responseSearchData.description(), is(description));
            assertThat(responseSearchData.telegramChatLink(), is(telegramChatLink));
        });
    }

    @Test
    @DisplayName("Редактирование книги после создания")
    public void successfulUpdateBookDataTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        Faker faker = new Faker();
        String newBookTitle = faker.book().title() + " №2";
        String newBookAuthors = faker.book().author();
        Integer newPublicationYear = faker.number().numberBetween(1900, 2025);
        String newDescription = faker.book().title();
        String newTelegramChatLink = faker.internet().url();

        SuccessfulUpdateBookDataRequestModel updateBookDataRequest =
                new SuccessfulUpdateBookDataRequestModel
                        (newBookTitle, newBookAuthors, newPublicationYear, newDescription, newTelegramChatLink);

        SuccessfulUpdateBookDataResponseModel updateBookDataResponse =
                api.clubs.updateBookData(token, updateBookDataRequest, addBookResponse.id());

        step("Проверка возврата обновленной информации о книге", () -> {
            assertThat(updateBookDataResponse.bookTitle(), is(newBookTitle));
            assertThat(updateBookDataResponse.bookAuthors(), is(newBookAuthors));
            assertThat(updateBookDataResponse.publicationYear(), is(newPublicationYear));
            assertThat(updateBookDataResponse.description(), is(newDescription));
            assertThat(updateBookDataResponse.telegramChatLink(), is(newTelegramChatLink));
            assertThat(updateBookDataResponse.id(), is(addBookResponse.id()));
        });
    }

    @Test
    @DisplayName("Удаление книги из книжного клуба")
    public void successfulDeleteBookDataTest() {
        LoginRequestModel loginData = new LoginRequestModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String token = api.auth.loginAndGetRefreshTokenForUpdate(loginData);

        SuccessfulAddBookRequestModel requestBookData =
                new SuccessfulAddBookRequestModel(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);

        SuccessfulAddBookResponseModel addBookResponse = api.clubs.addBook(token, requestBookData);

        api.clubs.deleteBookData(token, addBookResponse.id());

        SuccessfulSearchBookRequestModel requestSearchData =
                new SuccessfulSearchBookRequestModel(addBookResponse.id());

        MissingBookSearchResponseModel notFoundBookError =
                api.clubs.searchMissingBook(token, requestSearchData, addBookResponse.id());

        step("Проверка возвращения ошибки, книга отсутствует", () -> {
            assertThat(notFoundBookError.detail(), is(BOOK_NOT_FOUND_ERROR));
        });
    }
}
