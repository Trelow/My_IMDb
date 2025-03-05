package org.user;

import org.imdb.*;
import org.production.Production;
import org.production.Rating;

import org.actor.Actor;

public class Admin<T extends Comparable<Object>> extends Staff<T> {

    public void addUser(User<?> user) {
        IMDB.getInstance().getUsers().add(user);
    }

    public void removeUser(User<?> userToRemove) {
        IMDB.getInstance().getUsers().remove(userToRemove);

        if (userToRemove.accountType == AccountType.Regular)
            for (Production production : IMDB.getInstance().getProductions()) {
                for (Rating rating : production.getRatings()) {
                    if (rating.getUsername().equals(userToRemove.getUsername())) {
                        production.removeRating(rating);
                    }
                    break;
                }
            }

        if (userToRemove.accountType == AccountType.Contributor) {
            Staff<?> staff = (Staff<?>) userToRemove;
            for (Object object : staff.getContributions()) {
                for (User<?> user : IMDB.getInstance().getUsers()) {
                    if (user.accountType == AccountType.Admin) {
                        Staff<?> admin = (Staff<?>) user;
                        if (object instanceof Actor)
                            admin.addActorToContributions((Actor) object);
                        if (object instanceof Production)
                            admin.addProductionToContributions((Production) object);
                    }
                }
            }
        }
    }

}
