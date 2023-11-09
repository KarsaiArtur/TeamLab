package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Split {
    @Id
    @GeneratedValue
    private int id;
    private SplitType name;
    private int numberOfDays;
    @OneToOne(mappedBy = "split")
    private Gym gym;

    private enum SplitType{
        Body_Part,
        Upper_Lower_Body,
        Push_Pull_Legs,
        Full_Body,
        Arnold,
        Other;
    }
}
