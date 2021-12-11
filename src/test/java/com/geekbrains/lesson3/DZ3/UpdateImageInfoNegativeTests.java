package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class UpdateImageInfoNegativeTests extends BaseTest {
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
    @Test // некорректный deletehash - No image data was sent to the upload api
    void UpdateImageInformationNegativeTest() {
        given()
                .headers("Authorization", token)
                .multiPart("title", "Garden")
                .when()
                .post("https://api.imgur.com/3/image/0")
                .prettyPeek()
                .then()
                .statusCode(400)
                .body("data.error", equalTo("No image data was sent to the upload api"));
    }
    @Test // некорректный token - Malformed auth header
    void UpdateImageInformationTokenInvalidNegativeTest() {
        given()
                .headers("Authorization", 123)
                .multiPart("title", "Garden")
                .when()
                .post("https://api.imgur.com/3/image/{deleteHash}", uploadedImageId)
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
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
