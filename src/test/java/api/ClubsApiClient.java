package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.book_club.*;

import static io.restassured.RestAssured.given;
import static specs.book_club.BookClubSpec.*;

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
}
