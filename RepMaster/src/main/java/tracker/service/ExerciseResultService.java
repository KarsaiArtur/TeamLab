package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseRepository;
import tracker.repository.ExerciseResultRepository;
import tracker.repository.SetRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * gyakorlat eredmény service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ.
 */
@RequiredArgsConstructor
@Service
public class ExerciseResultService {
    /**
     * a gyakorlatokhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő gyakorlatokkal
     */
    private final ExerciseRepository exerciseRepository;
    /**
     * az eremdényekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő eredményekkel
     */
    private final ExerciseResultRepository exerciseResultRepository;
    /**
     * a szettekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő szettekkel
     */
    private final SetRepository setRepository;

    /**
     * visszaadja az aktuális felhasználó adott gyakorlatához tartozó eredményeket
     * @param exerciseId eredményeket tartalmazó gyakorlat id-ja
     * @return eredmények
     */
    public List<ExerciseResult> listExerciseResultsByExerciseId(int exerciseId) {
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        var results = exercise.get().getExerciseResults();
        results = results.stream().filter(a -> a.getRegisteredUser().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()).collect(Collectors.toList());
        return results;
    }

    /**
     * törli az adott id-jú eredményt és vele együtt a szettjeit
     * @param id törölni kívánt eredmény id-ja
     */
    @Transactional
    public void deleteResult(int id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        if(result.get().getSets() != null) {
            int size = result.get().getSets().size();
            for (int i = 0; i < size; i++) {
                removeSetFromResult(result.get().getId(), result.get().getSets().get(0).getId());
            }
        }
        result.get().getExercise().removeResult(result.get());
        exerciseResultRepository.delete(result.get());
    }

    /**
     * Új szetteket hozzáad az eredményhez és elmenti őket
     * @param id eredmény id-ja, amihez a szetteket hozzáadjuk
     * @param sets szettek listája, amit hozzáadunk az eredményhez
     */
    @Transactional
    public void addNewSets(int id, List<Set> sets){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        for(Set s : sets) {
            s.countVolume();
            setRepository.save(s);
            result.get().addSet(s);
        }
        result.get().calculateTotalVolume();
    }


    /**
     * kiveszi és törli a szettet az eredményből
     * @param id szettet tartalmazó eredmény id-ja
     * @param set_id szett id-ja, amit törlünk
     */
    @Transactional
    public void removeSetFromResult(int id, int set_id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        Optional<Set> set = setRepository.findById(set_id);
        result.get().removeSet(set.get());
        setRepository.delete(set.get());
    }
}
