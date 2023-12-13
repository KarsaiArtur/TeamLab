package tracker.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Split(izomcsoport felbontás) osztály
 */
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
    /**
     * hány napra van bontva
     */
    private int numberOfDays;

    /**
     * izomcsoport bontás neve
     */
    @Enumerated(EnumType.STRING)
    private SplitType name;
    /**
     * edzőterem, amihez az izomcsoport bontás tartozik
     */
    @OneToOne
    private Gym gym;

    /**
     * izomcsoport bontás típus enum
     */
    public enum SplitType {
        Body_Part,
        Upper_Lower_Body,
        Push_Pull_Legs,
        Full_Body,
        Arnold,
        Other;
    }
}
