package com.geekbrains.lesson4;

//import org.apache.commons.io.FileUtils;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.*;
//import java.io.File;
//import java.io.IOException;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.CoreMatchers.equalTo;
import static com.geekbrains.lesson4.Endpoints.UPLOAD_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateImageTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/reka.jpg";
    static String imageId;
    private MultiPartSpecification base64MultiPartSpec;
    static String encodedFile;
    private Response response;
    private String deleteHash;
    //String uploadedImageId;

    @BeforeEach
    void setUp() {
        byte[] byteArray = getFileContent(PATH_TO_IMAGE);
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();

        response = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageId = response.jsonPath().getString("data.id");
        deleteHash = response.jsonPath().getString("data.deleteHash");
    }

    @DisplayName("Изменения title")
    @Test
    void updateFileTest() {
        given()
                .headers("Authorization", token)
                .param("title", "Heart")
                .expect()
                .statusCode(200)
                .when()
                .put("https://api.imgur.com/3/image/{imageHash}", imageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response();


    String title = given()
            .headers("Authorization", token)
            .expect()
            .statusCode(200)
            .when()
            .get("https://api.imgur.com/3/image/{imageHash}", imageId)
            .prettyPeek()
            .then()
            .extract()
            .response()
            .jsonPath()
            .getString("data.title");

    assertThat("Title", title, equalTo("Heart"));
    }

}