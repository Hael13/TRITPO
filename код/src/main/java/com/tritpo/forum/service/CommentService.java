package com.tritpo.forum.service;

import com.tritpo.forum.exceptions.NoSuchRecordException;
import com.tritpo.forum.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Service
public class CommentService extends AbstractService<Comment> {

    private final String addQuery1="INSERT INTO c";
    private final String addQuery2="(writer, text) VALUES (?,?)";
    private final String createQuery1="CREATE TABLE IF NOT EXISTS c";
    private final String createQuery2="(id INT UNSIGNED AUTO_INCREMENT, writer VARCHAR(50), text VARCHAR(100), constraint c";
    private final String createQuery3=" primary key (id));";
    private final String searchQuery="SELECT * FROM c";
    private final String dropQuery="DROP TABLE IF EXISTS c";
    private final String deleteQuery="DELETE FROM c";

    public CommentService(){
        super();
    }

    @Autowired
    public CommentService(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
    }

    @Override
    public void add(Comment comment) throws DataAccessException {
        jdbcTemplate.update(addQuery1+comment.getRecordId()+addQuery2, comment.getWriter(), comment.getText());
    }

    @Override
    public void deleteRow(Integer RecordId) throws DataAccessException {
        jdbcTemplate.execute(dropQuery+RecordId.toString());
    }

    public void deleteRow(Integer RecordId, Integer commentId) throws DataAccessException {
        jdbcTemplate.execute(deleteQuery+RecordId.toString()+" WHERE id=\'"+commentId+"\'");
    }

    @Override
    public void update(Comment comment) throws DataAccessException {
        jdbcTemplate.execute(createQuery1+comment.getRecordId()+createQuery2+comment.getId()+createQuery3);
    }

    public List<Comment> search(Integer RecordId) throws DataAccessException {
        return jdbcTemplate.query(searchQuery+RecordId.toString()+";", new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet rs, int i) throws SQLException {
                return new Comment(RecordId, rs.getInt("id"), rs.getString("writer"),
                        rs.getString("text"));
            }
        });
    }

}
