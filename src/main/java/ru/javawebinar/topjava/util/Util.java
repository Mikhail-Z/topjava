package ru.javawebinar.topjava.util;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Util {

    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static Class<?> getEffectiveClass(Object o) {
        return o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    }

    public static <T> List<T> unique(Collection<T> collection) {
        return new HashSet<>(collection).stream().toList();
    }
}