package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;

import java.util.List;

/**
 * a gyakorlatokhoz tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő gyakorlatokkal
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    /**
     * gyakorlatok keresése egy megadott névvel
     * @param name a keresett név
     * @return a keresett névvel rendelkező gyakorlatok
     */
    List<Exercise> findByName(String name);
}
