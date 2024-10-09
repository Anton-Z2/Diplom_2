package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import ru.yandex.practicum.pojo.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserLoginTest {

    public static final String WRONG_CREDENTIALS = "email or password are incorrect";
    private final UserClient client = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();

    private User user = new User();
    private String accessToken;

    @Before
    public void setUp() {
        user = userGenerator.generateUser();
    }

    @Test
    @DisplayName("Авторизация корректного пользователя")
    public void userLoginTest() {
        client.createUser(user);
        ValidatableResponse response = client.loginUser(user);
        accessToken = response.extract().jsonPath().getString("accessToken");
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    public void userNoNameLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse secondResponse = client.loginUserNoName(UserNoName.fromUser(user));
        secondResponse.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильным логином")
    public void userIncorrectNameLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setName("AntonZubchevskii");
        ValidatableResponse secondResponse = client.loginUser(user);
        secondResponse.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    public void userNoPasswordLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse secondResponse = client.loginUserNoPassword(UserNoPassword.fromUser(user));
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(WRONG_CREDENTIALS));
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильным паролем")
    public void userIncorrectPasswordLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setPassword("AntonZubchevskii");
        ValidatableResponse secondResponse = client.loginUser(user);
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(WRONG_CREDENTIALS));
    }

    @Test
    @DisplayName("Авторизация пользователя без email")
    public void userNoEmailLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse secondResponse = client.loginUserNoEmail(UserNoEmail.fromUser(user));
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(WRONG_CREDENTIALS));
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильным email")
    public void userIncorrectEmailLoginTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setEmail("AntonZubchevskii");
        ValidatableResponse secondResponse = client.loginUser(user);
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(WRONG_CREDENTIALS));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
