package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tracker.TrackerApplication;
import tracker.service.ExerciseResultService;

import java.util.Map;

/**
 * Controller osztály az exerciseResults.hmtl-hez, amelyen egy gyakorlathoz tartozó eredmények kerülnek kilistázásra
 */
@Controller
@RequiredArgsConstructor
public class ExerciseResultTLController {
    /**
     * eredmények service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final ExerciseResultService exerciseResultService;

    /**
     * létrehozza a htmlt, kilistázza a kiválasztott gyakorlathoz tartozó eredményeket, amelyeket a bejelentkezett felhasználó rögzített
     * @param model model
     * @return html neve
     */
    @GetMapping("/exerciseResults")
    public String results(Map<String, Object> model){
        String userName = TrackerApplication.getInstance().getLoggedInUser().getUserName();
        model.put("exerciseResults", exerciseResultService.listExerciseResultsByExerciseId(TrackerApplication.getInstance().getCurrentExercise().getId()));
        model.put("userName", userName +"'s results in "+TrackerApplication.getInstance().getCurrentExercise().getName()+" exercise");
        return "exerciseResults";
    }
}

