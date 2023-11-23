package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.model.Gym;
import tracker.model.RegisteredUser;
import tracker.service.GymService;
import tracker.service.RegisteredUserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegisteredUserTLController {
    private final RegisteredUserService registeredUserService;
    private String msg = "";

    @GetMapping("/registration")
    public String registration(Map<String, Object> model){
        model.put("msg", msg);
        model.put("newRegisteredUser", new RegisteredUser());
        msg="";
        return "registration";
    }

    @PostMapping("/newRegisteredUser")
    public String register(RegisteredUser registeredUser) {
        msg = registeredUserService.addRegisteredUser(registeredUser);
        return "redirect:/registration";
    }

    @GetMapping("/")
    public String login(Map<String, Object> model){
        model.put("msg", msg);
        model.put("loginUser", new RegisteredUser());
        msg="";
        return "login";
    }

    @PostMapping("/loginUser")
    public String login(RegisteredUser registeredUser) {
        msg = registeredUserService.loginUser(registeredUser.getUserName(), registeredUser.getPassword());
        return "redirect:/";
    }
}
