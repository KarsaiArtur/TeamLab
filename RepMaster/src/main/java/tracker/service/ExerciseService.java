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
        ExerciseResult newResult = new ExerciseResult();
        exercise.addNewResult(newResult);

        for(int i=0; i < exercise.getSet_count(); i++){
            newResult.addResult(i, sets[i]);
        }
        newResult.setDate(LocalDate.now());
    }

    public double findPR(int id){
        Exercise exercise = exerciseRepository.findById(id);

        double maxWeight = 0;

        for (ExerciseResult e: exercise.getExerciseResults()) {
            double curWeight = e.findPrWithinSets();
            maxWeight = (maxWeight < curWeight)? curWeight : maxWeight;
        }

        return maxWeight;
    }

    public Set findMaxVolume(int id){
        Exercise exercise = exerciseRepository.findById(id);
        Set maxVolume = new Set(0, 0);

        for (ExerciseResult e: exercise.getExerciseResults()) {
            Set curVolume = e.findMaxVolumeWithinSets();
            maxVolume = (maxVolume.getWeight() < curVolume.getWeight())? curVolume : maxVolume;
        }

        return maxVolume;
    }



}
