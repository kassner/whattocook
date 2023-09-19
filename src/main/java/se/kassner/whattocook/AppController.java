package se.kassner.whattocook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController
{
    @GetMapping("/")
    public String home()
    {
        return "home";
    }
}
