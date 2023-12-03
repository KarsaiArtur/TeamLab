package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Equipment;
import tracker.model.Gym;
import tracker.model.Split;
import tracker.service.RegisteredUserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AddGymTLController {
    private final RegisteredUserService registeredUserService;
    private String userName = "";

    @GetMapping("/addGym")
    public String addGym(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("add", new GymCopy());
        return "addGym";
    }

    @PostMapping("/add")
    public String add(GymCopy gymCopy) {
        Gym gym = Gym.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(gymCopy.getName())
                .publiclyAvailable(gymCopy.publiclyAvailable.equals("Yes"))
                .location(gymCopy.getLocation())
                .howEquipped(Equipment.valueOf(gymCopy.getHowEquipped()))
                .build();
        gym.setSplit(Split.builder()
                .name(Split.SplitType.valueOf(gymCopy.getSplitType()))
                .numberOfDays(gymCopy.getNumberOfDays())
                .gym(gym)
                .build());
        registeredUserService.addNewGymToUser(gym);
        return "redirect:/gyms";
    }

    @Setter
    @Getter
    class GymCopy{
        private String name;
        private String publiclyAvailable;
        private String location;
        private int numberOfDays;
        private String splitType;
        private String howEquipped;
    }
}
