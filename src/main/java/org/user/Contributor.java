package org.user;

import org.imdb.*;
import org.request.*;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contributor<T extends Comparable<Object>> extends Staff<T> implements RequestsManager {
    List<Request> requests = new ArrayList<>();

    // Implementarea metodelor din RequestsManager
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
}