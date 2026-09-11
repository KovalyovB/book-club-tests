package specs.update_user;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserSpec {
    public static RequestSpecification updateUserRequestSpec = with()
            .log().all()
            .contentType(JSON);

    public static ResponseSpecification successfulUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/update_user_data/successful_update_user_data_response.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("firstName", notNullValue())
            .expectBody("lastName", notNullValue())
            .expectBody("email", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification notProvidedTokenOnUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/update_user_data/not_provided_token_on_update_response.json"))
            .expectBody("detail", notNullValue())
            .build();
}
