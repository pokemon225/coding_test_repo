package com.colorvotes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final ColorRepository colors;

    public PageController(ColorRepository colors) {
        this.colors = colors;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("colors", colors.findAll());
        return "index";
    }
}
