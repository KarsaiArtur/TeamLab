package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tracker.TrackerApplication;
import tracker.model.Gym;
import tracker.service.GymService;
import tracker.service.RegisteredUserService;

import java.util.Map;

/**
 * Controller osztály a gyms.hmtl-hez, amelyen láthatóak az edzőtermek
 */
@Controller
@RequiredArgsConstructor
public class GymTLController {
    /**
     * az edzőtervek service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
     */
    private final GymService gymService;
    /**
     * a regisztrált felhasználó service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final RegisteredUserService registeredUserService;

    /**
     * létrehozza a htmlt
     * @param model model
     * @return html neve
     */
    @GetMapping("/gyms")
    public String gyms(Map<String, Object> model){
        String userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("gyms", gymService.listUserGyms());
        model.put("userName", userName +"'s gyms");
        model.put("workouts", 0);
        return "gyms";
    }

    /**
     * elmenti a kiválasztott edzőtermet, és átirányít az edzőtervek oldalra, ahol annak az edzőteremnek az edzőtervei lesznek láthatóak
     * @param id a kiválasztott edzőterem id-ja
     * @return átirányít az edzőtervek oldalra
     */
    @PostMapping("/workouts")
    public String workouts(@RequestParam("gymId") int id) {
        Gym gym = gymService.findGym(id);
        TrackerApplication.getInstance().setCurrentGym(gym);
        return "redirect:/workouts";
    }

    /**
     * törli a kiválasztott edzőtermet.
     * Ha az edzőterem publikus akkor csak a jelenlegi felhasználótól veszi ki, ha nem, akkor törli az adatbázisból
     * @param id törölni kívánt edzőterem id-ja
     * @return átirányít az edzőtermek oldalra, ahol most is vagyunk
     */
    @PostMapping("/deleteGym")
    public String deleteGym(@RequestParam("gymId") int id) {
        Gym gym = gymService.findGym(id);
        if(gym.isPubliclyAvailable()){
            registeredUserService.removeGymFromUser(id);
        }
        else
            gymService.deleteGym(id);
        return "redirect:/gyms";
    }
}
