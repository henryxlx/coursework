package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xulixin
 */
@Controller
public class MyTestPaperController {

    @RequestMapping({"/my/quiz", "/my/favorite/question/show"})
    public String indexAction(HttpServletRequest request) {
        return "/my/quiz/my-quiz";
    }

    @RequestMapping("/my/teacher/{target}/test/list")
    public String listReviewingTestAction(HttpServletRequest request) {
        return "/my/quiz/teacher-test-layout";
    }
}
