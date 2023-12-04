package tracker.model;

import tracker.web.RateableDetailTLController;

import java.util.List;

/**
 * egy absztrakt osztály, amely jelképezi az értékelhető osztályokat
 */
public abstract class Rateable {
    /**
     * absztrakt függvény, amely hozzáad egy értékelést
     * @param r a hozzáadott értékelés
     */
    public abstract void addRating(Rating r);
    /**
     * absztrakt függvény, amely eltávolít egy értékelést
     * @param r az eltávolított értékelés
     */
    public abstract void removeRating(Rating r);
    public abstract List<Rating> getRatings();
    public abstract String getName();
    public abstract int getId();
    /**
     * absztrakt függvény, amely visszaadja az osztály részleteit/tulajdonságait
     * @return osztály részletei/tulajdonságai
     */
    public abstract List<RateableDetailTLController.Details> details();
    public abstract boolean isPubliclyAvailable();
}
