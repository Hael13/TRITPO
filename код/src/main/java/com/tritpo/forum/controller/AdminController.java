package com.tritpo.forum.controller;

import com.tritpo.forum.enums.Role;
import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.User;
import com.tritpo.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/ban/{username}")
    public String BanUser(@PathVariable(value="username") String username, HttpServletRequest request){

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        User user=new User();
        user.setName(username);
        if(user.getStatus().equals(Status.ACTIVE))
            user.setStatus(Status.BANNED);
        else
            user.setStatus(Status.ACTIVE);
        userService.update(user);

        return  "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/addNewTheme/{theme}")
    public String AddNewTheme(@PathVariable(value = "theme") String theme, HttpServletRequest request){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/forum?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
        if(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM themes WHERE theme=\'"+theme+"\'", Integer.class)==0)
            jdbcTemplate.update("INSERT INTO themes(theme) VALUE(?)", theme);

        return "redirect:/account/"+request.getUserPrincipal().getName()+"/1";
    }

}
