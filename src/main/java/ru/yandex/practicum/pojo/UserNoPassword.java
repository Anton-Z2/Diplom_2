package ru.yandex.practicum.pojo;


public class UserNoPassword {
    private String email;
    private String name;

    private UserNoPassword(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserNoPassword fromUser(User user) {
        return new UserNoPassword(user.getEmail(), user.getName());
    }


}
