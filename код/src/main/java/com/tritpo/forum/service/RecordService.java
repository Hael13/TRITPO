package com.tritpo.forum.service;

import com.tritpo.forum.exceptions.NoSuchRecordException;
import com.tritpo.forum.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Service
public class RecordService extends AbstractService<Record>{

    private final String addQuery="INSERT INTO records(theme, writer, title, text) VALUES (?, ?,?,?)";
    private final String deleteQuery="DELETE FROM records WHERE ";
    private final String searchQuery="SELECT * FROM records WHERE ";
    private final String updateQuery="UPDATE records SET theme=?, writer=?, title=?, text=? WHERE id=?";
    List<String> themes;
    Integer rowCount;

    public RecordService() throws DataAccessException{
        super();
        rowCount=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM records;", Integer.class);
        themes=jdbcTemplate.query("SELECT * FROM themes;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("theme");
            }
        });
    }

    @Autowired
    public RecordService(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
        rowCount=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM records;", Integer.class);
        themes=jdbcTemplate.query("SELECT * FROM themes;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("theme");
            }
        });
    }

    @Override
    public void add(Record record) throws DataAccessException{
        jdbcTemplate.update(addQuery, record.getTheme(), record.getWriter(), record.getTitle(), record.getText());
    }

    @Override
    public void update(Record record) throws DataAccessException, NoSuchRecordException{
        this.get(record.getId());//Проверка на наличие записи в БД
        jdbcTemplate.update(updateQuery, record.getTheme(), record.getWriter(), record.getTitle(), record.getText(), record.getId());
    }

    @Override
    public void deleteRow(Integer id) throws DataAccessException, NoSuchRecordException{
        this.get(id);//Проверка на наличие записи в БД
        jdbcTemplate.update(deleteQuery+"id=\'"+id.toString()+"\'");
        rowCount--;
    }

    private String formQuery(String theme, String title, String writer){

        String query=searchQuery;
        boolean i=false;
        if(theme!=null){
            query=query+"theme=\'"+theme+"\'";
            i=true;
        }
        if(title!=null){
            if(i)
                query=query+" AND ";
            query=query+"title=\'"+title+"\'";
            i=true;
        }
        if(writer!=null){
            if(i)
                query=query+" AND ";
            query=query+"writer=\'"+writer+"\'";
            i=true;
        }
        return query+" ORDER BY id DESC;";
    }

    public List<Record> search(String theme, String title, String writer) throws DataAccessException {

        List<Record> records = jdbcTemplate.query(formQuery(theme, title, writer), new RowMapper<Record>() {
            @Override
            public Record mapRow(ResultSet rs, int i) throws SQLException {
                return new Record(rs.getInt("id"), rs.getString("theme"),
                        rs.getString("writer"), rs.getString("title"), rs.getString("text"));
            }
        });
        return records;
    }

    public Record get(Integer id) throws NoSuchRecordException{

        List<Record> list=jdbcTemplate.query(searchQuery+"id=\'"+id.toString()+"\'", new RowMapper<Record>() {
            @Override
            public Record mapRow(ResultSet rs, int i) throws SQLException {
                return new Record(rs.getInt("id"), rs.getString("theme"),
                        rs.getString("writer"), rs.getString("title"), rs.getString("text"));
            }
        });
        if(list.size()!=0)
            return list.get(0);
        else
            throw new NoSuchRecordException(id);
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void addTheme(String theme) throws DataAccessException{
        jdbcTemplate.update("INSERT INTO themes(theme) VALUE(?)", theme);
    }
}
