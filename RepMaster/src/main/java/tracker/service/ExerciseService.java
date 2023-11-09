package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void addNewResults(int id, List<Set> sets) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        ExerciseResult newResult = new ExerciseResult();
        exercise.get().addNewResult(newResult);

        for(int i = 0; i < exercise.get().getSet_count(); i++){
            newResult.addResult(i, sets.get(i));     
        }
        newResult.setDate(LocalDate.now());
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
