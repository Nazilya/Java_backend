package com.geekbrains.lesson3.DZ3;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;
    String uploadedImageId;
    String error;
    final String PATH_TO_IMAGE = "src/test/resources/reka.jpg";
    final String URL_DELETE = "https://api.imgur.com/3/account/{username}/image/{deleteHash}";
    final String URL_UPLOAD = "https://api.imgur.com/3/image";
    final String URL_FAVORITE = "https://api.imgur.com/3/image/{imageHash}/favorite";
    final String URL_UPDATE = "https://api.imgur.com/3/image/{deleteHash}";
    String URL = "https://i.pinimg.com/originals/06/d2/c7/06d2c7e8e3ee8a12d1764ff2a52bdf4f.jpg";

    @BeforeAll
    static void BeforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
