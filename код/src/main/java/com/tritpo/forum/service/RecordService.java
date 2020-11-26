package com.tritpo.forum.service;

import com.tritpo.forum.model.Record;
import com.tritpo.forum.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecordService extends AbstractService<Record>{

    private final String addQuery="INSERT INTO records(theme, writer, title, text) VALUES (?, ?,?,?)";
    private final String deleteQuery="DELETE FROM records WHERE ";
    private final String searchQuery="SELECT * FROM records WHERE ";
    private final String updateQuery="UPDATE records SET theme=?, writer=?, title=?, text=? WHERE id=?";
    List<String> themes;
    Integer rowCount;

    public RecordService(){
        super();
        rowCount=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM records;", Integer.class);
        themes=jdbcTemplate.query("SELECT * FROM themes;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("theme");
            }
        });
    }

    @Override
    public void add(Record record){
        jdbcTemplate.update(addQuery, record.getTheme(), record.getWriter(), record.getTitle(), record.getText());
    }

    @Override
    public void update(Record record){
        jdbcTemplate.update(updateQuery, record.getTheme(), record.getWriter(), record.getTitle(), record.getText(), record.getId());
    }

    @Override
    public void deleteRow(Integer id){
        jdbcTemplate.update(deleteQuery+"id=\'"+id.toString()+"\'");
        rowCount--;
    }

    @Override
    public List<Record> search(ArrayList<String> paramName, ArrayList<String> param){
        String query=this.formQuery(searchQuery, paramName, param);
        return jdbcTemplate.query(query, new RowMapper<Record>() {
            @Override
            public Record mapRow(ResultSet rs, int i) throws SQLException {
                return new Record(rs.getInt("id"), rs.getString("theme"),
                        rs.getString("writer"), rs.getString("title"), rs.getString("text"));
            }
        });
    }

    @Override
    public List<Record> getList() {
        return jdbcTemplate.query(searchQuery+"id>0;", new RowMapper<Record>() {
            @Override
            public Record mapRow(ResultSet rs, int i) throws SQLException {
                return new Record(rs.getInt("id"), rs.getString("theme"),
                        rs.getString("writer"), rs.getString("title"), rs.getString("text"));
            }
        });
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public List<String> getThemes() {
        return themes;
    }
}
