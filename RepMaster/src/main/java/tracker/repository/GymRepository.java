package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Exercise;
import tracker.model.Gym;
import tracker.model.MuscleGroup;
import tracker.model.Split;

import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Integer>{
}
