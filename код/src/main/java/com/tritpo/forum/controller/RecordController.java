package com.tritpo.forum.controller;

import com.tritpo.forum.HTMLElement.Button;
import com.tritpo.forum.enums.Role;
import com.tritpo.forum.model.Record;
import com.tritpo.forum.service.RecordService;
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
public class RecordController {

    @Autowired
    RecordService recordService;

    @GetMapping("/addRecord")
    public String getAddRecordPage(Model model){
        Record record=new Record();
        model.addAttribute("url", "/addRecord");
        model.addAttribute("record", record);
        model.addAttribute("themes", recordService.getThemes());
        return "add_record";
    }

    @PostMapping("/addRecord")
    public String addRecord(Model model, Record record, HttpServletRequest request){
        String reqUser=request.getUserPrincipal().getName();
        record.setWriter(reqUser);
        recordService.add(record);
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.contains(Role.ADMIN))
            return "redirect:/addNewTheme/"+record.getTheme();
        return "redirect:/account/"+reqUser+"/1";
    }

    @PostMapping("/updateRecord")
    public String updateRecord(Model model, Record record, HttpServletRequest request){
        String reqUser=request.getUserPrincipal().getName();
        recordService.update(record);
        return "redirect:/account/"+reqUser+"/1";
    }

    @PostMapping("/read")
    public String getReadRecordPage(Model model, Record record) throws Exception {
        model.addAttribute("record", record);
        return "record";
    }

    @PostMapping("/edit")
    public String getEditRecordPage(Model model, Record record) throws Exception {
        model.addAttribute("url", "/updateRecord");
        model.addAttribute("record", record);
        model.addAttribute("themes", recordService.getThemes());
        return "add_record";
    }

    @PostMapping("/delete")
    public String deleteRecord(Model model, Record record, HttpServletRequest request) throws Exception {
        recordService.deleteRow(record.getId());
        return "redirect:"+request.getHeader("Referer");
    }

    @GetMapping("/theme/{theme}/{page}")
    public String getThemePage(Model model, @PathVariable(value = "theme") String theme, @PathVariable(value = "page") Integer page,
                               @RequestParam(value = "title", defaultValue = "") String title,
                               @RequestParam(value = "writer", defaultValue = "") String writer) throws Exception {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        ArrayList<String> paramName=new ArrayList<>();
        ArrayList<String> param=new ArrayList<>();
        paramName.add("theme");
        param.add(theme);
        if(!title.equals("")){
            paramName.add("title");
            param.add(title);
        }
        if(!writer.equals("")){
            paramName.add("writer");
            param.add(writer);
        }

        List<Record> records=recordService.search(paramName, param);
        records=records.subList((page-1)*5, Math.min (page*5, records.size()));

        ArrayList<Button> buttons=new ArrayList<>();
        buttons.add(new Button("Read", "/read"));

        if(authorities.contains(Role.ADMIN))
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
