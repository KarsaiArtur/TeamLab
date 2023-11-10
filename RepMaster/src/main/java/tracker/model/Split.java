package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Split {
    @Id
    @GeneratedValue
    private int id;
    private SplitType name;
    int numberOfDays;
    @OneToOne(mappedBy = "split")
    private Gym gym;

    public enum SplitType{
        Body_Part,
        Upper_Lower_Body,
        Push_Pull_Legs,
        Full_Body,
        Arnold,
        Other;
    }
}
