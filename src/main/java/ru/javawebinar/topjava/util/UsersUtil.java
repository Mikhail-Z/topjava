package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersUtil {

    public static final Integer ADMIN_ID = 1;
    public static final Integer USER_ID = 2;

    public static final List<User> users = Arrays.asList(
            new User(ADMIN_ID, "admin1", "password1", "admin1@mail.ru", Role.ADMIN),
            new User(USER_ID, "user2", "password2", "user2@mail.ru", Role.USER)
    );

    public static List<UserTo> toTos(List<User> users) {
        return users.stream()
                .map(u -> new UserTo(u.getId(), u.getName(), u.getRoles()))
                .collect(Collectors.toList());
    }
}
