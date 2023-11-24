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
public class SearchController {
    private final GymService gymService;
    private final RegisteredUserService registeredUserService;

    @GetMapping("/search")
    public String search(Map<String, Object> model){
        model.put("elements", gymService.listUserGyms());
        return "search";
    }

    @PostMapping("/search")
    public String add(Gym gym) {
        gym = Gym.builder().name("TEST").build();
        registeredUserService.addNewGymToUser(gym);
        return "redirect:/gyms";
    }

}
