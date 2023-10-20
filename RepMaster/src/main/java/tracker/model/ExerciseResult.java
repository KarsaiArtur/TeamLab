package tracker.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseResult {
    private int id;
    private LocalDate date;
    private Exercise exercise;
    private Set[] sets;

    public void setExercise(Exercise exercise){
        this.exercise = exercise;
        sets = new Set[exercise.getSet_count()];
    }

    public void addResult(int index, Set result){
        sets[index]=result;
    }

    public double getWeightByIndex(int index){
        return sets[index].getWeight();
    }

    public int getRepCountByIndex(int index){
        return sets[index].getRepetition_count();
    }

    public double findPrWithinSets(){
        double maxWeight = 0;

        for(int i=0; i < exercise.getSet_count(); i++){
            double curWeight = getWeightByIndex(i);
            maxWeight = (maxWeight < curWeight)? curWeight : maxWeight;
        }

        return maxWeight;
    }

    public Set findMaxVolumeWithinSets(){
        Set maxVolume = new Set(0, 0);

        for(int i=0; i < exercise.getSet_count(); i++){
            double curWeight = getWeightByIndex(i);
            int repCount = getRepCountByIndex(i);
            if(maxVolume.getWeight() < curWeight && repCount >= exercise.getRepetition_count()){
                maxVolume = sets[i];
            }
        }

        return maxVolume;
    }
}
