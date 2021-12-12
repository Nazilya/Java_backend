package com.geekbrains.lesson4.DZ4;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.geekbrains.lesson4.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class FavoriteImagePositiveTests extends BaseTest {
    private MultiPartSpecification URLMultiPartSpec;
    static RequestSpecification requestSpecificationWithAuthWithURL;
    private Response response;
    private String deleteHash;

    @BeforeEach
    void setUp() {
        URLMultiPartSpec = new MultiPartSpecBuilder(URL_IMAGE)
                .controlName("image")
                .build();

        requestSpecificationWithAuthWithURL = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(URLMultiPartSpec)
                .build();

        response = given(requestSpecificationWithAuthWithURL, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageId = response.jsonPath().getString("data.id");
        deleteHash = response.jsonPath().getString("data.deleteHash");
    }
    @Test
    void FavoriteImagePositiveTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post(FAVORITE_IMAGE, imageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }
    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete(DELETE_IMAGE, "nazilya", imageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
