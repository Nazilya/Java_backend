package com.geekbrains.lesson4.DZ4;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.Base64;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static com.geekbrains.lesson4.Endpoints.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateImageInfoPositiveTests extends BaseTest {
    private MultiPartSpecification base64MultiPartSpec;
    private Response response;
    private String deleteHash;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    static String encodedFile;

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
                .param("title", "Garden")
                .expect()
                .statusCode(200)
                .when()
                .put(UPDATE_IMAGE, imageId)
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
                .get(UPDATE_IMAGE, imageId)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.title");

        assertThat("Title", title, equalTo("Garden"));
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
