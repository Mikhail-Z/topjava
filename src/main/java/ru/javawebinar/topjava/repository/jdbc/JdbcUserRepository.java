package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
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

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private static final UserWithRolesResultSetExtractor USER_WITH_ROLES_RESULT_SET_EXTRACTOR
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
                """, USER_WITH_ROLES_RESULT_SET_EXTRACTOR, id);
        var users = userMap.values();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        var userMap = jdbcTemplate.query("""
                        SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id WHERE email=?
                """, USER_WITH_ROLES_RESULT_SET_EXTRACTOR, email);
        var users = userMap.values();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        var userMap = jdbcTemplate.query("""
                SELECT u.*, ur.* FROM users u LEFT OUTER JOIN user_role ur ON u.id = ur.user_id ORDER BY name, email
                """, USER_WITH_ROLES_RESULT_SET_EXTRACTOR);
        return new ArrayList<>(userMap.values());
    }

    private static class UserWithRolesResultSetExtractor implements ResultSetExtractor<Map<Integer, User>> {
        private static final BeanPropertyRowMapper<User> MAPPER = new BeanPropertyRowMapper<>(User.class);

        @Override
        public Map<Integer, User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new LinkedHashMap<>();
            while (rs.next()) {
                var roleStr = rs.getString("role");
                var id = rs.getInt("id");

                if (userMap.containsKey(id)) {
                    var oldUser = userMap.get(id);
                    var roles = oldUser.getRoles();
                    roles.add(Role.valueOf(roleStr));
                } else {
                    var user = MAPPER.mapRow(rs, rs.getRow());
                    user.setRoles((roleStr != null) ? List.of(Role.valueOf(roleStr)) : Collections.emptyList());
                    userMap.put(id, user);
                }
            }
            return userMap;
        }
    }
}
