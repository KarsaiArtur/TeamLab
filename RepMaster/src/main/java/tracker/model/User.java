package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String username;

    public User(){}
    public User(String username){
        this.username = username;
    }
}
