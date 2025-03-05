package org.request;

import org.user.AccountType;
import org.user.Admin;
import org.user.User;
import org.imdb.IMDB;

import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {

    private static List<Request> requests = new ArrayList<>();

    public static void addRequest(Request request) {
        requests.add(request);
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getAccountType() == AccountType.Admin)
                ((Admin<?>) user).getAssignedRequests().add(request);
        }
    }

    public static void deleteRequest(Request request) {
        requests.remove(request);
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getAccountType() == AccountType.Admin)
                ((Admin<?>) user).getAssignedRequests().remove(request);
        }
    }
}
