package org.user;

import org.observer.Observer;
import org.production.Production;
import org.user.experience.ExperienceStrategy;
import org.actor.Actor;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import java.util.ArrayList;

import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Setter
@SuppressWarnings("unchecked")
public abstract class User<T extends Comparable<Object>> implements Observer {
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDateTime dateOfBirth;

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.dateOfBirth = builder.dateOfBirth;
        }

        @Override
        public String toString() {
            return "Information{" +
                    "credentials=" + credentials +
                    ", name='" + name + '\'' +
                    ", country='" + country + '\'' +
                    ", age=" + age +
                    ", gender='" + gender + '\'' +
                    ", dateOfBirth=" + dateOfBirth +
                    '}';
        }

        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDateTime dateOfBirth;

            public InformationBuilder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder dateOfBirth(LocalDateTime dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
                return this;
            }

            public Information build() {
                // User<?> user = new User<>();
                return new Information(this);
            }
        }

    }

    protected Information information;
    protected AccountType accountType;
    protected String username;
    protected int experience;
    protected List<String> notifications;
    protected SortedSet<T> favorites;

    public User() {
        this.notifications = new ArrayList<>();
        this.favorites = new TreeSet<>();
    }

    @Override
    public void update(String notification) {
        notifications.add(notification);
    }

    public void PrintFavorites() {
        List<T> favorites = new ArrayList<>(this.favorites);
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i) instanceof Actor)
                System.out.println(i + 1 + ". " + ((Actor) favorites.get(i)).getName());
            else if (favorites.get(i) instanceof Production)
                System.out.println(i + 1 + ". " + ((Production) favorites.get(i)).getTitle());
        }
    }

    public boolean verifyCredentials(Credentials credentials) {
        if (information.credentials.getEmail().equals(credentials.getEmail())
                && information.credentials.getPassword().equals(credentials.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public String getEmail() {
        return information.credentials.getEmail();
    }

    public String getPassword() {
        return information.credentials.getPassword();
    }

    public String getName() {
        return information.name;
    }

    public String getCountry() {
        return information.country;
    }

    public int getAge() {
        return information.age;
    }

    public String getGender() {
        return information.gender;
    }

    public LocalDateTime getDateOfBirth() {
        return information.dateOfBirth;
    }

    public void addActorToFavorites(Actor actor) {
        favorites.add((T) actor);
    }

    public void addProductionToFavorites(Production production) {
        favorites.add((T) production);
    }

    public void removeActorFromFavorites(Actor actor) {
        favorites.remove((T) actor);
    }

    public void removeProductionFromFavorites(Production production) {
        favorites.remove((T) production);
    }

    public void updateExperience(ExperienceStrategy strategy) {
        this.experience += strategy.calculateExperience();
    };

    @Override
    public String toString() {
        return "User{" +
                "information='" + information + '\'' +
                ", accountType=" + accountType +
                ", username='" + username + '\'' +
                ", experience=" + experience +
                ", notifications=" + notifications +
                ", favorites=" + favorites +
                '}';
    }
}
