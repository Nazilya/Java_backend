package com.geekbrains.lesson4.DZ4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static com.geekbrains.lesson4.Endpoints.*;

public class FavoriteImageNegativeTests extends BaseTest {

    @BeforeEach
    void setUp() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .multiPart("title", "Sad")
                .expect()
                .statusCode(200)
                .when()
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }
    @DisplayName("Некорректный id")
    @Test
    void FavoriteImageInvalidIdNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post(FAVORITE_IMAGE, 0)
                .prettyPeek()
                .then()
                .statusCode(400)
                .body("data.error", equalTo("No image data was sent to the upload api"));
    }
    @DisplayName("Некорректный url")
    @Test
    void FavoriteImageInvalidURLNegativeTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/favorite")
                .prettyPeek()
                .then()
                .statusCode(404);
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
