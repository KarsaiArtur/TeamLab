package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tracker.TrackerApplication;
import tracker.service.ExerciseService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExerciseResultTLController {

    //private final ExerciseResultService exerciseResultService;
    private String userName = "";

    @GetMapping("/exerciseResults")
    public String results(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        //model.put("results", exerciseResultService.listResults());
        model.put("userName", userName+"'s results");
        return "exerciseResult";
    }

    /*@PostMapping("/open")
    public String open(ExerciseResult result) {
        exerciseResultService.saveExerciseResult(result);
        return "redirect:/results";
    }*/

    /*@PostMapping("/newExerciseResult")
    public String create(ExerciseResult result) {
        exerciseResultService.saveExerciseResult(result);
        return "redirect:/results";
    }*/
}

