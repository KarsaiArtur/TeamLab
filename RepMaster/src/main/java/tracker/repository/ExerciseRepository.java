package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Exercise;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    List<Exercise> findByName(String name);
}
