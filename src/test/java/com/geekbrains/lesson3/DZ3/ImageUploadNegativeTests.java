package com.geekbrains.lesson3.DZ3;

import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageUploadNegativeTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/reka.jpg";
    private final String PATH_TO_TXT = "src/test/resources/1.txt";
    static String encodedFile;
    String uploadedImageId;
    String URL = "ttps://i.pinimg.com/originals/06/d2/c7/06d2c7e8e3ee8a12d1764ff2a52bdf4f.jpg";

    @Test//загрузка изображения методом delete
    void uploadFileImageNegativeTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(400)
                .body("data.error", equalTo("An ID is required."))
                .body("success", equalTo(false))
                .when()
                .delete("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
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
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }
    @Test //загрузка изображения - файл txt
    void uploadFileImageTxtNegitiveTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_TXT))
                .expect()
                .statusCode(400)
                .body("data.error.message", equalTo("File type invalid (1)"))
                .body("data.error.type", equalTo("ImgurException"))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error.message");
    }
    //загрузка изображения вместо видео
    @Test
    void uploadVideoURLNegativeTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("video", URL)
                .expect()
                .statusCode(400)
                .body("data.error", equalTo("No image data was sent to the upload api"))
                .body("success", equalTo(false))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
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
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error");
    }
}
