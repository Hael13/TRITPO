package com.tritpo.forum.controller;

import com.tritpo.forum.HTMLElement.Button;
import com.tritpo.forum.enums.Role;
import com.tritpo.forum.exceptions.NoSuchRecordException;
import com.tritpo.forum.exceptions.NoSuchThemeException;
import com.tritpo.forum.model.Comment;
import com.tritpo.forum.model.Record;
import com.tritpo.forum.service.CommentService;
import com.tritpo.forum.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class RecordController {

    @Autowired
    RecordService recordService;

    @Autowired
    CommentService commentService;

    boolean isADMIN(){
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.contains(Role.ADMIN);
    }

    @PostMapping("/addComment")
    public String addComment(Comment comment, HttpServletRequest request){
        comment.setWriter(request.getUserPrincipal().getName());
        commentService.add(comment);
        return "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/addRecord")
    public String getAddRecordPage(Model model){
        Record record=new Record();
        model.addAttribute("url", "/addRecord");
        model.addAttribute("record", record);
        model.addAttribute("themes", recordService.getThemes());
        return "add_record";
    }

    @PostMapping("/addRecord")
    public String addRecord(Model model, Record record, HttpServletRequest request) throws NoSuchThemeException {

        if(!recordService.getThemes().contains(record.getTheme())){
            if(isADMIN())
                recordService.addTheme(record.getTheme());
            else
                throw new NoSuchThemeException(record.getTheme());
        }

        String reqUser=request.getUserPrincipal().getName();
        record.setWriter(reqUser);
        recordService.add(record);
        return "redirect:/account/"+reqUser+"/1";
    }

    @PostMapping("/updateRecord")
    public String updateRecord(Model model, Record record, HttpServletRequest request) throws NoSuchRecordException{
        String reqUser=request.getUserPrincipal().getName();
        recordService.update(record);
        return "redirect:/account/"+reqUser+"/1";
    }

    @GetMapping("/read")
    public String getReadRecordPage(Model model, @RequestParam(value = "id", required = true) Integer id) throws NoSuchRecordException {
        Comment comment=new Comment(id, 0, "", "");
        boolean x=isADMIN();
        synchronized (this) {
            Record record = recordService.get(id);
            commentService.update(comment);
            model.addAttribute("x", x);
            model.addAttribute("record", record);
            model.addAttribute("comm", comment);
            model.addAttribute("comments", commentService.search(record.getId()));
        }
        return "record";
    }

    @GetMapping("/edit")
    public String getEditRecordPage(Model model, @RequestParam(value = "id", required = true) Integer id) throws NoSuchRecordException {
        synchronized (this){
            model.addAttribute("url", "/updateRecord");
            model.addAttribute("record", recordService.get(id));
            model.addAttribute("themes", recordService.getThemes());
        }
        return "add_record";
    }

    @GetMapping("/delete")
    public String deleteRecord(Model model, @RequestParam(value = "id", required = true) Integer id, HttpServletRequest request) throws Exception {
        synchronized (this){
            commentService.deleteRow(id);
            recordService.deleteRow(id);
        }
        return "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/theme/{theme}/{page}")
    public String getThemePage(Model model, @PathVariable(value = "theme") String theme, @PathVariable(value = "page") Integer page,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "writer", required = false) String writer) throws Exception {

        List<Record> records=recordService.search(theme, title, writer);
        records=records.subList((page-1)*5, Math.min (page*5, records.size()));

        ArrayList<Button> buttons=new ArrayList<>();
        buttons.add(new Button("Read", "/read"));

        if(isADMIN())
            buttons.add(new Button("Delete", "/delete"));

        model.addAttribute("records", records);
        model.addAttribute("buttons", buttons);
        model.addAttribute("themes", recordService.getThemes());
        if(page!=1)
            model.addAttribute("prev", "/theme/"+theme+"/"+String.valueOf(page-1));
        if(records.size()==5)
            model.addAttribute("next", "/theme/"+theme+"/"+String.valueOf(page+1));
        return "theme";
    }

}
