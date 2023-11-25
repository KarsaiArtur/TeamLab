package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Set{
    @Id
    @GeneratedValue
    private int id;
    private int repetition_count;
    private double weight;
    private double volume;
    @ManyToOne
    private ExerciseResult exerciseResult;

    public Set(){

    }
    public Set(int repetition_count, double weight){
        this.repetition_count = repetition_count;
        this.weight = weight;
        this.volume = repetition_count * weight;
    }

    public String toString(){
        return repetition_count + " x " + weight;
    }
}