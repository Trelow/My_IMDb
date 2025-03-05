package org.production;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductionFilter {
    private List<Genre> selectedGenres;
    private boolean byNumberOfRatings;

    public ProductionFilter() {
        this.selectedGenres = null;
        this.byNumberOfRatings = false;
    }

    public List<Production> SortProductions(List<Production> productions) {
        List<Production> filteredProductions = new ArrayList<>();

        if (this.selectedGenres == null) {
            filteredProductions.addAll(productions);
        } else {
            for (Production production : productions) {
                if (production.getGenres().containsAll(this.selectedGenres)) {
                    filteredProductions.add(production);
                }
            }
        }

        if (this.byNumberOfRatings) {
            filteredProductions.sort((p1, p2) -> p2.getRatings().size() - p1.getRatings().size());
        } else {
            // sort by title
            filteredProductions.sort(Production::compareTo);
        }

        return filteredProductions;

    }

    public void addGenre(Genre genre) {
        if (this.selectedGenres == null) {
            this.selectedGenres = new ArrayList<>();
        }
        // If already selected do nothing
        if (this.selectedGenres.contains(genre)) {
            return;
        }

        this.selectedGenres.add(genre);
    }

    public void removeGenre(Genre genre) {
        if (this.selectedGenres == null) {
            return;
        }

        if (this.selectedGenres.contains(genre)) {
            this.selectedGenres.remove(genre);
        }
    }

}
