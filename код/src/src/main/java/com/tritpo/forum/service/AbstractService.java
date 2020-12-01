package com.tritpo.forum.service;

import com.mysql.cj.jdbc.Driver;
import com.sun.istack.NotNull;
import com.tritpo.forum.exceptions.NoSuchRecordException;
import com.zaxxer.hikari.util.DriverDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mapping.model.Property;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public abstract class AbstractService<Data> {

    protected JdbcTemplate jdbcTemplate;

    public AbstractService(){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/forum?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        jdbcTemplate=new JdbcTemplate(dataSource);

    }

    public abstract void add(Data data) throws DataAccessException;
    public abstract void update(Data data) throws DataAccessException, NoSuchRecordException;
    public abstract void deleteRow(Integer id) throws DataAccessException, NoSuchRecordException;

}



















