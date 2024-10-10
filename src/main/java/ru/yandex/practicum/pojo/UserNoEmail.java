package ru.yandex.practicum.pojo;


public class UserNoEmail {
    private String password;
    private String name;

    private UserNoEmail(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public static UserNoEmail fromUser(User user) {
        return new UserNoEmail(user.getPassword(), user.getName());
    }
}
