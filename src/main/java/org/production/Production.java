package org.production;

import java.util.List;

import org.user.User;
import org.actor.Actor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Production implements Comparable<Object> {
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings = new java.util.ArrayList<>();
    private List<User<?>> usersWhoRatedAtLeastOnce = new java.util.ArrayList<>();
    private String description;
    private Double averageRating;
    private String poster = null;

    public abstract void displayInfo();

    @Override
    public int compareTo(Object arg0) {
        if (arg0 instanceof Production) {
            Production p = (Production) arg0;
            return this.title.compareTo(p.title);
        } else if (arg0 instanceof Actor) {
            Actor a = (Actor) arg0;
            return this.title.compareTo(a.getName());
        } else {
            return 0;
        }
    }

    public void removeActor(String name) {
        for (String actor : actors) {
            if (actor.equals(name)) {
                actors.remove(actor);
                break;
            }
        }
    }

    public void printDirectors() {
        for (String director : directors) {
            System.out.println(director);
        }
    }

    public void printActors() {
        for (String actor : actors) {
            System.out.println(actor);
        }
    }

    public void printGenres() {
        for (Genre genre : genres) {
            System.out.println(genre);
        }
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        sortRatingsByScore();
        recalculateAverageRating();
    }

    public void sortRatingsByScore() {
        ratings.sort((Rating r1, Rating r2) -> r2.getUser().getExperience() - r1.getUser().getExperience());
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        recalculateAverageRating();
    }

    public void recalculateAverageRating() {
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        averageRating = sum / ratings.size();
        // round to 2 decimal places
        averageRating = Math.round(averageRating * 100.0) / 100.0;
    }

}
