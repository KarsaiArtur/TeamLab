package tracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Split {
    private SplitType name;
    private int numberOfDays;

    private enum SplitType{
        Body_Part,
        Upper_Lower_Body,
        Push_Pull_Legs,
        Full_Body,
        Arnold,
        Other;
    }
}
