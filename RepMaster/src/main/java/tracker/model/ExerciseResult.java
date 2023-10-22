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
    private double totalVolume;

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

    public double findPrWithinSets() {
        double maxWeight = 0;
        for(Set s : sets)
            if(s.getWeight() > maxWeight)
                maxWeight = s.getWeight();
        return maxWeight;
    }

    public Set findMaxVolumeWithinSets() {
        double maxVolume = 0;
        Set maxVolumeSet = new Set(0, 0);
        for(Set s : sets) {
            if(s.getVolume() > maxVolume) {
                maxVolume = s.getVolume();
                maxVolumeSet = s;
            }
        }
        return maxVolumeSet;
    }

    private void calculateTotalVolume() {
        for(Set s : sets) {
            totalVolume += s.getVolume();
        }
    }
}
