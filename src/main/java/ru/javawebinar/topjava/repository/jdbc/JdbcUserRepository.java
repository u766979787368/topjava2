package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

//    @Autowired
//    private DataSourceTransactionManager txManager;

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

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
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            if (Set.copyOf(jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", (rs, rowNum) -> Role.valueOf(rs.getString("role")), user.getId()))
                    .containsAll(user.getRoles())) {
                return null;
            }
        }
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        for (Role role : user.getRoles()) {
            jdbcTemplate.update("INSERT INTO user_roles (role, user_id) VALUES (?, ?)", role.name(), user.getId());
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
//        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
//        User user = DataAccessUtils.singleResult(users);
//        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", (rs, rowNum) -> Role.valueOf(rs.getString("role")), user.getId());
//        user.setRoles(Set.copyOf(roles));
//        return user;
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day,
                string_agg(r.role, ', ')  AS roles FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE u.id = ?
                GROUP BY u.id
                """, ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
//        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day,
                string_agg(r.role, ', ')  AS roles FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE u.email=?
                GROUP BY u.id
                """, ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
//        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN ORDER BY name, email", ROW_MAPPER);
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day,
                string_agg(r.role, ', ')  AS roles FROM users u LEFT JOIN user_roles r ON u.id = r.user_id 
                GROUP BY u.id  ORDER BY u.name, u.email
                """, ROW_MAPPER);


        return users;
    }
}
