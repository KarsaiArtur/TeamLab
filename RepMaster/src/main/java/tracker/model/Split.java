package tracker.model;

import jakarta.persistence.*;
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

    private int numberOfDays;

    @Enumerated(EnumType.STRING)
    private SplitType name;
    @OneToOne
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
