package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Gym;
import tracker.service.GymService;
import tracker.service.RegisteredUserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GymTLController {
    private final GymService gymService;
    private final RegisteredUserService registeredUserService;
    private String userName = "";

    @GetMapping("/gyms")
    public String gyms(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("gyms", gymService.listUserGyms());
        model.put("userName", userName+"'s gyms");
        model.put("add", new Gym());
        return "gyms";
    }

    @PostMapping("/workouts")
    public String workouts(Gym gym) {
        TrackerApplication.getInstance().setCurrentGym(gym);
        return "redirect:/workouts";
    }

    /*@PostMapping("/addNewGym")
    public String addNewGym() {
        return "redirect:/addGym";
    }*/
}
