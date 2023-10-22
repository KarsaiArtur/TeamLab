package tracker.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class User {
    private static int uid = 1;
    private int id;
    private String username;
    public User(String username) {
        id = uid++;
        this.username = username;
    }

}
