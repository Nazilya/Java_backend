package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class UpdateImageInfoNegativeTests extends BaseTest {
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
    @Test // некорректный deletehash
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
    @Test // некорректный token
    void UpdateImageInformationTokenInvalidNegativeTest() {
        given()
                .headers("Authorization", 123)
                .multiPart("title", "Garden")
                .when()
                .post(URL_UPDATE, uploadedImageId)
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
                .delete(URL_DELETE, "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
