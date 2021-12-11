package com.geekbrains.lesson3.DZ3;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageUploadNegativeTests extends BaseTest {
    private final String PATH_TO_TXT = "src/test/resources/1.txt";
    String URL = "ttps://i.pinimg.com/originals/06/d2/c7/06d2c7e8e3ee8a12d1764ff2a52bdf4f.jpg";


    @Test//загрузка изображения методом delete
    void uploadFileImageNegativeTest() {
         error = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .when()
                .delete(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
        assertThat(error, equalTo("An ID is required."));
    }

    @Test//загрузка изображения - не указан путь к файлу
    void uploadNoFileImageNegativeTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image","\"/path/to/file\"")
                .expect()
                .statusCode(400)
                .body("data.error", equalTo("Invalid URL (\"/path/to/file\")"))
                .body("success", equalTo(false))
                .when()
                .post(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    @Test //загрузка изображения - файл txt
    void uploadFileImageTxtNegitiveTest() {
        Response response = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_TXT))
                .when()
                .post(URL_UPLOAD)
                .prettyPeek();
        assertThat(response.jsonPath().getString("data.error.message"), equalTo("File type invalid (1)"));
    }
    //загрузка изображения вместо видео
    @Test
    void uploadVideoURLNegativeTest() {
        error = given()
                .headers("Authorization", token)
                .multiPart("video", URL)
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .when()
                .post(URL_UPLOAD)
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
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", URL)
                .expect()
                .statusCode(400)
                .when()
                .post(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
}
