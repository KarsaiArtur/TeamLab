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

/**
 * Controller osztály az addGym.hmtl-hez, amelyen egy edzőterem hozzáadása történik
 */
@Controller
@RequiredArgsConstructor
public class AddGymTLController {
    /**
     * regisztrált felhasználó service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final RegisteredUserService registeredUserService;

    /**
     * létrehozza a htmlt
     * @param model model
     * @return html neve
     */
    @GetMapping("/addGym")
    public String addGym(Map<String, Object> model){
        String userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("add", new GymCopy());
        model.put("userName", userName +"'s gyms");
        return "addGym";
    }

    /**
     * egy edzőterem hozzáadása az adatbázisba a beolvasott értékek alapján
     * @param gymCopy a böngészőben a felhasználó által megadott adatok
     * @return az edzőterem hozzáadása után visszamegyünk az edzőtermek oldalra
     */
    @PostMapping("/add")
    public String add(GymCopy gymCopy) {
        Gym gym = Gym.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(gymCopy.getName())
                .publiclyAvailable(gymCopy.getPubliclyAvailable().equals("Yes"))
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

    /**
     * belső osztály, a html-en ezen keresztül történik az adatok beolvasása
     */
    @Setter
    @Getter
    class GymCopy{
        /**
         * edzőterem neve
         */
        private String name;
        /**
         * publikus-e
         */
        private String publiclyAvailable;
        /**
         * edzőterem helye
         */
        private String location;
        /**
         * hány napra van bontva a split
         */
        private int numberOfDays;
        /**
         * split típusa
         */
        private String splitType;
        /**
         * mennyire felszerelt az edzőterem
         */
        private String howEquipped;
    }
}
