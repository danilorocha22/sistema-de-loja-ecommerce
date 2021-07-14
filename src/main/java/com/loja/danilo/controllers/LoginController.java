package com.loja.danilo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView form() {
        return new ModelAndView("/auth/login");
    }

}
