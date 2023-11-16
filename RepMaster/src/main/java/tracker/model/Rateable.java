package tracker.model;

import java.util.List;

public interface Rateable {
    public void addRating(Rating r);
    public void removeRating(Rating r);
    public List<Rating> getRatings();
}
