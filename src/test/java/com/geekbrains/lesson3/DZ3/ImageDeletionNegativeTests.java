package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ImageDeletionNegativeTests extends BaseTest {
    String uploadedImageId;

    @BeforeEach
    void setUp() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/sad.jpg"))
                .multiPart("title", "Sad")
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test // в POSTMANе запрос на удаление с id=0 возвращает такой же error, но "success": false и "status": 400
    void ImageDeletionWrongIdNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/0", "nazilya")
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Test // замена delete методом post - Unknown Action
    void ImageDeletionWrongMethodNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/account/{username}/image/{deletehash}", "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(400);
    }
    @Test // некорректное имя пользователя - почему-то 404 Not Found
    void ImageDeletionWrongUserNameNegativeqTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/account/{username}/image/{deletehash}", "nazilya1", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(404);
    }
    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
