package ru.yandex.practicum.generator;

import ru.yandex.practicum.pojo.User;

import java.util.Random;

public class UserGenerator {

    Random random = new Random();

    public User generateUser() {
        return new User(generateEmail(), generatePassword(), generateName());
    }

    private String generateName() {
        return ("avzname" + random.nextInt(1000));
    }

    private String generateEmail() {
        return ("avzemail" + random.nextInt(1000) + "@yandex.ru");
    }

    private String generatePassword() {
        return ("avzpass" + random.nextInt(1000));
    }


}
