package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Equipment;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;
import tracker.service.WorkoutService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AddExerciseTLController {

    private final WorkoutService workoutService;
    private String userName = "";

    @GetMapping("/addExercise")
    public String addExercise(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("addE", new AddExerciseTLController.ExerciseCopy());
        return "addExercise";
    }

    @PostMapping("/addE")
    public String addE(ExerciseCopy exerciseCopy) {
        Exercise exercise = Exercise.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(exerciseCopy.getName())
                .publiclyAvailable(exerciseCopy.publiclyAvailable.equals("Yes"))
                .set_count(exerciseCopy.getSet_count())
                .repetition_count(exerciseCopy.getRepetition_count())
                .isCompound(exerciseCopy.isCompound.equals("Yes"))
                .primaryMuscleGroup(MuscleGroup.valueOf(exerciseCopy.getPrimaryMuscleGroup()))
                .build();
        workoutService.addNewExerciseToWorkout(TrackerApplication.getInstance().getCurrentWorkout().getId(), exercise);
        return "redirect:/exercises";
    }

    @Setter
    @Getter
    class ExerciseCopy {
        private String name;
        private int set_count;
        private int repetition_count;
        private String isCompound;
        private String publiclyAvailable;
        private String primaryMuscleGroup;
    }
}
