package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseResultRepository;
import tracker.repository.SetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExerciseResultService {

    private final ExerciseResultRepository exerciseResultRepository;
    private final SetRepository setRepository;

    public ExerciseResult findResult(int id) {
        return exerciseResultRepository.findById(id).isEmpty() ? null : exerciseResultRepository.findById(id).get();
    }

    public List<ExerciseResult> listResults() {
        return exerciseResultRepository.findAll();
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
    public void addNewSet(int id, Set set){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        setRepository.save(set);
        result.get().addResult(set);
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
    }

    public List<Set> listSets(int id){
        Optional<ExerciseResult> result = exerciseResultRepository.findById(id);
        return result.get().getSets();
    }


}
