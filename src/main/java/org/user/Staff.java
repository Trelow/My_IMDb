package org.user;

import org.imdb.IMDB;
import org.production.*;
import org.actor.Actor;
import org.request.Request;
import org.user.experience.*;
import org.utils.*;
import org.exceptions.*;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import java.util.ArrayList;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@SuppressWarnings("unchecked")
public abstract class Staff<T extends Comparable<Object>> extends User<T> implements StaffInterface {
    private List<Request> assignedRequests;
    private SortedSet<T> contributions;

    public Staff() {
        this.assignedRequests = new ArrayList<>();
        this.contributions = new TreeSet<>();
    }

    @Override
    public void addProductionSystem(Production p) {
        contributions.add((T) p);
        List<Production> productions = null;
        productions = IMDB.getInstance().getProductions();
        productions.add(p);
        productions.sort(Production::compareTo);
        System.out.println("Experience: " + experience);
        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() != AccountType.Admin)
            updateExperience(new ContributionStrategy());
        System.out.println("Experience: " + experience);

    }

    public void addProductionToContributions(Production p) {
        contributions.add((T) p);
    }

    public void removeProductionFromContributions(Production p) {
        contributions.remove((T) p);
    }

    @Override
    public void addActorSystem(Actor a) {
        contributions.add((T) a);
        List<Actor> productions = null;
        productions = IMDB.getInstance().getActors();
        productions.add(a);
        productions.sort(Actor::compareTo);
        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() != AccountType.Admin)
            updateExperience(new ContributionStrategy());
    }

    public void addActorToContributions(Actor a) {
        contributions.add((T) a);
    }

    public void removeActorFromContributions(Actor a) {
        contributions.remove((T) a);
    }

    @Override
    public void removeProductionSystem(String name) {
        try {
            for (T production : contributions) {
                if (production instanceof Production) {
                    Production p = (Production) production;
                    if (p.getTitle().equals(name)) {
                        contributions.remove(p);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }


          // Also if delete contribution from favorites of users
            for (User<?> user : IMDB.getInstance().getUsers()) {
                for (Production production : IMDB.getInstance().getProductions()) {
                    if (production.getTitle().equals(name)) {
                        // if user has it in favorites, remove it
                        if (user.getFavorites().contains(production)) {
                            user.removeProductionFromFavorites(production);
                        }
                    }
                }
            }

            List<Production> productions = null;
            productions = IMDB.getInstance().getProductions();
            for (Production production : productions) {
                if (production.getTitle().equals(name)) {
                    productions.remove(production);
                    break;
                }
            }
    

        // Delete also production from contributions of other admins, if they have it
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getAccountType() == AccountType.Admin) {
                Admin<?> admin = (Admin<?>) user;
                Production p = admin.findProductionInContributions(name);
                if (p != null) {
                    admin.removeProductionFromContributions(p);
                }
            }
        }
        
    
    }

    @Override
    public void removeActorSystem(String name) {
        try {
            for (T actor : contributions) {
                if (actor instanceof Actor) {
                    Actor a = (Actor) actor;
                    if (a.getName().equals(name)) {
                        contributions.remove(a);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }


        // Also if delete contribution from favorites of users
        for (User<?> user : IMDB.getInstance().getUsers()) {
            for (Actor actor : IMDB.getInstance().getActors()) {
                if (actor.getName().equals(name)) {
                    // if user has it in favorites, remove it
                    if (user.getFavorites().contains(actor)) {
                        user.removeActorFromFavorites(actor);;
                    }
                }
            }
        }

        List<Actor> actors = null;
        actors = IMDB.getInstance().getActors();
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                actors.remove(actor);
                break;
            }
        }

        // Delete also actor from contributions of other admins, if they have it
        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user.getAccountType() == AccountType.Admin) {
                Admin<?> admin = (Admin<?>) user;
                Actor a = admin.findActorInContributions(name);
                if(a != null)
                    admin.removeActorFromContributions(a);
            }
        }

    }

    @Override
    public void updateProduction(Production p) {
        do {
            int options = 0;
            System.out.println("Choose field to update: ");
            System.out.println("1. Title");
            System.out.println("2. Directors");
            System.out.println("3. Actors");
            System.out.println("4. Genres");
            System.out.println("5. Description");
            System.out.println("6. Release Year");
            options += 6;
            if (p instanceof Series)
                System.out.println("7. Number of seasons");
            else if (p instanceof Movie)
                System.out.println("7. Duration");
            options++;
            if (p instanceof Series) {
                System.out.println(++options + ". Seasons");
            }
            System.out.println(++options + ". Exit");

            int option = InputManager.ToPositiveNumber(InputManager.ReadLine());
            try {
                if (option == 1) {
                    System.out.print("Enter new title: ");
                    String newTitle = InputManager.ReadLine();
                    p.setTitle(newTitle);
                    break;
                } else if (option == 2) {
                    do {
                        System.out.println("Directors: ");
                        p.printDirectors();
                        try {
                            // add director / remove director / exit
                            System.out.println("1. Add director   2. Remove director   3. Return to previous menu");
                            int option2 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                            // Print directors
                            System.out.print("Enter director name: ");
                            String directorName = InputManager.ReadLine();
                            if (option2 == 1) {
                                p.getDirectors().add(directorName);
                            } else if (option2 == 2) {
                                p.getDirectors().remove(directorName);
                            } else if (option2 == 3) {
                                break;
                            } else {
                                throw new InvalidCommandException("Invalid option");
                            }
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (true);
                } else if (option == 3) {
                    do {
                        System.out.println("Actors: ");
                        p.printActors();
                        try {
                            // add actor / remove actor / exit
                            System.out.println("1. Add actor   2. Remove actor   3. Return to previous menu");
                            int option2 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                            // Print actors
                            System.out.print("Enter actor name: ");
                            String actorName = InputManager.ReadLine();
                            if (option2 == 1) {
                                p.getActors().add(actorName);
                            } else if (option2 == 2) {
                                p.getActors().remove(actorName);
                            } else if (option2 == 3) {
                                break;
                            } else {
                                throw new InvalidCommandException("Invalid option");
                            }
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (true);
                } else if (option == 4) {
                    do {
                        System.out.println("Genres: ");
                        p.printGenres();
                        try {
                            // add genre / remove genre / exit
                            System.out.println("1. Add genre   2. Remove genre   3. Return to previous menu");
                            int option2 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                            // Print genres

                            if (option2 == 1) {
                                System.out.print("Enter genre name: ");
                                String genreName = InputManager.ReadLine();
                                boolean GenreAdded = false;
                                // Check if genre exists in Genre enum
                                for (Genre genre : Genre.values()) {
                                    if (genre.name().equals(genreName)) {
                                        // if genre already exists
                                        if (!p.getGenres().contains(genre)) {
                                            p.getGenres().add(genre);
                                        } else {
                                            System.out.println("Genre already exists");
                                        }
                                        GenreAdded = true;
                                        break;
                                    }
                                }
                                if (!GenreAdded)
                                    System.out.println("Invalid genre");
                            } else if (option2 == 2) {
                                System.out.print("Enter genre name: ");
                                String genreName = InputManager.ReadLine();
                                boolean GenreRemoved = false;
                                // Check if genre exists in Genre enum
                                for (Genre genre : Genre.values()) {
                                    if (genre.name().equals(genreName)) {
                                        if (p.getGenres().contains(genre)) {
                                            p.getGenres().remove(genre);
                                            GenreRemoved = true;
                                        }
                                        break;
                                    }
                                }

                                if (!GenreRemoved)
                                    System.out.println("Genre doesn't exist");

                            } else if (option2 == 3) {
                                break;
                            } else {
                                throw new InvalidCommandException("Invalid option");
                            }
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (true);
                } else if (option == 5) {
                    System.out.print("Enter new description: ");
                    String newDescription = InputManager.ReadLine();
                    p.setDescription(newDescription);

                } else if (option == 6) {
                    System.out.print("Enter new release year: ");
                    int newReleaseYear = InputManager.ReadAPositiveNumber();
                    if (p instanceof Movie)
                        ((Movie) p).setReleaseYear(newReleaseYear);
                    else if (p instanceof Series)
                        ((Series) p).setReleaseYear(newReleaseYear);
                } else if (option == 7) {
                    if (p instanceof Movie) {
                        System.out.print("Enter new duration: ");
                        int newDuration = InputManager.ReadAPositiveNumber();
                        ((Movie) p).setDuration(newDuration);
                    } else if (p instanceof Series) {
                        System.out.print("Enter new number of seasons: ");
                        int newNumberOfSeasons = InputManager.ReadAPositiveNumber();
                        ((Series) p).setNoSeasons(newNumberOfSeasons);
                    }
                } else if (option == options) {
                    break;
                } else if (option == 8) {
                    if (p instanceof Series) {
                        do {
                            System.out.println("Seasons: ");
                            ((Series) p).printSeasons();
                            try {
                                // add season / remove season / exit
                                System.out.println(
                                        "1. Select season   2. Add season   3. Remove season   4. Change season number   5. Return to previous menu");
                                int option2 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                                // Print seasons
                                if (option2 == 1) {
                                    System.out.print("Enter season number: ");
                                    int seasonNumber = InputManager.ToPositiveNumber(InputManager.ReadLine());
                                    List<Episode> season = ((Series) p).getEpisodes().get("Season " + seasonNumber);

                                    if (season != null) {
                                        do {
                                            System.out.println("Season " + seasonNumber + ": ");
                                            season.forEach(episode -> System.out.printf(
                                                    "Episode: %-30s Duration: %d\n", episode.getName(),
                                                    episode.getDuration()));
                                            try {
                                                // add episode / remove episode / exit
                                                System.out.println(
                                                        "1. Add episode   2. Remove episode   3. Change episode name   4. Change episode duration   5. Return to previous menu");
                                                int option3 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                                                // Print episodes
                                                if (option3 == 1) {
                                                    System.out.print("Enter episode name: ");
                                                    String episodeName = InputManager.ReadLine();
                                                    System.out.print("Enter episode duration: ");
                                                    int episodeDuration = InputManager.ReadAPositiveNumber();
                                                    season.add(new Episode(episodeName, episodeDuration));
                                                } else if (option3 == 2) {
                                                    System.out.print("Enter episode name: ");
                                                    String episodeName = InputManager.ReadLine();
                                                    // find episode by name
                                                    for (Episode episode : season) {
                                                        if (episode.getName().equals(episodeName)) {
                                                            season.remove(episode);
                                                            break;
                                                        }
                                                    }
                                                } else if (option3 == 4) {
                                                    System.out.print("Enter episode name: ");
                                                    String episodeName = InputManager.ReadLine();
                                                    // find episode by name
                                                    for (Episode episode : season) {
                                                        if (episode.getName().equals(episodeName)) {
                                                            System.out.print("Enter new duration: ");
                                                            int newDuration = InputManager.ReadAPositiveNumber();
                                                            episode.setDuration(newDuration);
                                                            break;
                                                        }
                                                    }
                                                } else if (option3 == 3) {
                                                    System.out.print("Enter episode name: ");
                                                    String episodeName = InputManager.ReadLine();
                                                    // find episode by name
                                                    for (Episode episode : season) {
                                                        if (episode.getName().equals(episodeName)) {
                                                            System.out.print("Enter new name: ");
                                                            String newName = InputManager.ReadLine();
                                                            episode.setName(newName);
                                                            break;
                                                        }
                                                    }
                                                } else if (option3 == 5) {
                                                    break;
                                                } else {
                                                    throw new InvalidCommandException("Invalid option");
                                                }
                                            } catch (InvalidCommandException e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } while (true);
                                    } else {
                                        System.out.println("Season doesn't exist");
                                    }
                                } else if (option2 == 2) {
                                    System.out.print("Enter season number: ");
                                    int seasonNumber = InputManager.ReadAPositiveNumber();
                                    List<Episode> season = ((Series) p).getEpisodes().get("Season " + seasonNumber);
                                    if (season == null) {
                                        ((Series) p).getEpisodes().put("Season " + seasonNumber, new ArrayList<>());
                                    } else {
                                        System.out.println("Season already exists");
                                    }
                                } else if (option2 == 3) {
                                    System.out.print("Enter season number: ");
                                    int seasonNumber = InputManager.ReadAPositiveNumber();
                                    List<Episode> season = ((Series) p).getEpisodes().get("Season " + seasonNumber);
                                    if (season != null) {
                                        ((Series) p).getEpisodes().remove("Season " + seasonNumber);
                                    } else {
                                        System.out.println("Season doesn't exist");
                                    }
                                } else if (option2 == 4) {
                                    System.out.print("Enter season number: ");
                                    int seasonNumber = InputManager.ReadAPositiveNumber();
                                    List<Episode> season = ((Series) p).getEpisodes().get("Season " + seasonNumber);
                                    if (season != null) {
                                        System.out.print("Enter new season number: ");
                                        int newSeasonNumber = InputManager.ReadAPositiveNumber();
                                        ((Series) p).getEpisodes().put("Season " + newSeasonNumber, season);
                                        ((Series) p).getEpisodes().remove("Season " + seasonNumber);
                                    } else {
                                        System.out.println("Season doesn't exist");
                                    }
                                } else if (option2 == 5) {
                                    break;
                                } else {
                                    throw new InvalidCommandException("Invalid option");
                                }

                            } catch (InvalidCommandException e) {
                                System.out.println(e.getMessage());
                            }
                        } while (true);
                    } else {
                        throw new InvalidCommandException("Invalid option");
                    }
                } else
                    throw new InvalidCommandException("Invalid option");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

    }

    @Override
    public void updateActor(Actor a) {
        // Choose field to updat
        do {
            System.out.println("Choose field to update: ");
            System.out.println("1. Name");
            System.out.println("2. Description");
            System.out.println("3. Filmography");
            System.out.println("4. Exit");
            int option = InputManager.ToPositiveNumber(InputManager.ReadLine());
            try {
                if (option == 1) {
                    System.out.print("Enter new name: ");
                    String newName = InputManager.ReadLine();
                    a.setName(newName);
                    break;
                }
                if (option == 2) {
                    System.out.print("Enter new description: ");
                    String newDescription = InputManager.ReadLine();
                    a.setDescription(newDescription);
                    break;
                } else if (option == 3) {
                    do {
                        System.out.println("Filmography: ");
                        a.printFilmography();
                        try {
                            // add to filmography/ remove from filmography / exit
                            System.out.println(
                                    "1. Add to filmography   2. Remove from filmography   3. Return to previous menu");

                            int option2 = InputManager.ToPositiveNumber(InputManager.ReadLine());
                            // Print filmography

                            System.out.print("Enter production name: ");
                            String productionName = InputManager.ReadLine();
                            String productionType = "";
                            do {
                                System.out.print("Enter production type (Movie/Series): ");
                                productionType = InputManager.ReadLine();
                            } while (!productionType.equals("Movie") && !productionType.equals("Series"));
                            if (option2 == 1) {
                                a.addProductionToFilmography(productionName, ProductionType.valueOf(productionType));
                            } else if (option2 == 2) {
                                a.removeProductionFromFilmography(productionName,
                                        ProductionType.valueOf(productionType));
                            } else if (option2 == 3) {
                                break;
                            } else {
                                throw new InvalidCommandException("Invalid option");
                            }
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (true);
                } else if (option == 4) {
                    break;
                } else {
                    throw new InvalidCommandException("Invalid option");
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    public Actor findActorInContributions(String name) {
        for (T actor : contributions) {
            if (actor instanceof Actor) {
                Actor a = (Actor) actor;
                if (a.getName().equals(name)) {
                    return a;
                }
            }
        }
        return null;
    }

    public Production findProductionInContributions(String name) {
        for (T production : contributions) {
            if (production instanceof Production) {
                Production p = (Production) production;
                if (p.getTitle().equals(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public void resolveRequest(Request request) {
        assignedRequests.remove(request);
    }

    public void printContributions() {
        for (T contribution : contributions) {
            if (contribution instanceof Production)
                System.out.println("\t" + ((Production) contribution).getTitle() + " ("
                        + contribution.getClass().getSimpleName() + ")");
            else if (contribution instanceof Actor)
                System.out.println("\t" + ((Actor) contribution).getName() + " (Actor)");
        }
    }
}
