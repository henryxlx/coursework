package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("courseworkSearchController")
public class SearchController {

    @RequestMapping("/search")
    public String indexPage() {
        return "/search/index";
    }
}
