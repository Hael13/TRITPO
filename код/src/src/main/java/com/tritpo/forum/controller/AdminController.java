package com.tritpo.forum.controller;

import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.Record;
import com.tritpo.forum.model.User;
import com.tritpo.forum.service.RecordService;
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

}
