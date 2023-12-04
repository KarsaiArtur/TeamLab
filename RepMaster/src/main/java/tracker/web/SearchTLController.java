package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tracker.TrackerApplication;
import tracker.model.Rateable;
import tracker.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchTLController {
    private final GymService gymService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final RegisteredUserService registeredUserService;
    private RateableService rateableService;
    private boolean searched = false;
    private List<Rateable> rateableList;

    @GetMapping("/search")
    public String search(Map<String, Object> model){
        if(!searched) rateableList = new ArrayList<>();
        model.put("elements", rateableList);
        model.put("search", new Search());
        model.put("rateableopen", 0);
        model.put("loggedIn", TrackerApplication.getInstance().isLoggedIn());
        searched=false;
        return "search";
    }

    /**
     * a megadott keresési beállítások alapján a találatok listájának megadása.
     * a rateableservice-eknek érteket adunk az alapján, hogy milyen típusú keresés történt
     * @param search a felhasználó által megadott keresési beállítások
     * @return visszairányít a keresés oldalra, ahol betölti a találatok listáját
     */
    @PostMapping("/search")
    public String r_desc(Search search) {
        searched=true;
        if("GymSplit".equals(search.searchType)){
            rateableList = registeredUserService.SearchGymBySplit(search.searchBar, search.order);
            rateableService = gymService;
            RateableDetailTLController.setRateableService(gymService);
        }
        else if("WorkoutName".equals(search.searchType)){
            rateableList = registeredUserService.SearchWorkoutByName(search.searchBar, search.order);
            rateableService = workoutService;
            RateableDetailTLController.setRateableService(workoutService);
        }
        else if("ExerciseMuscle".equals(search.searchType)){
            rateableList = registeredUserService.SearchExerciseByMuscleGroup(search.searchBar, search.order);
            rateableService = exerciseService;
            RateableDetailTLController.setRateableService(exerciseService);
        }
        else{
            rateableList = new ArrayList<>();
        }
        return "redirect:/search";
    }

    /**
     * a keresésnek megfelelt értékelhető objektumok listájából egy kiválasztott találat részleteinek betöltése
     * @param id a kiválaszott találat id-ja
     * @return a találat részleteinek betöltése
     */
    @PostMapping("/rateableopen")
    public String rateableOpen(@RequestParam("elementId") int id) {
        Rateable rateable = rateableService.findById(id);
        TrackerApplication.getInstance().setCurrentRateable(rateable);
        return "redirect:/detail";
    }

    /**
     * belső osztály, a html-en ezen keresztül történik a keresési adatok elmentése
     */
    @Getter
    @Setter
    class Search{
        /**
         * a keresett szó
         */
        private String searchBar;
        /**
         * a kiválasztott rendezési mód: RatingDesc, RatingAsc, NameDesc, NameAsc
         */
        private String order;
        /**
         * a kiválasztott keresési típus: GymSplit, WorkoutName, ExerciseMuscle
         */
        private String searchType;

    }
}
