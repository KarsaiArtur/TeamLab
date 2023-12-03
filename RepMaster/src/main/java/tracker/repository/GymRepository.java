package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Gym;
/**
 * az edzőtermekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtermekkel
 */
public interface GymRepository extends JpaRepository<Gym, Integer>{
}
