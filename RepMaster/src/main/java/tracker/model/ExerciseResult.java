package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * gyakorlat eredmény osztály
 */
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
    /**
     * eredmény rögzítésének dátuma
     */
    private LocalDate date;
    /**
     * teljes edzés volumen(szett * ismétlés)
     */
    private double totalVolume;

    /**
     * gyakorlat, amihez az eredmény tartozik
     */
    @ManyToOne
    private Exercise exercise;

    /**
     * felhasználó, akihez, az eredmény tartozik
     */
    @ManyToOne
    private RegisteredUser registeredUser;

    /**
     * az eredményhez tartozó szettek listája
     */
    @OneToMany(mappedBy = "exerciseResult", fetch = FetchType.EAGER)
    private List<Set> sets;

    public void setExercise(Exercise exercise){
        this.exercise = exercise;
        sets = new ArrayList<>();
    }

    /**
     * szett hozzáadása
     * @param set szett, amit hozzáadunk
     */
    public void addSet(Set set){
        sets.add(set);
        set.setExerciseResult(this);
    }

    /**
     * szett kivétele
     * @param result szett, amit kiveszünk
     */
    public void removeSet(Set result){
            sets.remove(result);
    }

    /**
     * az eredményhez tartozó szettek között megkeresi a legjobb teljesítményt (legnagyobb súly)
     * @return a legnagyobb súly
     */
    public double findPrWithinSets() {
        double maxWeight = 0;
        for(Set s : sets)
            if(s.getWeight() > maxWeight)
                maxWeight = s.getWeight();
        return maxWeight;
    }

    /**
     * max teljesítményű szettet megtalálja
     * @return max teljesítményű szett
     */
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

    /**
     * a teljes edzés volument kiszámolja
     */
    public void calculateTotalVolume() {
        for(Set s : sets) {
            totalVolume += s.getVolume();
        }
    }
}
