package org.actor;

import java.util.List;

import org.production.Production;
import org.production.ProductionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Actor implements Comparable<Object> {
    private String name;
    private String description;
    private List<Filmography> filmography;

    public Actor(String name, String description, List<Filmography> filmography) {
        this.name = name;
        this.description = description;
        this.filmography = filmography;
    }

    @Override
    public int compareTo(Object arg0) {
        if (arg0 instanceof Actor) {
            Actor a = (Actor) arg0;
            return this.name.compareTo(a.name);
        } else if (arg0 instanceof Production) {
            Production p = (Production) arg0;
            return this.name.compareTo(p.getTitle());
        } else {
            return 0;
        }
    }

    public int displayInfo() {
        System.out.println("Name: " + this.name);
        System.out.println("Description: " + this.description);
        System.out.println("Filmography: ");
        this.printFilmography();
        return 0;
    }

    public void printFilmography() {
        for (Filmography pair : this.filmography) {
            System.out.println("\t" + pair);
        }
    }

    public void addProductionToFilmography(String name, ProductionType type) {
        Filmography pair = new Filmography(name, type);
        this.filmography.add(pair);
    }

    public void removeProductionFromFilmography(String name, ProductionType type) {
        for (Filmography pair : this.filmography) {
            if (pair.getName().equals(name) && pair.getType().equals(type)) {
                this.filmography.remove(pair);
                break;
            }
        }
        System.out.println("No Such Production");
    }

}
