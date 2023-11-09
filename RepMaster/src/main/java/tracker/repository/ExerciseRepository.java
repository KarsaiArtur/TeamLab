package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

}
