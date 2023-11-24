package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tracker.TrackerApplication;
import tracker.service.ExerciseService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExerciseTLController {

    private final ExerciseService exerciseService;
    private String userName = "";

    @GetMapping("/exercises")
    public String exercises(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        //model.put("exercises", exerciseService.listExercises());
        model.put("userName", userName+"'s exercises");
        return "exercise";
    }

    /*@PostMapping("/open")
    public String open(Exercise exercise) {
        exerciseService.saveExercise(exercise);
        return "redirect:/exercises";
    }*/

    /*@PostMapping("/newExercise")
    public String create(Exercise exercise) {
        exerciseService.saveExercise(exercise);
        return "redirect:/exercises";
    }*/
}

