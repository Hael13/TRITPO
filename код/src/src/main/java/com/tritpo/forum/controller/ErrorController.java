package com.tritpo.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ErrorController{
    @ExceptionHandler(Throwable.class)
    public String UncheckedError(Model model, HttpServletRequest req, Throwable ex) {
        model.addAttribute("errorText", ex.getMessage());
        return "error";
    }

}