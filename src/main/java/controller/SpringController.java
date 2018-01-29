package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FaceFeel
 * @Created 2018-01-26 13:39
 */

@Controller
@RequestMapping(value = "/springDemo")
public class SpringController {

    @RequestMapping(value = "/show")
    public String show(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("spring","springmvc");
        return "show";
    }
}
