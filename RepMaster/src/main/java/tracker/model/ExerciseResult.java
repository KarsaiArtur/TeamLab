package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExerciseResult {
    @Id
    @GeneratedValue
    private int id;

    private LocalDate date;
    private double totalVolume;

    @ManyToOne
    private Exercise exercise;

    @OneToMany(mappedBy = "exerciseResult")
    private List<Set> sets;

    public void setExercise(Exercise exercise){
        this.exercise = exercise;
        sets = new ArrayList<>();
    }

    public void addResult(Set result){
        sets.add(result);
    }

    public double getWeightByIndex(int index){
        return sets.get(index).getWeight();
    }

    public int getRepCountByIndex(int index){
        return sets.get(index).getRepetition_count();
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

    public void calculateTotalVolume() {
        for(Set s : sets) {
            totalVolume += s.getVolume();
        }
    }
}
