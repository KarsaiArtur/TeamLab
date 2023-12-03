package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseRepository;
import tracker.repository.ExerciseResultRepository;
import tracker.repository.SetRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExerciseResultService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseResultRepository exerciseResultRepository;
    private final SetRepository setRepository;

    public ExerciseResult findResult(int id) {
        return exerciseResultRepository.findById(id).isEmpty() ? null : exerciseResultRepository.findById(id).get();
    }

    public List<ExerciseResult> listResults() {
        return exerciseResultRepository.findAll();
    }

    public List<ExerciseResult> listExerciseResultsByExerciseId(int exerciseId) {
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        return exercise.get().getExerciseResults();
    }

    @Transactional
    public void saveResult(double totalVolume){
        List<ExerciseResult> results = exerciseResultRepository.findByTotalVolume(totalVolume);
        for(ExerciseResult r: results)
            exerciseResultRepository.save(r);
    }

    @Transactional
    public ExerciseResult saveResult(ExerciseResult r){
        return exerciseResultRepository.save(r);
    }

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

    @Transactional
    public void deleteAll(){
        exerciseResultRepository.deleteAllInBatch();
    }

    @Transactional
    public void saveSetInResult(ExerciseResult r) {
        Optional<ExerciseResult> result = exerciseResultRepository.findById(r.getId());
        result.get().getSets().forEach(s -> setRepository.save(s));
    }

    public List<Set> listSets(ExerciseResult r){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(r.getId());
        return result.get().getSets();
    }

    @Transactional
    public void addNewSets(int id, List<Set> sets){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        for(Set s : sets) {
            s.countVolume();
            setRepository.save(s);
            result.get().addResult(s);
        }
        result.get().calculateTotalVolume();
    }

    @Transactional
    public void addExistingSetToResult(int id, int set_id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        Optional<Set> set = setRepository.findById(set_id);
        result.get().addResult(set.get());
    }

    @Transactional
    public void removeSetFromResult(int id, int set_id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        Optional<Set> set = setRepository.findById(set_id);
        result.get().removeResult(set.get());
        setRepository.delete(set.get());
    }

    public List<Set> listSets(int id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        return result.get().getSets();
    }
}
