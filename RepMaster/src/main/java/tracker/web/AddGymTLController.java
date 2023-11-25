package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Gym;
import tracker.model.Split;
import tracker.service.GymService;
import tracker.service.RegisteredUserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AddGymTLController {
    private final GymService gymService;
    private final RegisteredUserService registeredUserService;
    private String userName = "";

    @GetMapping("/addGym")
    public String addGym(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("add", new Gym());
        return "addGym";
    }

    @PostMapping("/add")
    public String add(Gym gym) {
        gym = Gym.builder().name("TEST").build();
        registeredUserService.addNewGymToUser(gym);
        return "redirect:/gyms";
    }
}
