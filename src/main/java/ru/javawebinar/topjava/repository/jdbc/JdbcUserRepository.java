package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.Util.*;
import static ru.javawebinar.topjava.util.Util.unique;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final UserWithRolesResultSetExtractor userWithRolesResultSetExtractor
            = new UserWithRolesResultSetExtractor();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertUserRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            updateUserRoles(user);
        }
        return user;
    }

    private void updateUserRoles(User user) {
        deleteUserRoles(user);
        insertUserRoles(user);
    }

    private void insertUserRoles(User user) {
        var batchArgs = user.getRoles().stream().map(role -> new Object[]{role.name(), user.id()}).toList();
        var sql = "INSERT INTO user_role (role, user_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void deleteUserRoles(User user) {
        var sql = "DELETE FROM user_role WHERE user_id=?";
        jdbcTemplate.update(sql, user.id());
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        var userMap = jdbcTemplate.query("""
                SELECT * FROM users u INNER JOIN user_role ur on u.id = ur.user_id WHERE id=?
                """, userWithRolesResultSetExtractor, id);
        List<User> users = unique(userMap.values());
        return singleResultOrNull(users);
    }

    @Override
    public User getByEmail(String email) {
        var userMap = jdbcTemplate.query("""
                        SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id WHERE email=?
                """, userWithRolesResultSetExtractor, email);
        List<User> users = unique(userMap.values());
        return singleResultOrNull(users);
    }

    @Override
    public List<User> getAll() {
        var userMap = jdbcTemplate.query("""
                SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id ORDER BY name, email
                """, userWithRolesResultSetExtractor);
        return unique(userMap.values());
    }

    private class UserWithRolesResultSetExtractor implements ResultSetExtractor<Map<Integer, User>> {
        @Override
        public Map<Integer, User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new HashMap<>();
            var mapper = new BeanPropertyRowMapper<User>(User.class);
            while (rs.next()) {
                var roleStr = rs.getString("role");
                var user = mapper.mapRow(rs, rs.getRow());

                if (userMap.containsKey(user.id())) {
                    var oldUser = userMap.get(user.id());
                    var roles = oldUser.getRoles();
                    roles.add(Role.valueOf(roleStr));
                } else {
                    user.setRoles((roleStr != null) ? List.of(Role.valueOf(roleStr)) : Collections.emptyList());
                    userMap.putIfAbsent(user.id(), user);
                }
            }
            return userMap;
        }
    }
}
