package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.service.ExerciseService;
import tracker.service.WorkoutService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExerciseTLController {
    private final ExerciseService exerciseService;
    private final WorkoutService workoutService;
    private String userName = "";

    @GetMapping("/exercises")
    public String exercise(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("exercises", exerciseService.listExercisesByWorkoutId(TrackerApplication.getInstance().getCurrentWorkout().getId()));
        model.put("userName", userName+"'s exercises");
        return "exercises";
    }

    @PostMapping("/exerciseResults")
    public String exerciseResults(@RequestParam("exerciseId") int id) {
        Exercise exercise = exerciseService.findExercise(id);
        TrackerApplication.getInstance().setCurrentExercise(exercise);
        return "redirect:/exerciseResults";
    }

    @PostMapping("/deleteExercise")
    public String deleteExercise(@RequestParam("exerciseId") int id) {
        Exercise exercise = exerciseService.findExercise(id);
        if(exercise.isPubliclyAvailable()){
            workoutService.removeExerciseFromWorkout(TrackerApplication.getInstance().getCurrentWorkout().getId(), id);
        }
        else
            exerciseService.deleteExercise(id);
        return "redirect:/exercises";
    }
}

