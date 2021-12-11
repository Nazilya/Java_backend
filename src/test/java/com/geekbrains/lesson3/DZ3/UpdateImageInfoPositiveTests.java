package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class UpdateImageInfoPositiveTests extends BaseTest {
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
    @Test //изменить название изображение
    void UpdateImageInformationURLPositiveTest() {
        given()
                .headers("Authorization", token)
                .multiPart("title", "Garden")
                .when()
                .post("https://api.imgur.com/3/image/{deletehash}", uploadedImageId)
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
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
