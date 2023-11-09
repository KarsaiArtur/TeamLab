package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public enum Equipment {
    Fully_Equipped,
    Well_Equipped,
    Poorly_Equipped;

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "howEquipped")
    private List<Gym> gyms;
}
