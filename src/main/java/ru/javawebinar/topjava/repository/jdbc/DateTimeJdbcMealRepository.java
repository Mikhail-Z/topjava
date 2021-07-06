package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(POSTGRES_DB)
public class DateTimeJdbcMealRepository extends JdbcMealRepository<LocalDateTime> {

    public DateTimeJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime getValidTypeDateTimeValue(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
