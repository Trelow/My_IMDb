package org.production;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie extends Production {
    private int duration;
    private int releaseYear;

    @Override
    public void displayInfo() {
        System.out.println("Title: " + this.getTitle());
        System.out.println("Duration: " + this.duration);
        System.out.println("Release Year: " + this.releaseYear);
        System.out.print("Directors: ");
        List<String> directors = this.getDirectors();
        for (String director : directors) {
            System.out.print(director);
            if (directors.indexOf(director) != directors.size() - 1)
                System.out.print(", ");
        }
        System.out.println();
        List<String> actors = this.getActors();
        System.out.print("Actors: ");
        for (String actor : actors) {
            System.out.print(actor);
            if (actors.indexOf(actor) != actors.size() - 1)
                System.out.print(", ");
        }
        System.out.println();
        System.out.print("Genres: ");
        List<Genre> genres = this.getGenres();
        for (Genre genre : genres) {
            System.out.print(genre);
            if (genres.indexOf(genre) != genres.size() - 1)
                System.out.print(", ");
        }

        System.out.println();
        System.out.print("Ratings: ");
        List<Rating> ratings = this.getRatings();
        if (ratings.size() == 0) {
            System.out.println("No ratings");
        }
        for (Rating rating : ratings) {
            if (ratings.indexOf(rating) == 0) {
                System.out.println(rating);
            } else {
                System.out.println("\t " + rating);
            }
        }
        System.out.println("Description: " + this.getDescription());
        System.out.println("Average Rating: " + this.getAverageRating());
    }
}
