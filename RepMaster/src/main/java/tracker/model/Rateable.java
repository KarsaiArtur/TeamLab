package tracker.model;

import tracker.web.RateableDetailTLController;

import java.util.List;

public abstract class Rateable {
    public abstract void addRating(Rating r);
    public abstract void removeRating(Rating r);
    public abstract List<Rating> getRatings();
    public abstract String getName();
    public abstract int getId();
    public abstract List<RateableDetailTLController.Details> details();
    public abstract boolean isPubliclyAvailable();
}
