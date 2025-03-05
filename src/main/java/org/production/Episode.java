package org.production;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Episode {
    private String name;
    private int duration;

    public Episode(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

}
