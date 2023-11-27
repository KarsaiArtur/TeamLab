package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Gym;
import tracker.model.Workout;
import tracker.service.GymService;
import tracker.service.WorkoutService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WorkoutTLController {
    private final WorkoutService workoutService;
    private String userName = "";

    @GetMapping("/workouts")
    public String workouts(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        Gym gym = TrackerApplication.getInstance().getCurrentGym();
        model.put("workouts", workoutService.listWorkoutsByGymId(gym.getId()));
        model.put("userName", userName+"'s workouts");
        return "workouts";
    }

    @PostMapping("/exercises")
    public String exercise(Workout workout) {
        TrackerApplication.getInstance().setCurrentWorkout(workout);
        return "redirect:/exercises";
    }

    /*@PostMapping("/newGym")
    public String create(Gym gym) {
        gymService.saveGym(gym);
        return "redirect:/gyms";
    }*/
}

