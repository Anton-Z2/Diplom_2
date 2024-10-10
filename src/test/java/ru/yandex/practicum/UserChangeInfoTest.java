package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import ru.yandex.practicum.pojo.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserChangeInfoTest {
    public static final String UNAUTHORIZED_MESSAGE = "You should be authorised";
    private final UserClient client = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();

    private User user = new User();
    private String accessToken;

    @Before
    public void setUp() {
        user = userGenerator.generateUser();
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void authUserСhangePasswordTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setPassword("Hello");
        ValidatableResponse secondResponse = client.changeInfoUser(user, accessToken);
        secondResponse.assertThat().statusCode(200).and().body("success", is(true));
        ValidatableResponse thirdResponse = client.loginUser(user);
        thirdResponse.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void authUserСhangeNameTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setName("Hello");
        ValidatableResponse secondResponse = client.changeInfoUser(user, accessToken);
        secondResponse.assertThat().statusCode(200).and().body("user.name", equalTo("Hello"));
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    public void authUserСhangeEmailTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setEmail("hello");
        ValidatableResponse secondResponse = client.changeInfoUser(user, accessToken);
        secondResponse.assertThat().statusCode(200).and().body("user.email", equalTo("hello"));
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    public void NoAuthUserСhangePasswordTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setPassword("Hello");
        ValidatableResponse secondResponse = client.changeInfoUserNoAuth(user);
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void NoAuthUserСhangeNameTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setName("Hello");
        ValidatableResponse secondResponse = client.changeInfoUserNoAuth(user);
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }

    @Test
    @DisplayName("Изменение email неавторизованного пользователя")
    public void NoAuthUserСhangeEmailTest() {
        ValidatableResponse firstResponse = client.createUser(user);
        accessToken = firstResponse.extract().jsonPath().getString("accessToken");
        user.setEmail("Hello");
        ValidatableResponse secondResponse = client.changeInfoUserNoAuth(user);
        secondResponse.assertThat().statusCode(401).and().body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
