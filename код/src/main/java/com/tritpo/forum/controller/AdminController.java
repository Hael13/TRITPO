package com.tritpo.forum.controller;

import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.Comment;
import com.tritpo.forum.model.Record;
import com.tritpo.forum.model.User;
import com.tritpo.forum.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

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

    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam(value = "table", required = true) Integer table,
                                @RequestParam(value = "commentId", required = true) Integer commentId,
                                HttpServletRequest request) throws Exception {
        commentService.deleteRow(table, commentId);
        return "redirect:"+request.getHeader("Referer");
    }

}
