package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.pojo.Ingredient;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    public static final String ORDERS_ENDPOINT = "/api/orders";

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .baseUri(BASE_URI)
                .get(INGREDIENTS_ENDPOINT)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderNoAuth(Ingredient ingredient) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(ingredient)
                .post(ORDERS_ENDPOINT)
                .then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderAuth(Ingredient ingredient, String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(ingredient)
                .post(ORDERS_ENDPOINT)
                .then();
    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .get(ORDERS_ENDPOINT)
                .then();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getUserOrdersNoAuth() {
        return given()
                .baseUri(BASE_URI)
                .get(ORDERS_ENDPOINT)
                .then();
    }

}
