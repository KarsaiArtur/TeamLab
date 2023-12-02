package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Workout;
import tracker.service.GymService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AddWorkoutTLController {
    private final GymService gymService;
    private String userName = "";

    @GetMapping("/addWorkout")
    public String addGym(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("addW", new WorkoutCopy());
        return "addWorkout";
    }

    @PostMapping("/addW")
    public String addW(WorkoutCopy workoutCopy) {
        Workout workout = Workout.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(workoutCopy.getName())
                .publiclyAvailable(workoutCopy.publiclyAvailable.equals("Yes"))
                .build();
        gymService.addNewWorkoutToGym(TrackerApplication.getInstance().getCurrentGym().getId(), workout);
        return "redirect:/workouts";
    }

    @Setter
    @Getter
    class WorkoutCopy{
        private String name;
        private String publiclyAvailable;
    }
}
