import models.AuthData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

public class ApiTests {

    @Test
    void checkUserNameByGroovy() {

        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("data.findAll{it.email}.email.flatten().",
                        hasItem("emma.wong@reqres.in"));
    }

    @Test
    void createUser() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"kotlomoy\"\n" +
                        "}")
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .body("name", is("morpheus"))
                .body("job", is("kotlomoy"));
    }

    @Test
    void deleteUser() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void checkLoginUnSuccessful() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void checkLoginSuccessful() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkLoginSuccessfulWithModels() {
        AuthData data =
                given()
                        .spec(requestSpec)
                        .body("{\n" +
                                "    \"email\": \"eve.holt@reqres.in\",\n" +
                                "    \"password\": \"cityslicka\"\n" +
                                "}")
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec)
                        .extract().as(AuthData.class);

        assertEquals("QpwL5tke4Pnpja7X4", data.getToken());
    }

}