package org.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.observer.Observer;
import org.observer.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request implements Subject {
    private RequestType type;
    private LocalDateTime creationDate;
    private String description;
    private String userUsername;
    private String resolverUsername;

    private List<Observer> observers = new ArrayList<>();

    public Request(RequestType type, LocalDateTime creationDate, String description, String userUsername,
            String resolverUsername) {
        this.type = type;
        this.creationDate = creationDate;
        this.description = description;
        this.userUsername = userUsername;
        this.resolverUsername = resolverUsername;
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
