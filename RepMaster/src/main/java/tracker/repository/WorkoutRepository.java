package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Gym;
import tracker.model.Workout;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
}
