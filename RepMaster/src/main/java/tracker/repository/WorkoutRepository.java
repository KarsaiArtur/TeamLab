package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
}
