package com.geekbrains.lesson4.DZ4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.geekbrains.lesson4.Endpoints.*;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class UpdateImageInfoNegativeTests extends BaseTest {
    String error;

    @BeforeEach
    void setUp() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart(UPLOAD_IMAGE, new File(PATH_TO_IMAGE))
                .multiPart("title", "Sad")
                .expect()
                .statusCode(200)
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    @DisplayName("некорректный deletehash")
    @Test
    void UpdateImageInformationNegativeTest() {
        error = given()
                .headers("Authorization", token)
                .multiPart("title", "Garden")
                .when()
                .post("https://api.imgur.com/3/image/0")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("No image data was sent to the upload api"));
    }
    @DisplayName("некорректный token")
    @Test
    void UpdateImageInformationTokenInvalidNegativeTest() {
        given()
                .headers("Authorization", 123)
                .multiPart("title", "Garden")
                .when()
                .post(UPDATE_IMAGE, imageId)
                .prettyPeek()
                .then()
                .statusCode(403)
                .body("data.error", equalTo("Malformed auth header"));
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
