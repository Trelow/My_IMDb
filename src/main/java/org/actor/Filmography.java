package org.actor;

import org.production.ProductionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filmography {
    private String name;
    private ProductionType type;

    public Filmography(String name, ProductionType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type;
    }
}