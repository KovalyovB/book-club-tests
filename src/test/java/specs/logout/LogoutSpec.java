package specs.logout;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LogoutSpec {
    public static RequestSpecification successfulLogoutRequestSpec = with()
            .log().all()
            .contentType(JSON);

    public static ResponseSpecification successfulLogoutResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("isEmpty()", is(true))
            .build();

    public static ResponseSpecification wrongRefreshTokenResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath
                    ("schemas/logout/wrong_refresh_token_response_schema.json"))
            .expectBody("detail", notNullValue())
            .expectBody("code", notNullValue())
            .build();

}
