package tracker.service;

import tracker.model.Rateable;

import java.util.List;

/**
 * az értékelhető osztályok service interfésze, amelyben deklarálva vannak a komplexebb függvények, amelyeket a webes réteg használ
 */
public interface RateableService {
    /**
     * id alapján keres és visszatér vele
     * @param id id, ami alapján keres
     * @return amit talált
     */
    public Rateable findById(int id);

    /**
     * visszaadja a lehetséges tartalmazóit
     * @return a lehetséges tartalmazói listája
     */
    public List<Rateable> getPossibleContainers();

    /**
     * hozzáadja az egyiket a másikhoz
     * @param idTo amihez hozzáadja
     * @param id amit hozzáad
     */
    public void addRateable(int idTo, int id);
}
