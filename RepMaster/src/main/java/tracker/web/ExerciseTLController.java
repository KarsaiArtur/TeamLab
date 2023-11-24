package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.model.Workout;
import tracker.service.ExerciseService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExerciseTLController {

    private final ExerciseService exerciseService;
    private String userName = "";

    @GetMapping("/exercises")
    public String exercise(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("exercises", exerciseService.listExercisesByWorkoutId(TrackerApplication.getInstance().getCurrentWorkout().getId()));
        model.put("userName", userName+"'s exercises");
        return "exercises";
    }

    @PostMapping("/exerciseResults")
    public String exerciseResults(Exercise exercise) {
        TrackerApplication.getInstance().setCurrentExercise(exercise);
        return "redirect:/exerciseResults";
    }

    /*@PostMapping("/newExercise")
    public String create(Exercise exercise) {
        exerciseService.saveExercise(exercise);
        return "redirect:/exercises";
    }*/
}

