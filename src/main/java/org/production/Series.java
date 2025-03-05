package org.production;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Series extends Production {
    private int releaseYear;
    private int noSeasons;
    private Map<String, List<Episode>> episodes = new LinkedHashMap<>();

    @Override
    public void displayInfo() {
        System.out.println("Title: " + this.getTitle());
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
        List<Rating> ratings = this.getRatings();
        System.out.print("Ratings: ");
        for (Rating rating : ratings) {
            // print first rating without tab and other ratings with tab
            if (ratings.indexOf(rating) == 0) {
                System.out.println(rating);
            } else {
                System.out.println("\t " + rating);
            }
        }
        System.out.println("Description: " + this.getDescription());
        System.out.println("Average Rating: " + this.getAverageRating());
        System.out.println("Number of seasons: " + this.noSeasons);

        // For each season display the episodes
        for (Map.Entry<String, List<Episode>> entry : episodes.entrySet()) {
            System.out.println("\t" + entry.getKey());
            for (Episode episode : entry.getValue()) {
                System.out.printf("\t\tEpisode: %-30s Duration: %d\n", episode.getName(), episode.getDuration());
            }
        }
    }

    public void addSeason(String seasonName, List<Episode> episodes) {
        this.episodes.put(seasonName, episodes);
    }

    public void addEpisode(String seasonName, Episode episode) {
        this.episodes.get(seasonName).add(episode);
    }

    public void printSeasons() {
        for (Map.Entry<String, List<Episode>> entry : episodes.entrySet()) {
            System.out.println(entry.getKey());
        }
    }
}
