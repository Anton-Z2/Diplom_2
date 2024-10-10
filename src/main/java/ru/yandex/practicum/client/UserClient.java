package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.pojo.*;

import static io.restassured.RestAssured.given;

public class UserClient {

    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String REGISTER_ENDPOINT = "/api/auth/register";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_ENDPOINT = "/api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER_ENDPOINT)
                .then();
    }

    @Step("Создание пользователя без логина")
    public ValidatableResponse createUserNoName(UserNoName userNoName) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoName)
                .post(REGISTER_ENDPOINT)
                .then();
    }

    @Step("Создание пользователя без email")
    public ValidatableResponse createUserNoEmail(UserNoEmail userNoEmail) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoEmail)
                .post(REGISTER_ENDPOINT)
                .then();
    }

    @Step("Создание пользователя без пароля")
    public ValidatableResponse createUserNoPassword(UserNoPassword userNoPassword) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoPassword)
                .post(REGISTER_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(user)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя без логина")
    public ValidatableResponse loginUserNoName(UserNoName userNoName) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoName)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя без пароля")
    public ValidatableResponse loginUserNoPassword(UserNoPassword userNoPassword) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoPassword)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя без email")
    public ValidatableResponse loginUserNoEmail(UserNoEmail userNoEmail) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(userNoEmail)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeInfoUser(User user, String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(user)
                .patch(USER_ENDPOINT)
                .then();
    }

    @Step("Изменение данных неавторизованного пользователя")
    public ValidatableResponse changeInfoUserNoAuth(User user) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(user)
                .patch(USER_ENDPOINT)
                .then();
    }


    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .delete(USER_ENDPOINT)
                .then();
    }
}
