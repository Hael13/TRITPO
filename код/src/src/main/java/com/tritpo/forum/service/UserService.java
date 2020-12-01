package com.tritpo.forum.service;

import com.tritpo.forum.enums.Role;
import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserService extends AbstractService<User>{
    private final String addQuery="INSERT INTO users(name, password, role, status) VALUES (?,?,?,?)";
    private final String deleteQuery="DELETE FROM users WHERE";
    private final String searchQuery="SELECT * FROM users WHERE name=?;";
    private final String updateQuery="UPDATE users SET status=? WHERE name=?";
    BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        super();
        passwordEncoder=new BCryptPasswordEncoder(4);
    }

    @Override
    public void add(User user) throws DataAccessException{
        jdbcTemplate.update(addQuery, user.getName(), passwordEncoder.encode(user.getPassword()), Role.USER.name(), Status.ACTIVE.name());
    }

    @Override
    public void update(User user) throws DataAccessException{
        jdbcTemplate.update(updateQuery, user.getStatus().name(), user.getName());
    }

    @Override
    public void deleteRow(Integer id) throws DataAccessException{
        jdbcTemplate.update(deleteQuery+"id=\'"+id.toString()+"\'");
    }

    public List<User> search(String name) throws DataAccessException {
        return jdbcTemplate.query(searchQuery, new Object[]{name}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getString("role"), rs.getString("status"));
            }
        });
    }

}
