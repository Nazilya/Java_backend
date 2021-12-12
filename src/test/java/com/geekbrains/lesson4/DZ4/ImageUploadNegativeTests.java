package com.geekbrains.lesson4.DZ4;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageUploadNegativeTests extends BaseTest {
    private final String PATH_TO_TXT = "src/test/resources/1.txt";
    private final String URL_INVALID = "ttps://i.pinimg.com/originals/06/d2/c7/06d2c7e8e3ee8a12d1764ff2a52bdf4f.jpg";
    private final String INVALID_PATH = "src/test/resources/sad1.jpg";
    String error;

    @DisplayName("Загрузка изображения методом delete")
    @Test
    void uploadFileImageNegativeTest() {
         error = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .when()
                .delete("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("An ID is required."));
    }
    @DisplayName("Загрузка изображения. Не указан путь к файлу")
    @Test
    void uploadNoFileImageNegativeTest() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart("image",INVALID_PATH)
                .expect()
                .statusCode(400)
                .body("data.error", equalTo("Invalid URL (src/test/resources/sad1.jpg)"))
                .body("success", equalTo(false))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    @DisplayName("Загрузка изображения. Файл txt")
    @Test
    void uploadFileImageTxtNegitiveTest() {
        Response response = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_TXT))
                .when()
                .post("/image")
                .prettyPeek();
        assertThat(response.jsonPath().getString("data.error.message"), equalTo("File type invalid (1)"));
    }
    @DisplayName("Загрузка изображения вместо видео")
    @Test
    void uploadVideoURLNegativeTest() {
        error = given()
                .headers("Authorization", token)
                .multiPart("video", URL_IMAGE)
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("No image data was sent to the upload api"));
    }

    //загрузка изображения (ошибка в URL)
    @Test
    void uploadFileURLNegativeTest() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart("image", URL_INVALID)
                .expect()
                .statusCode(400)
                .body("data.error", equalTo("Unable to process upload!"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
}
