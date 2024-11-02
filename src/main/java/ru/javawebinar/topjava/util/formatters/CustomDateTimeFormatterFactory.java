package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class CustomDateTimeFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return Set.of(LocalDate.class, LocalTime.class);
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }

    private Formatter<?> getFormatter(CustomDateTimeFormat annotation) {
        return switch (annotation.type()) {
            case DATE -> new CustomDateFormatter();
            case TIME -> new CustomTimeFormatter();
        };
    }
}
