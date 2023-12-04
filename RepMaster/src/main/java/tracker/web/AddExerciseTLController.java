package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;
import tracker.service.ExerciseService;
import tracker.service.WorkoutService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller osztály az addExercise.hmtl-hez, amelyen egy gyakorlat hozzáadása történik
 */
@Controller
@RequiredArgsConstructor
public class AddExerciseTLController {
    /**
     * edzőterv service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final WorkoutService workoutService;
    /**
     * gyakorlatok service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final ExerciseService exerciseService;
    /**
     * gyakorlat másolata, ezen keresztül történik a böngészőben az adatok beolvasása
     */
    private ExerciseCopy exerciseCopy_ = new ExerciseCopy();

    /**
     * létrehozza a htmlt
     * @param model model
     * @return html neve
     */
    @GetMapping("/addExercise")
    public String addExercise(Map<String, Object> model){
        String userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("userName", userName +"'s gyms");
        model.put("addE", exerciseCopy_);
        model.put("muscle", new ExerciseCopy());
        return "addExercise";
    }

    /**
     * egy gyakorlat hozzáadása az adatbázisba a beolvasott értékek alapján
     * @param exerciseCopy a böngészőben a felhasználó által megadott adatok
     * @return a gyakorlat hozzáadása után visszamegyünk az gyakorlatok oldalra
     */
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

        for (String muscle: exerciseCopy_.getSecondaryMuscleGroups()) {
            exerciseService.addSecondaryMuscleGroup(exercise.getId(), MuscleGroup.valueOf(muscle));
        }
        return "redirect:/exercises";
    }

    /**
     * másodlagosan edzett izomcsoport hozzáadása az elmentendő gyakorlathoz
     * @param exerciseCopy ez az objektum tárolja el a hozzáadandó izomcsoportot
     * @return az izomcsoport hozzáadása után folytatjuk a gyakorlat adatainak kitöltését
     */
    @PostMapping("/addSecondary")
    public String addSecondary(ExerciseCopy exerciseCopy) {
        exerciseCopy_.getSecondaryMuscleGroups().add(exerciseCopy.getPrimaryMuscleGroup());
        return "redirect:/addExercise";
    }

    /**
     * belső osztály, a html-en ezen keresztül történik az adatok beolvasása
     */
    @Setter
    @Getter
    class ExerciseCopy {
        /**
         * gyakorlat neve
         */
        private String name;
        /**
         * szettek száma
         */
        private int set_count = 1;
        /**
         * ismétlések száma
         */
        private int repetition_count = 1;
        /**
         * összetett gyakorlat-e
         */
        private String isCompound;
        /**
         * publikus-e (megjelenik a keresésben és mások is használhatják)
         */
        private String publiclyAvailable;
        /**
         * fő izomcsoport, amit edz
         */
        private String primaryMuscleGroup;
        /**
         * másodlagos izomcsoportok, amiket edz
         */
        private List<String> secondaryMuscleGroups = new ArrayList<>();
    }
}
