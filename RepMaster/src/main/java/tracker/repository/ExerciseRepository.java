package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    List<Exercise> findByName(String name);
    List<Exercise> findByPrimaryMuscleGroup(MuscleGroup muscleGroup);
}
