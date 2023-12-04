package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tracker.TrackerApplication;
import tracker.model.Gym;
import tracker.model.Workout;
import tracker.service.GymService;
import tracker.service.WorkoutService;

import java.util.Map;

/**
 * Controller osztály a workouts.hmtl-hez, amelyen láthatóak az edzőtervek
 */
@Controller
@RequiredArgsConstructor
public class WorkoutTLController {
    /**
     * az edzőtermek service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
     */
    private final WorkoutService workoutService;
    /**
     * az edzőtervek service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
     */
    private final GymService gymService;

    /**
     * létrehozza a htmlt
     * @param model model
     * @return html neve
     */
    @GetMapping("/workouts")
    public String workouts(Map<String, Object> model){
        String userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        Gym gym = TrackerApplication.getInstance().getCurrentGym();
        model.put("workouts", workoutService.listWorkoutsByGymId(gym.getId()));
        model.put("userName", userName +"'s workouts in "+TrackerApplication.getInstance().getCurrentGym().getName()+" gym");
        return "workouts";
    }

    /**
     * elmenti a kiválasztott edzőtervet, és átirányít a gyakorlatok oldalra, ahol annak az edzőtervnek a gyakorlatai lesznek láthatóak
     * @param id a kiválasztott edzőterv id-ja
     * @return átirányít a gyakorlatok oldalra
     */
    @PostMapping("/exercises")
    public String exercises(@RequestParam("workoutId") int id) {
        Workout workout = workoutService.findWorkout(id);
        TrackerApplication.getInstance().setCurrentWorkout(workout);
        return "redirect:/exercises";
    }

    /**
     * törli a kiválasztott edzőtervet.
     * Ha az edzőterv publikus akkor csak a jelenlegi edzőteremből veszi ki, ha nem, akkor törli az adatbázisból
     * @param id törölni kívánt edzőterv id-ja
     * @return átirányít az edzőtervek oldalra, ahol most is vagyunk
     */
    @PostMapping("/deleteWorkout")
    public String deleteWorkout(@RequestParam("workoutId") int id) {
        Workout workout = workoutService.findWorkout(id);
        if(workout.isPubliclyAvailable()){
            gymService.removeWorkoutFromGym(TrackerApplication.getInstance().getCurrentGym().getId(), id);
        }
        else
            workoutService.deleteWorkout(id);
        return "redirect:/workouts";
    }
}

