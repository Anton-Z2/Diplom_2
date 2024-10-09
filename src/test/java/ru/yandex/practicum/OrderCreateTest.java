package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import ru.yandex.practicum.pojo.Ingredient;
import ru.yandex.practicum.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class OrderCreateTest {

    public static final String NO_INGREDIENT_MESSAGE = "Ingredient ids must be provided";
    private final OrderClient client = new OrderClient();
    private final Ingredient ingredient = new Ingredient();
    private final ArrayList<String> ingredients = new ArrayList<>();
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private User user = new User();
    private String accessToken;

    @Before
    public void setUp() {
        user = userGenerator.generateUser();
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами, с авторизацией")
    public void orderWithIngredientsCreateAuthTest() {
        ValidatableResponse accessResponse = userClient.createUser(user);
        accessToken = accessResponse.extract().jsonPath().getString("accessToken");
        List<String> allIngredientsIds = client.getIngredients().extract().jsonPath().getList("data._id", String.class);
        ingredients.add(allIngredientsIds.get(0));
        ingredients.add(allIngredientsIds.get(1));
        ingredient.setIngredients(ingredients);
        ValidatableResponse response = client.createOrderAuth(ingredient, accessToken);
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    public void orderWithIngredientsCreateNoAuthTest() {
        List<String> allIngredientsIds = client.getIngredients().extract().jsonPath().getList("data._id", String.class);
        ingredients.add(allIngredientsIds.get(0));
        ingredients.add(allIngredientsIds.get(1));
        ingredient.setIngredients(ingredients);
        ValidatableResponse response = client.createOrderNoAuth(ingredient);
        response.assertThat().statusCode(200).and().body("success", is(true));
    }


    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, с авторизацией")
    public void orderWrongIngredientsCreateAuthTest() {
        ValidatableResponse accessResponse = userClient.createUser(user);
        accessToken = accessResponse.extract().jsonPath().getString("accessToken");
        List<String> allIngredientsIds = client.getIngredients().extract().jsonPath().getList("data.calories", String.class);
        ingredients.add(allIngredientsIds.get(0));
        ingredients.add(allIngredientsIds.get(1));
        ingredient.setIngredients(ingredients);
        ValidatableResponse response = client.createOrderAuth(ingredient, accessToken);
        response.assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, без авторизации")
    public void orderWrongIngredientsCreateNoAuthTest() {
        List<String> allIngredientsIds = client.getIngredients().extract().jsonPath().getList("data.calories", String.class);
        ingredients.add(allIngredientsIds.get(0));
        ingredients.add(allIngredientsIds.get(1));
        ingredient.setIngredients(ingredients);
        ValidatableResponse response = client.createOrderNoAuth(ingredient);
        response.assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, с авторизацией")
    public void orderNoIngredientsCreateAuthTest() {
        ValidatableResponse accessResponse = userClient.createUser(user);
        accessToken = accessResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse response = client.createOrderAuth(ingredient, accessToken);
        response.assertThat().statusCode(400).and().body("message", equalTo(NO_INGREDIENT_MESSAGE));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, без авторизации")
    public void orderNoIngredientsCreateNoAuthTest() {
        ValidatableResponse response = client.createOrderNoAuth(ingredient);
        response.assertThat().statusCode(400).and().body("message", equalTo(NO_INGREDIENT_MESSAGE));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

}
