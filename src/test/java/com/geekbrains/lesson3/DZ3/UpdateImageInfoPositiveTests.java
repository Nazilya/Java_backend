package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateImageInfoPositiveTests extends BaseTest {

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
    @Test //изменить название изображение
    void UpdateImageInformationURLPositiveTest() {
        given()
                .headers("Authorization", token)
                .multiPart("title", "Garden")
                .when()
                .post(URL_UPDATE, uploadedImageId)
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
                .delete(URL_DELETE, "nazilya", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
