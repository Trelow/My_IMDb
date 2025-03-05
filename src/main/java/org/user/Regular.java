package org.user;

import org.request.*;
import org.user.experience.ReviewStrategy;
import org.imdb.IMDB;
import org.production.Production;
import org.production.Rating;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Regular<T extends Comparable<Object>> extends User<T> implements RequestsManager {
    private List<Request> requests = new ArrayList<>();

    @Override
    public void createRequest(Request request) {
        requests.add(request);
        IMDB.getInstance().getRequests().add(request);
        Staff<?> staff = null;
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getUsername().equals(request.getResolverUsername())) {
                staff = (Staff<?>) user;
                break;
            }
        }
        if (staff != null)
            staff.getAssignedRequests().add(request);
    }

    @Override
    public void removeRequest(Request request) {
        requests.remove(request);
        IMDB.getInstance().getRequests().remove(request);
        Staff<?> staff = null;
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getUsername().equals(request.getResolverUsername())) {
                staff = (Staff<?>) user;
                break;
            }
        }
        if (staff != null)
            staff.getAssignedRequests().remove(request);

    }

    public void addReview(Rating rating, Production production) {
        production.getRatings().add(rating);

        boolean userALreadyRated = false;
        for (User<?> user : production.getUsersWhoRatedAtLeastOnce()) {
            if (user == this) {
                userALreadyRated = true;
                break;
            }
        }
        if (!userALreadyRated) {
            this.updateExperience(new ReviewStrategy());
            production.getUsersWhoRatedAtLeastOnce().add(IMDB.getInstance().getCurrentLoggedInUser());
        }

    }
}
