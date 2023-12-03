package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * szett osztály, amely eltárolja a szetthez tartozó adatokat (ismétlések száma, súly)
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Set{
    @Id
    @GeneratedValue
    private int id;
    /**
     * ismétlések száma
     */
    private int repetition_count;
    /**
     * súly kg-ban
     */
    private double weight;
    /**
     * a szetthez tartozó edzés volumen
     */
    private double volume;
    @ManyToOne
    private ExerciseResult exerciseResult;

    /**
     * konstruktor, amely a bekért ismétlések száma és súly alapján kiszámolja az edzés volument
     * @param repetition_count a bekért ismétlések száma
     * @param weight a bekért súly
     */
    public Set(int repetition_count, double weight){
        this.repetition_count = repetition_count;
        this.weight = weight;
        this.volume = repetition_count * weight;
    }

    /**
     * az eltárolt ismétlések száma és súly alapján kiszámolja az edzés volument
     */
    public void countVolume(){
        this.volume = repetition_count * weight;
    }

    /**
     * stringgé alakítja az osztály tartalmát
     * @return az átalakított string
     */
    public String toString(){
        return repetition_count + " x " + weight;
    }
}