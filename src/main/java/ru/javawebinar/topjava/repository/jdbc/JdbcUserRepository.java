package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

import static ru.javawebinar.topjava.util.Util.unique;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

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

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        var userWithRolesRowMapper = new UserWithRolesRowMapper();
        List<User> users = unique(jdbcTemplate.query("""
SELECT * FROM users u INNER JOIN user_role ur on u.id = ur.user_id WHERE id=?
""", userWithRolesRowMapper, id));
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        var userWithRolesResultSetExtractor = new UserWithRolesRowMapper();
        List<User> users = unique(jdbcTemplate.query("""
        SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id WHERE email=?
""", userWithRolesResultSetExtractor, email));
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        var userWithRolesResultSetExtractor = new UserWithRolesRowMapper();
        return unique(jdbcTemplate.query("""
SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id ORDER BY name, email
""", userWithRolesResultSetExtractor));
    }

    private void updateUserRoles(User user) {
        deleteUserRoles(user);
        insertUserRoles(user);
    }

    private void insertUserRoles(User user) {
        var batchArgs = user.getRoles().stream().map(role -> new Object[] {role.name(), user.id()}).toList();
        var sql = "INSERT INTO user_role (role, user_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void deleteUserRoles(User user) {
        var sql = "DELETE FROM user_role WHERE user_id=?";
        jdbcTemplate.update(sql, user.id());
    }

    private class UserWithRolesRowMapper implements RowMapper<User> {

        private Map<Integer, User> userMap = new HashMap<>();

        @Override
        public User mapRow(ResultSet rs, int num) throws SQLException, DataAccessException {
            var id = rs.getInt("id");
            var name = rs.getString("name");
            var email = rs.getString("email");
            var password = rs.getString("password");
            var caloriesPerDay = rs.getInt("calories_per_day");
            var enabled = rs.getBoolean("enabled");
            var registered = rs.getDate("registered");
            var roleStr = rs.getString("role");
            if (userMap.containsKey(id)) {
                var user = userMap.get(id);
                var roles = user.getRoles();
                roles.add(Role.valueOf(roleStr));
                return user;
            }
            var user = new User(
                    id,
                    name,
                    email,
                    password,
                    caloriesPerDay,
                    enabled,
                    registered,
                    (roleStr != null) ? List.of(Role.valueOf(roleStr)) : Collections.emptyList()) ;
            userMap.put(id, user);
            return user;
        }
    };
}
