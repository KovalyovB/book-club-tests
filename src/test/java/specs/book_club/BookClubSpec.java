package specs.book_club;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class BookClubSpec {
    public static RequestSpecification successfulAddBookRequestSpec = baseRequestSpec;
    public static RequestSpecification successfulSearchBookRequestSpec = baseRequestSpec;
    public static RequestSpecification successfulUpdateBookDataRequestSpec = baseRequestSpec;
    public static RequestSpecification successfulDeleteBookDataRequestSpec = baseRequestSpec;


    public static ResponseSpecification successfulAddBookResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/book_club/successful_add_book_response_schema.json"))
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("description", notNullValue())
            .expectBody("telegramChatLink", notNullValue())
            .build();

    public static ResponseSpecification successfulSearchBookResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/book_club/successful_search_book_response_schema.json"))
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("description", notNullValue())
            .expectBody("telegramChatLink", notNullValue())
            .build();

    public static ResponseSpecification successfulUpdateBookDataResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/book_club/successful_update_book_data_response_schema.json"))
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("description", notNullValue())
            .expectBody("telegramChatLink", notNullValue())
            .build();

    public static ResponseSpecification searchMissingBookResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(404)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/book_club/search_missing_book_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();
}
