package dashfwd.springbootdemo.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest

@Controller
class PeopleController {

    /**
     * Page rendering using Freemarker
     */
    @GetMapping("/")
    fun people(request:HttpServletRequest, model: Model, name:String?):String {

        // highlight "People" in the top nav
        model.addAttribute("selectedNav", "people")
        return "/people"
    }
}