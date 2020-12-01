package com.tritpo.forum;

import com.tritpo.forum.model.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = " ")
public class RecordTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    RowMapper<Record> rowMap=new RowMapper<Record>() {
        @Override
        public Record mapRow(ResultSet rs, int i) throws SQLException {
            return new Record(rs.getInt("id"), rs.getString("theme"),
                    rs.getString("writer"), rs.getString("title"),
                    rs.getString("text"));
        }
    };

    @Test
    @Sql(value = "/test-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addRecordTest() throws Exception {
        String title = "title";
        String text="text";
        String theme="Sample";

        this.mockMvc.perform(post("/addRecord").param("id", "0").param("writer", " ")
                .param("title", title).param("theme", theme).param("text", text))
                .andDo(print());

        List<Record> records=jdbcTemplate.query("SELECT * FROM records WHERE writer=' ' AND title='title' AND text='text'", rowMap);

        assertThat(records.size()).isEqualTo(1);
    }

    @Test
    void updateRecordTest() throws Exception {
        List<Record> records;

        records=jdbcTemplate.query("SELECT * FROM records WHERE writer=' ' AND title='title' AND text='text'", rowMap);

        assertThat(records.size()).isEqualTo(1);
        Record record=records.get(0);
        this.mockMvc.perform(post("/updateRecord").param("id", record.getId().toString()).param("writer", record.getWriter())
                .param("title", "Second title version").param("theme", record.getTheme()).param("text", "Second text version"))
                .andDo(print());

        records=jdbcTemplate.query("SELECT * FROM records WHERE id='"+record.getId().toString()+"'", rowMap);

        assertThat(records.size()).isEqualTo(1);
        assertThat(records.get(0).getTitle()).isEqualTo("Second title version");
        assertThat(records.get(0).getText()).isEqualTo("Second text version");
    }

    @Test
    @Sql(value = "/test-row-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/test-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteRecordTest() throws Exception {

        List<Record> records=jdbcTemplate.query("SELECT * FROM records WHERE writer=' '", rowMap);

        assertThat(records.size()).isEqualTo(1);

        this.mockMvc.perform(get("/delete").param("id", records.get(0).getId().toString())).andDo(print());

        records=jdbcTemplate.query("SELECT * FROM records WHERE id='"+records.get(0).getId().toString()+"'", rowMap);

        assertThat(records.size()).isEqualTo(0);

    }

}
