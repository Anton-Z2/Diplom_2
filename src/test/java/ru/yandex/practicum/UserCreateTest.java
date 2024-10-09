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

public class UserCreateTest {

    public static final String USER_EXISTS = "User already exists";
    public static final String REQUIRED_FIELDS = "Email, password and name are required fields";
    private final UserClient client = new UserClient();
    private User user = new User();
    private final UserGenerator userGenerator = new UserGenerator();

    private String accessToken;

    @Before
    public void setUp() {
        user = userGenerator.generateUser();
    }

    @Test
    @DisplayName("Создание пользователя")
    public void userCreateTest() {
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().jsonPath().getString("accessToken");
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void twoIdenticalUserCreateTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse secondResponse = client.createUser(user);
        secondResponse.assertThat().statusCode(403).and().body("message", equalTo(USER_EXISTS));
    }

    @Test
    @DisplayName("Создание пользователя без логина")
    public void userNoNameCreateTest() {
        ValidatableResponse response = client.createUserNoName(UserNoName.fromUser(user));
        response.assertThat().statusCode(403).and().body("message", equalTo(REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void userNoEmailCreateTest() {
        ValidatableResponse response = client.createUserNoEmail(UserNoEmail.fromUser(user));
        response.assertThat().statusCode(403).and().body("message", equalTo(REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void userNoPasswordCreateTest() {
        ValidatableResponse response = client.createUserNoPassword(UserNoPassword.fromUser(user));
        response.assertThat().statusCode(403).and().body("message", equalTo(REQUIRED_FIELDS));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
