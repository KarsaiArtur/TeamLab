package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.RegisteredUser;

import java.util.List;
/**
 * a regisztrált felhasználókhoz tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált felhasználókkal
 */
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer>{
    /**
     * regisztrált felhasználók keresése egy megadott névvel
     * @param userName a keresett név
     * @return a keresett névvel rendelkező felhasználók
     */
    List<RegisteredUser> findByUserName(String userName);
}
