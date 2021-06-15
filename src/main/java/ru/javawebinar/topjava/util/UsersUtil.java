package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {

    public static final Integer USER_ID_1 = 1;
    public static final Integer USER_ID_2 = 2;

    public static final List<User> users = Arrays.asList(
            new User(USER_ID_1, "admin1", "password1", "admin1@mail.ru", Role.ADMIN),
            new User(USER_ID_2, "user2", "password2", "user2@mail.ru", Role.USER)
    );
}
