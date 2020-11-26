package com.tritpo.forum.controller;

import com.tritpo.forum.HTMLElement.Button;
import com.tritpo.forum.enums.Role;
import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.Record;
import com.tritpo.forum.service.RecordService;
import com.tritpo.forum.service.UserService;
import com.tritpo.forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String getWelcomePage(){
        return "welcome";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/reg")
    public String regPage(){
        return  "reg";
    }

    @PostMapping("/reg")
    public String regUser(User user){
        ArrayList<String> paramName=new ArrayList<>();
        ArrayList<String> param=new ArrayList<>();
        paramName.add("name");
        param.add(user.getName());
        if(userService.search(paramName, param).size()==0)
            userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/myaccount")
    public String myaccount(HttpServletRequest request) throws Exception {
        ArrayList<String> paramName=new ArrayList<>();
        ArrayList<String> param=new ArrayList<>();
        paramName.add("name");
        param.add(request.getUserPrincipal().getName());
        User user=userService.search(paramName, param).get(0);

        if(user.getStatus().equals(Status.BANNED)){
            request.logout();
            throw new Exception("You're banned");
        }
        return "redirect:/account/"+request.getUserPrincipal().getName()+"/1";
    }

    @GetMapping("/account/{username}/{page}")
    public String getAccountPage(Model model, @PathVariable(value="username") String username, @PathVariable(value="page") Integer page,
                                 @RequestParam(value = "title", defaultValue = "") String title, HttpServletRequest request){

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        String reqUser=request.getUserPrincipal().getName();

        ArrayList<String> paramName=new ArrayList<>();
        ArrayList<String> param=new ArrayList<>();
        paramName.add("name");
        param.add(username);

        ArrayList<Button> buttons=new ArrayList<>();
        buttons.add(new Button("Read", "/read"));
        if(username.equals(reqUser)){
            buttons.add(new Button("Edit", "/edit"));
            buttons.add(new Button("Delete", "/delete"));
        }
        else if(authorities.contains(Role.ADMIN)) {
            buttons.add(new Button("Delete", "/delete"));
            if(userService.search(paramName, param).get(0).getStatus().equals(Status.ACTIVE))
                model.addAttribute("banBut", new Button("ban", "/ban/"+username));
            else
                model.addAttribute("banBut", new Button("unban", "/ban/"+username));
        }

        RecordService tempService=new RecordService();
        paramName.clear();
        param.clear();

        paramName.add("writer");
        param.add(username);

        if(!title.equals("")) {
            paramName.add("title");
            param.add(title);
        }

        List<Record> records=tempService.search(paramName, param);
        records=records.subList((page-1)*5, Math.min (page*5, records.size()));

        model.addAttribute("username", username);
        model.addAttribute("buttons", buttons);
        model.addAttribute("themes", tempService.getThemes());
        model.addAttribute("records", records);
        if(page!=1)
            model.addAttribute("prev", "/account/"+username+"/"+String.valueOf(page-1));
        if(records.size()==5)
            model.addAttribute("next", "/account/"+username+"/"+String.valueOf(page+1));
        return  "account";
    }

}