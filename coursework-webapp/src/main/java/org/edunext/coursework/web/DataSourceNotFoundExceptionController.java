package org.edunext.coursework.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * @author xulixin
 */
@ControllerAdvice
public class DataSourceNotFoundExceptionController {

    @ExceptionHandler(value = {SQLException.class})
    public ModelAndView handlerDataSourceNotFoundException() {
        return new ModelAndView("redirect:/install");
    }
}
