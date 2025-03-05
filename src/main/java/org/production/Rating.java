package org.production;

import java.util.ArrayList;
import java.util.List;

import org.user.User;

import lombok.Getter;
import lombok.Setter;

import org.observer.Observer;
import org.observer.Subject;

@Getter
@Setter
public class Rating implements Subject {
    private String username;
    private int score;
    private String comment;
    private User<?> user;
    private List<Observer> observers = new ArrayList<>();

    public Rating(String username, int score, String comment) {
        this.username = username;
        this.setScore(score);
        this.comment = comment;
    }

    // Getteri și Setteri
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score >= 1 && score <= 10) {
            this.score = score;
        } else {
            throw new IllegalArgumentException("Nota trebuie să fie între 1 și 10.");
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "username = '" + username + '\'' +
                ", score = " + score +
                ", comment = '" + comment + '\'';
    }

    @Override
    public void register(Observer obj) {
        observers.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

}
