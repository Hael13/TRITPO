package com.tritpo.forum.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ErrorController{

    @ExceptionHandler(value = Exception.class)
    public String UserNotFound(Model model, HttpServletRequest req, Exception ex) {
        model.addAttribute("errorText", ex.getMessage());
        return "error";
    }

}
