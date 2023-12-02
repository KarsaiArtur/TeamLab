package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.MuscleGroup;
import tracker.model.Set;
import tracker.service.ExerciseService;
import tracker.service.WorkoutService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AddExerciseResultTLController {

    private final ExerciseService exerciseService;
    private String userName = "";

    @GetMapping("/addExerciseResult")
    public String addExerciseResult(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("addER", new AddExerciseResultTLController.ExerciseResultCopy());
        return "addExerciseResult";
    }

    @PostMapping("/addER")
    public String addER(ExerciseResultCopy exerciseResultCopy) {
        ExerciseResult result = ExerciseResult.builder()
                .registeredUser(TrackerApplication.getInstance().getLoggedInUser())
                .exercise(TrackerApplication.getInstance().getCurrentExercise())
                 //.sets(Set.builder().repetition_count(repetition_count).weight(weight).build())
                .date(exerciseResultCopy.getDate())
                .build();
        exerciseService.addNewResult(TrackerApplication.getInstance().getCurrentExercise().getId(), result);
        return "redirect:/exerciseResults";
    }

    @Setter
    @Getter
    class ExerciseResultCopy {
        private int repetition_count;
        private double weight;
        private LocalDate date;
    }
}
