package de.outfit;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class OutfitAcceptanceTest {
    @BeforeClass
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        OutfitService.startServer("localhost", 8080, new Weather() {
            @Override
            public Temperature currentTemperatureForCity(String city) throws IOException {
                return new Temperature(19);
            }

            @Override
            public Temperature currentTemperatureForCityId(int id) throws IOException {
                throw new FileNotFoundException("Location not found");
            }

            @Override
            public Temperature currentTemperatureForLocation(double latitude, double longitude) throws IOException {
                return new Temperature(27);
            }

            @Override
            public Temperature currentTemperatureForZip(String zip) throws IOException {
                return new Temperature(0);
            }
        });
    }

    @Test
    public void outfitLevelOf19ShouldBe3() {
        given().
                header("Accept-Encoding", "application/json").
        when().
                get("/outfit?q=Karlsruhe").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("outfit_level", equalTo(3));
    }

    @Test
    public void outfitLevelOf0ShouldBe5() {
        given().
                header("Accept-Encoding", "application/json").
        when().
                get("/outfit?zip=123").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("outfit_level", equalTo(5));
    }

    @Test
    public void outfitLevelOf27ShouldBe1() {
        given().
                header("Accept-Encoding", "application/json").
        when().
                get("/outfit?lat=1&lon=1").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("outfit_level", equalTo(1));
    }

    @Test
    public void unknownLocationShouldResultInAnError() {
        given().
                header("Accept-Encoding", "application/json").
        when().
                get("/outfit?id=0").
        then().
                statusCode(404).
                contentType(ContentType.JSON).
                body("error", equalTo("Could not find location"));
    }

    @Test
    public void noLocationGivenShouldResultInAnError() {
        given().
                header("Accept-Encoding", "application/json").
        when().
                get("/outfit").
        then().
                statusCode(422).
                contentType(ContentType.JSON).
                body("error", equalTo("No location given"));
    }
}
