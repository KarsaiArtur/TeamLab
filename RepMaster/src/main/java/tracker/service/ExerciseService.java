package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.ExerciseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void saveExercise(Exercise exercise){
        exerciseRepository.save(exercise);
    }
    @Transactional
    public void deleteExercise(Exercise exercise){
        exerciseRepository.delete(exercise);
    }

    public List<ExerciseResult> listExerciseResults(String id){
        List<Exercise> exercise = exerciseRepository.findByName(id);
        return exercise.get(0).getExerciseResults();
    }
    @Transactional
    public void addNewResults(String id, List<Set> sets) {
        List<Exercise> exercise = exerciseRepository.findByName(id);
        ExerciseResult newResult = ExerciseResult.builder().build();

        for(int i = 0; i < exercise.get(0).getSet_count(); i++){
            newResult.addResult(sets.get(i));
        }
        newResult.setDate(LocalDate.now());
        exercise.get(0).addNewResult(newResult);
    }

    public double findPR(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        double maxWeight = 0;

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            double curWeight = e.findPrWithinSets();
            maxWeight = (maxWeight < curWeight) ? curWeight : maxWeight;
        }

        return maxWeight;
    }

    public Set findMaxVolume(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Set maxVolume = new Set(0, 0);

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            Set curVolume = e.findMaxVolumeWithinSets();
            maxVolume = (maxVolume.getVolume() < curVolume.getVolume()) ? curVolume : maxVolume;
        }

        return maxVolume;
    }



}
