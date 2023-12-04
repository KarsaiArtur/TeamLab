package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.TrackerApplication;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.service.ExerciseResultService;
import tracker.service.ExerciseService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller osztály az addExerciseResult.hmtl-hez, amelyen egy eredmény hozzáadása történik
 */
@Controller
@RequiredArgsConstructor
public class AddExerciseResultTLController {
    private final ExerciseService exerciseService;
    private final ExerciseResultService exerciseResultService;
    private String userName = "";

    @GetMapping("/addExerciseResult")
    public String addExerciseResult(Map<String, Object> model){
        userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("addER", new AddExerciseResultTLController.ExerciseResultCopy());
        return "addExerciseResult";
    }

    /**
     * egy eredmény hozzáadása az adatbázisba a beolvasott értékek alapján
     * @param exerciseResultCopy a böngészőben a felhasználó által megadott adatok
     * @return az eredmény hozzáadása után visszamegyünk az eredmények oldalra
     */
    @PostMapping("/addER")
    public String addER(ExerciseResultCopy exerciseResultCopy) {
        ExerciseResult result = ExerciseResult.builder()
                .registeredUser(TrackerApplication.getInstance().getLoggedInUser())
                .exercise(TrackerApplication.getInstance().getCurrentExercise())
                .date(LocalDate.now())
                .build();
        exerciseService.addNewResult(TrackerApplication.getInstance().getCurrentExercise().getId(), result);
        exerciseResultService.addNewSets(result.getId(), exerciseResultCopy.getSets());
        return "redirect:/exerciseResults";
    }

    /**
     * belső osztály, a html-en ezen keresztül történik az adatok beolvasása
     */
    @Setter
    @Getter
    class ExerciseResultCopy {
        private List<Set> sets;

        public ExerciseResultCopy(){
            sets = new ArrayList<>();
            for(int i=0; i<TrackerApplication.getInstance().getCurrentExercise().getSet_count(); i++){
                sets.add(new Set());
            }
        }

    }
}
