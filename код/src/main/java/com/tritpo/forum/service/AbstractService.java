package com.tritpo.forum.service;

import com.sun.istack.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractService<Data> {

    protected JdbcTemplate jdbcTemplate;

    AbstractService(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/forum?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    protected String formQuery(String searchQuery, ArrayList<String> paramName, ArrayList<String> param){
        String query=searchQuery;
        if(paramName.size()!=0 && param.size()!=0){
            for(int i=0; i<param.size(); i++){
                if(i!=0){
                    query=query+" AND ";
                }
                query=query+paramName.get(i)+"=\'"+param.get(i)+"\'";
            }
        }
        else{
            query=query+" id>0";
        }
        query+=" ORDER BY id DESC;";
        return query;
    }

    public abstract void add(Data data);
    public abstract void update(Data data);
    public abstract void deleteRow(Integer id);
    public abstract List<Data> search(ArrayList<String> paramName, ArrayList<String> param);
    public abstract List<Data> getList();

}



















