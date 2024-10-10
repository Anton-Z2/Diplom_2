package ru.yandex.practicum.pojo;

public class UserNoName {
    private String email;
    private String password;

    private UserNoName(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserNoName fromUser(User user) {
        return new UserNoName(user.getEmail(), user.getPassword());
    }

}
