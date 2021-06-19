package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;

import java.util.EnumSet;
import java.util.Set;

public class UserTo {

    private final Integer id;
    private final String name;
    private final Set<Role> roles;

    public UserTo(Integer id, String name, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
