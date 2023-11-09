package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.ExerciseResult;

public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Integer>{

}
