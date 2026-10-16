package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.book_club.*;
import models.login.LoginRequestModel;

import static io.restassured.RestAssured.given;
import static specs.book_club.BookClubSpec.*;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;

public class ClubsApiClient {

    @Step("Запрос на добавление книги в клуб")
    public SuccessfulAddBookResponseModel addBook(String token, SuccessfulAddBookRequestModel body) {
        return given(successfulAddBookRequestSpec)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .post("/api/v1/clubs/")
                .then()
                .spec(successfulAddBookResponseSpec)
                .extract()
                .as(SuccessfulAddBookResponseModel.class);
    }

    @Step("Запрос на поиск добавленной в книжный клуб книги")
    public SuccessfulSearchBookResponseModel searchBook(String token, SuccessfulSearchBookRequestModel body, Integer id) {
        return given(successfulSearchBookRequestSpec)
                .auth()
                .oauth2(token)
                .body(body)
                .pathParam("id", id)
                .when()
                .get("/api/v1/clubs/{id}/")
                .then()
                .spec(successfulSearchBookResponseSpec)
                .extract()
                .as(SuccessfulSearchBookResponseModel.class);
    }

    @Step("Запрос на обновление добавленной в книжный клуб книги")
    public SuccessfulUpdateBookDataResponseModel updateBookData(String token, SuccessfulUpdateBookDataRequestModel body, Integer id) {
        return given(successfulUpdateBookDataRequestSpec)
                .auth()
                .oauth2(token)
                .body(body)
                .pathParam("id", id)
                .when()
                .put("/api/v1/clubs/{id}/")
                .then()
                .spec(successfulUpdateBookDataResponseSpec)
                .extract()
                .as(SuccessfulUpdateBookDataResponseModel.class);
    }

    @Step("Запрос на удаление добавленной в книжный клуб книги")
    public Response deleteBookData(String token, Integer id) {
        return given(successfulDeleteBookDataRequestSpec)
                .auth()
                .oauth2(token)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/clubs/{id}/")
                .then()
                .extract()
                .response();
    }

    @Step("Запрос на поиск отсутствующей в книжном клубе книги")
    public MissingBookSearchResponseModel searchMissingBook
            (String token, SuccessfulSearchBookRequestModel body, Integer id) {
        return given(successfulSearchBookRequestSpec)
                .auth()
                .oauth2(token)
                .body(body)
                .pathParam("id", id)
                .when()
                .get("/api/v1/clubs/{id}/")
                .then()
                .spec(searchMissingBookResponseSpec)
                .extract()
                .as(MissingBookSearchResponseModel.class);
    }

    @Step("Запрос на открытие страницы книжного клуба")
    public OpenBookClubPageResponseModel openBookClubPage(String token, Integer id) {
        return given(successfulOpenBookClubPageRequestSpec)
                .auth()
                .oauth2(token)
                .pathParam("id", id)
                .when()
                .get("/api/v1/clubs/{id}/")
                .then()
                .spec(openBookClubPageResponseSpec)
                .extract()
                .as(OpenBookClubPageResponseModel.class);
    }

    @Step("Запрос на добавление отзыва о книжном клубе")
    public SuccessfulAddClubReviewResponseModel addBookClubReview(String token, SuccessfulAddClubReviewRequestModel body) {
        return given(successfulAddClubReviewRequestSpec)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .post("/api/v1/clubs/reviews/")
                .then()
                .spec(openAddClubReviewResponseSpec)
                .extract()
                .as(SuccessfulAddClubReviewResponseModel.class);
    }

    @Step("Запрос на получения отзывов на книжный клуб")
    public SuccessfulGetClubReviewsResponseModel getClubReviewsData(String token, Integer id) {
        return given(successfulGetClubReviewRequestSpec)
                .auth()
                .oauth2(token)
                .pathParam("id", id)
                .when()
                .get("/api/v1/clubs/reviews/{id}/")
                .then()
                .spec(successfulGetClubReviewResponseSpec)
                .extract()
                .as(SuccessfulGetClubReviewsResponseModel.class);
    }

    @Step("Запрос на удаление отзыва на книжный клуб")
    public Response deleteClubReview(String token, Integer id) {
        return given(successfulDeleteClubReviewRequestSpec)
                .auth()
                .oauth2(token)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/clubs/reviews/{id}/")
                .then()
                .extract()
                .response();
    }

    @Step("Запрос отзывов по клубу без отзывов")
    public WrongGetClubReviewsResponseModel getWrongClubReviewsData(String token, Integer id) {
        return given(successfulGetClubReviewRequestSpec)
                .auth()
                .oauth2(token)
                .pathParam("id", id)
                .when()
                .get("/api/v1/clubs/reviews/{id}/")
                .then()
                .spec(wrongGetClubReviewResponseSpec)
                .extract()
                .as(WrongGetClubReviewsResponseModel.class);
    }

    @Step("Запрос на удаление отзыва на книжный клуб без токена")
    public String deleteClubReviewWithoutToken(Integer id) {
        return given(successfulDeleteClubReviewRequestSpec)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/clubs/reviews/{id}/")
                .then()
                .spec(deleteReviewWithoutTokenResponseSpec)
                .extract()
                .path("detail");
    }
}