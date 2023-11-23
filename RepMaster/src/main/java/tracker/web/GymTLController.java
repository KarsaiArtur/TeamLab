package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.model.Gym;
import tracker.service.GymService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class GymTLController {

    private final GymService gymService;

    @GetMapping("/index")
    public String index(Map<String, Object> model){
        model.put("gyms", gymService.listGyms());
        model.put("msg", "valami");
        model.put("newGym", new Gym());
        return "index";
    }

    @PostMapping("/newGym")
    public String create(Gym gym) {
        gymService.saveGym(gym);
        return "redirect:/index";
    }
}
