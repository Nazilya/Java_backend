package com.geekbrains.lesson4.DZ4;

import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static com.geekbrains.lesson4.Endpoints.*;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImageUploadPositiveTests extends BaseTest {
    static String encodedFile;
    private MultiPartSpecification PATHMultiPartSpec;
    static RequestSpecification requestSpecificationWithAuthWithPATH;
    private Response response;
    //private String deleteHash;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
    }

    @DisplayName("Загрузка изображения, закодированного Base64")
    @Test
    void uploadFileTest() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Загрузка изображения с resources")
    @Test
    void uploadFileImageTest() {
        PATHMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_IMAGE))
                .controlName("image")
                .build();

        requestSpecificationWithAuthWithPATH = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(PATHMultiPartSpec)
                .build();

        response = given(requestSpecificationWithAuthWithPATH, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageId = response.jsonPath().getString("data.id");
        //deleteHash = response.jsonPath().getString("data.deleteHash");
        assertThat(response.jsonPath().getString("status"), equalTo("200"));
        assertThat(response.jsonPath().getString("data.type"), equalTo("image/jpeg"));
    }

    @DisplayName("Загрузка изображения с URL")
    @Test
    void uploadFileURLTest() {
        imageId = given()
                .headers("Authorization", token)
                .multiPart("image", URL_IMAGE)
                .multiPart("title", "Sakura")
                .expect()
                .statusCode(200)
                .body("data.title", equalTo("Sakura"))
                .when()
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
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

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
