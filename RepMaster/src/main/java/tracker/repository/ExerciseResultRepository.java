package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.ExerciseResult;

import java.util.List;

public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Integer>{
    List<ExerciseResult> findByTotalVolume(double totalVolume);
}
