package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithIdAndUserId(T object, int id, int userId) {
        checkNotFoundWithIdAndUserId(object != null, id, userId);
        return object;
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithIdAndUserId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithIdAndUserId(boolean found, int id, int userId) {
        checkNotFound(found, String.format("id=%d, userId=%d", id, userId));
    }

    public static void checkNotFoundWithIdAndUserId(boolean found, int id) {
        checkNotFound(found, String.format("id=%d", id));
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }
}