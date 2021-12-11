package com.geekbrains.lesson3.DZ3;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageDeletionNegativeTests extends BaseTest {

    @BeforeEach
    void setUp() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .multiPart("title", "Sad")
                .expect()
                .statusCode(200)
                .when()
                .post(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test // в POSTMANе запрос на удаление с id=0 возвращает такой же error, но "success": false и "status": 400
    void ImageDeletionWrongIdNegativeTest() {
        Response response = given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/0", "nazilya")
                .prettyPeek();
        assertThat(response.jsonPath().getString("data.error"), equalTo("An ID is required."));

    }
    @Test // замена delete методом post - Unknown Action
    void ImageDeletionWrongMethodNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post(URL_DELETE, "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(400)
                .body("data.error", equalTo("Unknown Action"));
    }
    @Test // некорректное имя пользователя - почему-то 404 Not Found
    void ImageDeletionWrongUserNameNegativeqTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post(URL_DELETE, "nazilya1", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(404);
    }
    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete(URL_DELETE, "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
