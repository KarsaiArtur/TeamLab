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

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WorkoutTLController {
    private final WorkoutService workoutService;
    private final GymService gymService;
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
    public String exercises(@RequestParam("workoutId") int id) {
        Workout workout = workoutService.findWorkout(id);
        TrackerApplication.getInstance().setCurrentWorkout(workout);
        return "redirect:/exercises";
    }

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

