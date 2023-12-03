package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tracker.TrackerApplication;
import tracker.service.ExerciseResultService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExerciseResultTLController {

    private final ExerciseResultService exerciseResultService;
    private String userName = "";

    @GetMapping("/exerciseResults")
    public String results(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("exerciseResults", exerciseResultService.listExerciseResultsByExerciseId(TrackerApplication.getInstance().getCurrentExercise().getId()));
        model.put("userName", userName+"'s results in "+TrackerApplication.getInstance().getCurrentExercise().getName()+" exercise");
        return "exerciseResults";
    }
}

