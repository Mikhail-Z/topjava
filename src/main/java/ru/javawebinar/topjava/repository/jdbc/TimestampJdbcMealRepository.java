package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(HSQL_DB)
public class TimestampJdbcMealRepository extends JdbcMealRepository<Timestamp> {

    public TimestampJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Timestamp getValidTypeDateTimeValue(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime.toString());
    }
}
