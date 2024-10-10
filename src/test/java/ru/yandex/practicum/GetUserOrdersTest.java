package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import ru.yandex.practicum.pojo.Ingredient;
import ru.yandex.practicum.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetUserOrdersTest {

    public static final String UNAUTHORIZED_MESSAGE = "You should be authorised";
    private final OrderClient client = new OrderClient();
    private final Ingredient ingredient = new Ingredient();
    private final ArrayList<String> ingredients = new ArrayList<>();
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private String accessToken;
    private String orderId;

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    public void getOrdersAuthUserTest() {
        User user = userGenerator.generateUser();
        ValidatableResponse accessResponse = userClient.createUser(user);
        accessToken = accessResponse.extract().jsonPath().getString("accessToken");
        List<String> allIngredientsIds = client.getIngredients().extract().jsonPath().getList("data._id", String.class);
        ingredients.add(allIngredientsIds.get(0));
        ingredients.add(allIngredientsIds.get(1));
        ingredient.setIngredients(ingredients);
        orderId = client.createOrderAuth(ingredient, accessToken).extract().jsonPath().getString("order._id");
        ValidatableResponse response = client.getUserOrders(accessToken);
        List<String> userOrderIds = response.extract().jsonPath().getList("orders._id", String.class);
        response.assertThat().statusCode(200).and().body("success", is(true));
        assertThat(userOrderIds, hasItem(orderId));
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void getOrdersNoAuthUserTest() {
        ValidatableResponse response = client.getUserOrdersNoAuth();
        response.assertThat().statusCode(401).and().body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

}
