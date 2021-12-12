package com.geekbrains.lesson4.DZ4;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.geekbrains.lesson4.Endpoints.GET_ACCOUNT;

public class AccountTests extends BaseTest {
    @Test
    void getAccountInfoTest() {
        given(requestWithAuth, positiveResponseSpecification)
                .get(GET_ACCOUNT, username);

    }
    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .contentType("application/json")
                .body("data.url", equalTo(username))
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .when()
                .get(GET_ACCOUNT, username)
                .prettyPeek();
    }
    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(GET_ACCOUNT, username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"), equalTo(username));
    }
}
