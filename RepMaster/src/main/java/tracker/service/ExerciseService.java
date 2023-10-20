package tracker.service;

import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseRepository;

import java.time.LocalDate;

public class ExerciseService {
    private ExerciseRepository exerciseRepository;

    public void addNewResults(int id, Set[] sets){
        Exercise exercise = exerciseRepository.findById(id);
        ExerciseResult newResult = new ExerciseResult(exercise.getSet_count(), exercise.getId());
        for(int i=0; i < exercise.getSet_count(); i++){
            newResult.addResult(i, sets[i]);
        }
        newResult.setDate(LocalDate.now());
        exercise.addNewResult(newResult);
    }

}
