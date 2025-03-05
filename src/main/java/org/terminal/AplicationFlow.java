package org.terminal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.actor.*;
import org.exceptions.*;
import org.production.*;
import org.user.*;
import org.imdb.*;
import org.request.*;
import org.utils.GeneratePassword;
import org.utils.InputManager;
import org.utils.ItemSelector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AplicationFlow {
    private List<User<?>> users;
    private List<Actor> actors;
    private List<Production> productions;
    private List<Request> requests;
    private List<MenuOptions> menuOptions;
    private int options = 0;

    public AplicationFlow() {
        users = IMDB.getInstance().getUsers();
        actors = IMDB.getInstance().getActors();
        productions = IMDB.getInstance().getProductions();
        requests = IMDB.getInstance().getRequests();
        menuOptions = new ArrayList<>();
    }

    public void Start() {
        LoginIn();
        MainMenu();
    }

    public void LoginIn() {
        do {
            System.out.print("   Email: ");
            String username = InputManager.ReadLine();

            System.out.print("   Password: ");
            String password = InputManager.ReadLine();

            Credentials credentials = new Credentials(username, password);

            // Find user with username and password
            findUser(credentials);
            if (IMDB.getInstance().getCurrentLoggedInUser() == null) {
                System.out.println("Invalid credentials, try again");
            }

        } while (!IMDB.getInstance().is_LoggedIn);
    }

    private void ExecuteMainMenuCommand(int choice) {
        MenuOptions command = menuOptions.get(choice - 1);
        switch (command) {
            case ViewProductionsDetails:
                ViewProductions();
                break;
            case ViewActorsDetails:
                ViewActors();
                break;
            case ViewNotifications:
                PrintNotifications();
                InputManager.WaitForInput();
                break;
            case Search:
                SearchForObject();
                break;
            case AddDeleteToFavorites:
                AddDeleteToFavorites();
                break;
            case Requests:
                break;
            case AddDeleteToSystem:
                AddDeleteToSystem();
                break;
            case UpdateActor:
                UpdateActor();
                break;
            case UpdateProduction:
                UpdateProduction();
                break;
            case AddDeleteUser:
                AddDeleteUser();
                break;
            case AddRating:
                AddRating(IMDB.getInstance().getProductions());
                break;
            case DeleteRating:
                DeleteRating(IMDB.getInstance().getProductions());
                break;
            case SeeMyRequest:
                SeeMyRequest();
                break;
            case AddRequest:
                AddRequest();
                break;
            case DeleteRequest:
                // DeleteRequest();
                break;
            default:
                break;
        }
    }

    private void SeeMyRequest() {
        // print all requests of contributor and regular
        System.out.println("Requests:");
        // regular make cast to it and get requests
        if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Regular<?>)
            for (Request request : ((Regular<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests()) {
                // for each request print it
                // print all information about request
                System.out.println(request.getUserUsername());
                System.out.println(request.getResolverUsername());
                System.out.println(request.getDescription());
                System.out.println(request.getCreationDate());

            }

        // contributor make cast to it and get requests
        else if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Contributor<?>)
            for (Request request : ((Contributor<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests()) {
                // for each request print it
                // print all information about request
                System.out.println(request.getUserUsername());
                System.out.println(request.getResolverUsername());
                System.out.println(request.getDescription());
                System.out.println(request.getCreationDate());
            }
    }

    private void AddRequest() {
        // Select type of request
        // print request types
        System.out.println("Request types:");
        for (int i = 0; i < RequestType.values().length; i++) {
            System.out.println((i + 1) + ". " + RequestType.values()[i]);
        }
        // print the number
        System.out.print("Request type: ");
        String input = InputManager.ReadLine();
        int requestType = InputManager.ToPositiveNumber(input);
        if (requestType < 1 || requestType > RequestType.values().length) {
            System.out.println("Invalid command");
            return;
        }

        // if request type is Movie or actor type
        // print all movies or actors
        if (requestType == 3) {
            System.out.println("Movies:");
            for (int i = 0; i < productions.size(); i++) {
                System.out.println((i + 1) + ". " + productions.get(i).getTitle());
            }
        } else if (requestType == 2) {
            System.out.println("Actors:");
            for (int i = 0; i < actors.size(); i++) {
                System.out.println((i + 1) + ". " + actors.get(i).getName());
            }
        }

        if (requestType == 3 || requestType == 2) {
            // Select movie or actor
            System.out.print("The number of the movie/actor to request: ");
            input = InputManager.ReadLine();
            int number = InputManager.ToPositiveNumber(input);
            if (number < 1 || number > productions.size()) {
                System.out.println("Invalid command");
                return;
            }

            RequestType type = RequestType.values()[requestType - 1];
            LocalDateTime date = LocalDateTime.now();
            String resolver_username = null;
            // find username which added contribution for each user get contribution and
            // check if contains it
            Staff<?> staff = null;
            for (User<?> user : users) {
                if (user instanceof Staff<?>) {
                    if (((Staff<?>) user).getContributions().contains(productions.get(number - 1))) {
                        resolver_username = user.getUsername();
                        staff = (Staff<?>) user;
                        break;
                    }
                }
            }

            // add request write comment
            System.out.print("Comment: ");
            String comment = InputManager.ReadLine();
            Request request = new Request(type, date, comment,
                    IMDB.getInstance().getCurrentLoggedInUser().getUsername(),
                    resolver_username);

            // add request to user
            IMDB.getInstance().getRequests().add(request);
            ((Regular<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests().add(request);
            if (staff != null) {
                // add request to staff if is admin add to admin if contributor add to
                // contributor
                staff.getAssignedRequests().add(request);

            }
            if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Contributor<?>)
                ((Contributor<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests().add(request);
            if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Regular<?>)
                ((Regular<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests().add(request);
        }

        if (requestType == 1 || requestType == 4) {
            // send to request to admin
            RequestType type = RequestType.values()[requestType - 1];
            LocalDateTime date = LocalDateTime.now();
            System.out.print("Comment: ");
            String comment = InputManager.ReadLine();
            Request request = new Request(type, date, comment,
                    IMDB.getInstance().getCurrentLoggedInUser().getUsername(),
                    "ADMIN");

            // add request to user
            IMDB.getInstance().getRequests().add(request);
            RequestsHolder.addRequest(request);

            if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Contributor<?>)
                ((Contributor<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests().add(request);
            if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Regular<?>)
                ((Regular<?>) IMDB.getInstance().getCurrentLoggedInUser()).getRequests().add(request);
        }

    }

    private void DeleteRating(List<Production> productions) {
        // Select production to delete rating
        PrintProductions(productions, new ProductionFilter());
        System.out.print("The number of the production to delete rating: ");
        String input = InputManager.ReadLine();
        int number = InputManager.ToPositiveNumber(input);

        // Select production to delete rating, if it contains rating from current user
        // then delete it
        try {
            if (number > 0 && number <= productions.size()) {
                Production production = productions.get(number - 1);
                // Delete rating
                if (production.getRatings().stream()
                        .anyMatch(r -> r.getUsername()
                                .equals(IMDB.getInstance().getCurrentLoggedInUser().getUsername()))) {
                    production.getRatings().removeIf(
                            r -> r.getUsername().equals(IMDB.getInstance().getCurrentLoggedInUser().getUsername()));
                } else
                    throw new InvalidCommandException("You have not rated this production");
            } else
                throw new InvalidCommandException("Invalid command");
        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
        }
    }

    private void AddRating(List<Production> productions) {
        // Select production to add rating
        PrintProductions(productions, new ProductionFilter());
        System.out.print("The number of the production to add rating: ");
        String input = InputManager.ReadLine();
        int number = InputManager.ToPositiveNumber(input);

        try {
            if (number > 0 && number <= productions.size()) {
                Production production = productions.get(number - 1);
                // Add rating
                System.out.print("Rating: ");
                input = InputManager.ReadLine();
                int rating = InputManager.ToPositiveNumber(input);
                if (rating < 1 || rating > 10) {
                    throw new InvalidCommandException("Invalid command");
                }

                // Add comment
                System.out.print("Comment: ");
                String comment = InputManager.ReadLine();

                // Add rating to production
                Rating newRating = new Rating(IMDB.getInstance().getCurrentLoggedInUser().getUsername(), rating,
                        comment);
                newRating.setUser(IMDB.getInstance().getCurrentLoggedInUser());
                production.addRating(
                        newRating);

            } else
                throw new InvalidCommandException("Invalid command");
        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
        }

    }

    private void PrintProductions(List<Production> productions, ProductionFilter filter) {
        System.out.print("Productions");
        if (filter.getSelectedGenres() != null) {
            System.out.print(" (Genre: ");
            for (int i = 0; i < filter.getSelectedGenres().size(); i++) {
                System.out.print(filter.getSelectedGenres().get(i));
                if (i != filter.getSelectedGenres().size() - 1)
                    System.out.print(", ");
            }
            System.out.print(")");
        }
        if (filter.isByNumberOfRatings())
            System.out.print(" (Sorted by number of reviews)");

        System.out.println(":");
        for (int i = 0; i < productions.size(); i++) {
            System.out.println((i + 1) + ". " + productions.get(i).getTitle());
        }
    }

    private void PrintAllGenres() {
        System.out.println("Genres:");
        for (int i = 0; i < Genre.values().length; i++) {
            System.out.println((i + 1) + ". " + Genre.values()[i]);
        }
    }

    private int SelectGenre() {
        int genreChoice = 0;
        String input = "";
        do {
            try {
                PrintAllGenres();

                input = InputManager.ReadLine();
                genreChoice = InputManager.ToPositiveNumber(input);

                if (genreChoice > 0 && genreChoice <= Genre.values().length) {
                    return genreChoice;
                } else
                    throw new InvalidCommandException("Invalid command");

            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    private int SelectFilter() {
        int filterChoice = 0;
        String input = "";
        do {
            try {
                System.out.println("Filter by:");
                System.out.println("1. Genre");
                System.out.println("2. Rating");
                System.out.println("3. Return to main menu");

                input = InputManager.ReadLine();
                filterChoice = InputManager.ToPositiveNumber(input);

                if (filterChoice == 1 || filterChoice == 2 || filterChoice == 3)
                    return filterChoice;
                else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    private void ViewProductions() {

        // Create a filter
        ProductionFilter filter = new ProductionFilter();
        List<Production> filteredProductions = null;
        do {
            String input = "";
            int choice = 0;
            // Sort productions after filter
            filteredProductions = filter.SortProductions(productions);

            // If there are no productions after filter, display a message
            PrintProductions(filteredProductions, filter);
            if (filteredProductions.size() == 0)
                System.out.println("No productions found");

            System.out.println(
                    "\n1. Select production     2. Add filter     3. Clear filter     4. Return to main menu");

            input = InputManager.ReadLine();
            choice = InputManager.ToPositiveNumber(input);
            try {
                if (choice == 1) {
                    ItemSelector.SelectItem(filteredProductions);
                } else if (choice == 2) {
                    int filterChoice = SelectFilter();
                    if (filterChoice == 1) {
                        int genreChoice = SelectGenre();
                        filter.addGenre(Genre.values()[genreChoice - 1]);
                    } else if (filterChoice == 2) {
                        filter.setByNumberOfRatings(true);
                    } else if (filterChoice == 3) {
                        filter.setSelectedGenres(null);
                        filter.setByNumberOfRatings(false);
                    }
                } else if (choice == 3) {
                    filter.setSelectedGenres(null);
                    filter.setByNumberOfRatings(false);
                } else if (choice == 4) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private void PrintActors() {
        System.out.println("Actors:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ". " + actors.get(i).getName());
        }
    }

    private void ViewActors() {
        actors.sort(Actor::compareTo);
        do {
            try {
                int choice = 0;
                String input = "";

                PrintActors();

                System.out.println("\n1. Select actor     2. Return to main menu");

                input = InputManager.ReadLine();
                choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    ItemSelector.SelectItem(actors);

                } else if (choice == 2) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private void PrintNotifications() {
        System.out.println("Notifications:");
        for (int i = 0; i < IMDB.getInstance().getCurrentLoggedInUser().getNotifications().size(); i++) {
            System.out.println((i + 1) + ". " + IMDB.getInstance().getCurrentLoggedInUser().getNotifications().get(i));
        }
    }

    private void ShowResults(List<Object> results) {
        System.out.println("Results:");

        for (int i = 0; i < results.size(); i++) {
            // If is actor
            if (results.get(i) instanceof Actor) {
                System.out
                        .println((i + 1) + ". " + ((Actor) results.get(i)).getName() + " (Actor)");
            } else if (results.get(i) instanceof Production) {
                // get class name and print it too
                System.out.println((i + 1) + ". " + ((Production) results.get(i)).getTitle()
                        + " ("
                        + results.get(i).getClass().getSimpleName() + ")");
            }
        }
    }

    private void SearchForObject() {
        List<Object> results = new ArrayList<>();
        boolean isSearchClear = true;
        String input = "";
        do {

            int choosenResult = 0;
            // Enter search input
            if (isSearchClear) {
                System.out.print("Search: ");

                input = InputManager.ReadLine();

                if (input != "") {
                    isSearchClear = false;
                    results = search(input);
                }
            }

            if (results.size() == 0) {
                System.out.println("No results found");
            } else {
                ShowResults(results);
            }

            if (results.size() != 0) {
                System.out.println("\n1. See details     2. Search again     3. Return to menu");
            } else {
                System.out.println("\n1. Search again     2. Return to menu");
            }

            try {
                input = InputManager.ReadLine();
                choosenResult = InputManager.ToPositiveNumber(input);

                if (choosenResult == 1) {
                    if (results.size() != 0) {
                        ItemSelector.SelectItem(results);
                    }
                } else if (choosenResult == 2) {
                    isSearchClear = true;
                } else if (choosenResult == 3) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    // Add movie
    private Production AddProduction(ProductionType type) {

        System.out.print("Title: ");
        String title = null;

        title = InputManager.ReadLine();

        List<String> directors = new ArrayList<>();
        do {
            System.out.print("Directors: ");
            // print also the list
            for (int i = 0; i < directors.size(); i++) {
                System.out.print(directors.get(i));
                if (i != directors.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();

            try {
                System.out.println("1. Add director     2. Cancel");
                String input = InputManager.ReadLine();
                int choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    System.out.print("Name: ");
                    String director = InputManager.ReadLine();
                    directors.add(director);
                } else if (choice == 2) {
                    if (directors.size() == 0)
                        throw new InvalidCommandException("At least one director is required");
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        List<String> actors = new ArrayList<>();

        do {
            System.out.print("Actors: ");
            for (int i = 0; i < actors.size(); i++) {
                System.out.print(actors.get(i));
                if (i != actors.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();

            try {
                System.out.println("1. Add actor     2. Cancel");
                String input = InputManager.ReadLine();
                int choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    System.out.print("Name: ");
                    String actor = InputManager.ReadLine();
                    actors.add(actor);

                    for (Actor a : this.actors) {
                        if (a.getName().equals(actor)) {
                            // add movie to actor filmography
                            a.addProductionToFilmography(actor, type);
                            break;
                        }
                    }
                } else if (choice == 2) {
                    if (actors.size() == 0)
                        throw new InvalidCommandException("At least one actor is required");
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        List<Genre> genres = new ArrayList<>();
        do {
            System.out.print("Genres: ");
            for (int i = 0; i < genres.size(); i++) {
                System.out.print(genres.get(i));
                if (i != genres.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();
            try {
                System.out.println("1. Add genre     2. Cancel");
                String input = InputManager.ReadLine();
                int choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    System.out.println("Genre: ");
                    input = InputManager.ReadLine();
                    Genre genre = Genre.valueOf(input);
                    genres.add(genre);
                } else if (choice == 2) {
                    if (genres.size() == 0)
                        throw new InvalidCommandException("At least one genre is required");
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        System.out.println("Description: ");
        String description = InputManager.ReadLine();

        // Create movie
        Production production = null;
        if (type == ProductionType.Movie)
            production = new Movie();
        else if (type == ProductionType.Series)
            production = new Series();

        production.setTitle(title);
        production.setDirectors(directors);
        production.setActors(actors);
        production.setGenres(genres);
        production.setDescription(description);
        production.setAverageRating(0.0);
        production.setRatings(new ArrayList<>());

        return production;
    }

    private void AddMovie() {
        Movie movie = (Movie) AddProduction(ProductionType.Movie);
        String duration = "";
        do {
            System.out.print("Duration: ");
            duration = InputManager.ReadLine();
        } while (!InputManager.isNumber(duration));

        String releaseYear = "";
        do {
            System.out.print("Release Year: ");
            releaseYear = InputManager.ReadLine();
        } while (!InputManager.isNumber(releaseYear));

        movie.setDuration(Integer.parseInt(duration));
        movie.setReleaseYear(Integer.parseInt(releaseYear));

        productions.add(movie);
        ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).addProductionSystem(movie);
    }

    private void AddSeries() {
        Series series = (Series) AddProduction(ProductionType.Series);
        String releaseYear = "";
        do {
            System.out.print("Release Year: ");
            releaseYear = InputManager.ReadLine();
        } while (!InputManager.isNumber(releaseYear));

        String numberOfSeasons = "";
        do {
            System.out.print("Number of seasons: ");
            numberOfSeasons = InputManager.ReadLine();
        } while (!InputManager.isNumber(numberOfSeasons));

        series.setNoSeasons(InputManager.ToPositiveNumber(numberOfSeasons));
        series.setReleaseYear(Integer.parseInt(releaseYear));

        // For each season add episodes
        for (int i = 0; i < series.getNoSeasons(); i++) {
            List<Episode> episodes = new ArrayList<>();
            String seasonName = "Season " + (i + 1);

            do {
                System.out.println("Season " + (i + 1) + ":");
                for (int j = 0; j < episodes.size(); j++) {
                    System.out.printf("\t\tEpisode: %-30s Duration: %d\n", episodes.get(j).getName(),
                            episodes.get(j).getDuration());
                }

                try {
                    System.out.println("1. Add episode     2. Cancel");
                    String input = InputManager.ReadLine();
                    int choice = InputManager.ToPositiveNumber(input);

                    if (choice == 1) {
                        String episodeName = "";

                        System.out.print("Name: ");
                        episodeName = InputManager.ReadLine();

                        String duration = "";
                        do {
                            System.out.print("Duration: ");
                            duration = InputManager.ReadLine();
                        } while (!InputManager.isNumber(duration));

                        episodes.add(new Episode(episodeName, Integer.parseInt(duration)));
                    } else if (choice == 2) {
                        if (episodes.size() == 0)
                            throw new InvalidCommandException("At least one episode is required");
                        break;
                    } else
                        throw new InvalidCommandException("Invalid command");
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            } while (true);

            series.addSeason(seasonName, episodes);
        }

        ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).addProductionSystem(series);

    }

    // Add actor
    private void AddActor() {
        System.out.print("Name: ");
        String name = InputManager.ReadLine();
        System.out.print("Desciption: ");
        String description = InputManager.ReadLine();

        List<Filmography> filmography = new ArrayList<>();
        do {
            try {
                // add new production untill cancel
                System.out.println("1. Add production     2. Cancel");
                String input = InputManager.ReadLine();
                int choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    String title = null;
                    ProductionType type = null;
                    // read input
                    do {
                        try {
                            System.out.println("Production type: ");
                            input = InputManager.ReadLine();

                            if (input.equals("Movie"))
                                type = ProductionType.Movie;
                            else if (input.equals("Series"))
                                type = ProductionType.Series;
                            else
                                throw new InvalidCommandException("Invalid command");
                            break;
                        } catch (InvalidCommandException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (true);

                    System.out.print("Title: ");

                    title = InputManager.ReadLine();

                    filmography.add(new Filmography(title, type));

                } else if (choice == 2) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);

        Actor actor = new Actor(name, description, filmography);
        ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).addActorSystem(actor);

    }

    // Add/Delete actor/movie/series to/from favorites
    private void AddDeleteToFavorites() {
        do {
            int choice = 0;
            String input = "";

            // Convet SortedSet to List
            List<Object> favorites = new ArrayList<>(IMDB.getInstance().getCurrentLoggedInUser().getFavorites());

            // Print favorites
            System.out.println("Favorites:");
            IMDB.getInstance().getCurrentLoggedInUser().PrintFavorites();

            System.out.println(
                    "\n1. Select actor/prodution     2. Add actor/production     3. Delete actor/production     4. Return to main menu");

            try {
                input = InputManager.ReadLine();
                choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    ItemSelector.SelectItem(favorites);
                }
                // Add actor/production
                else if (choice == 2) {
                    // Add actor / production
                    System.out.print("Actor/Production to add: ");
                    input = InputManager.ReadLine();

                    Object itemTOFInd = findActorAndProdcutions(input, actors, productions);
                    if (itemTOFInd != null) {
                        if (itemTOFInd instanceof Actor) {
                            Actor actor = (Actor) itemTOFInd;
                            IMDB.getInstance().getCurrentLoggedInUser().addActorToFavorites(actor);
                        } else if (itemTOFInd instanceof Production) {
                            Production production = (Production) itemTOFInd;
                            IMDB.getInstance().getCurrentLoggedInUser().addProductionToFavorites(production);
                        }
                    } else
                        throw new InvalidCommandException("Invalid command");

                } else if (choice == 3) {
                    // Delete actor/production
                    System.out.print("The number of the actor/production to delete: ");
                    input = InputManager.ReadLine();
                    int number = InputManager.ToPositiveNumber(input);

                    if (number > 0 && number <= favorites.size()) {
                        if (favorites.get(number - 1) instanceof Production) {
                            Production production = (Production) favorites.get(number - 1);
                            IMDB.getInstance().getCurrentLoggedInUser().removeProductionFromFavorites(production);
                        } else if (favorites.get(number - 1) instanceof Actor) {
                            Actor actor = (Actor) favorites.get(number - 1);
                            IMDB.getInstance().getCurrentLoggedInUser().removeActorFromFavorites(actor);
                        }
                    } else
                        throw new InvalidCommandException("Invalid command");

                } else if (choice == 4) {
                    break;
                } else if (!(choice > 0 && choice <= 4))
                    throw new InvalidCommandException("Invalid command");

            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private void AddToSystem() {
        do {
            int choice = 0;
            String input = "";

            System.out.println("1. Actor   2. Movie   3. Series    4. Return to previous menu");

            try {
                input = InputManager.ReadLine();
                choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    AddActor();
                } else if (choice == 2) {
                    AddMovie();
                } else if (choice == 3) {
                    AddSeries();
                } else if (choice == 4) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    private void DeleteToSystem() {
        String input = "";

        System.out.println("Contributions:");
        ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).printContributions();
        // delete actor/production
        System.out.print("The name of the actor/production to delete: ");

        input = InputManager.ReadLineWithoutNullException();

        // find actor/production
        try {
            Object itemTOFInd = findActorAndProdcutions(input, actors, productions);
            // System.out.println(itemTOFInd);
            if (itemTOFInd != null) {
                if (itemTOFInd instanceof Actor) {
                    Actor actor = (Actor) itemTOFInd;
                    ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).removeActorSystem(actor.getName());
                    // Also for each production in actor filmography remove actor
                    for (Production production : productions) {
                        if (production.getActors().contains(actor.getName()))
                            production.removeActor(input);
                    }

                    // remove actor from actors list
                    actors.remove(actor);
                } else if (itemTOFInd instanceof Production) {
                    Production production = (Production) itemTOFInd;
                    ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser())
                            .removeProductionSystem(production.getTitle());
                    // Also for each actor in production filmography remove production
                    ProductionType type = null;
                    if (production instanceof Movie)
                        type = ProductionType.Movie;
                    else if (production instanceof Series)
                        type = ProductionType.Series;
                    for (Actor actor : actors) {
                        actor.removeProductionFromFilmography(production.getTitle(), type);
                    }

                    // remove production from productions list
                    productions.remove(production);
                }
            } else {
                throw new InvalidCommandException("Item not found");
            }
        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage());
        }
    }

    private void AddDeleteToSystem() {
        do {
            int choice = 0;
            String input = "";

            System.out.println("1. Add actor/production     2. Delete actor/production     3. Return to main menu");

            try {
                input = InputManager.ReadLine();
                choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    AddToSystem();
                } else if (choice == 2) {
                    // Delete actor/production
                    DeleteToSystem();

                } else if (choice == 3) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");

            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    private void UpdateActor() {
        do {
            // int choice = 0;
            String input = "";

            // print all user contributions
            System.out.println("Contributions:");
            ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).printContributions();
            System.out.println(((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).getContributions().size());

            System.out.println("Name of the actor to update (q to exit): ");
            input = InputManager.ReadLine();
            if (input.equals("q")) {
                break;
            }

            // find actor in
            Actor actor = ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).findActorInContributions(input);

            if (actor != null) {
                ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).updateActor(actor);
            }

        } while (true);
    }

    private void UpdateProduction() {
        do {
            String input = "";
            System.out.println("Contributions:");
            ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).printContributions();
            System.out.println(((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).getContributions().size());

            System.out.println("Name of the production to update (q to exit): ");
            input = InputManager.ReadLine();
            if (input.equals("q")) {
                break;
            }

            Production production = ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser())
                    .findProductionInContributions(input);

            if (production != null) {
                ((Staff<?>) IMDB.getInstance().getCurrentLoggedInUser()).updateProduction(production);
            }

        } while (true);
    }

    private void AddDeleteUser() {
        do {
            int choice = 0;
            String input = "";

            System.out.println("1. Add user     2. Delete user     3. Return to main menu");

            try {
                input = InputManager.ReadLine();
                choice = InputManager.ToPositiveNumber(input);

                if (choice == 1) {
                    // Add user
                    System.out.println("1. Regular     2. Contributor     3. Admin");
                    input = InputManager.ReadLine();
                    choice = InputManager.ToPositiveNumber(input);
                    User<?> user = null;
                    if (choice == 1)
                        user = UserFactory.createUser(AccountType.Regular);
                    else if (choice == 2)
                        user = UserFactory.createUser(AccountType.Contributor);
                    else if (choice == 3)
                        user = UserFactory.createUser(AccountType.Admin);
                    else
                        throw new InvalidCommandException("Invalid command");

                    users.add(user);

                    // Set username
                    System.out.print("Username: ");
                    input = InputManager.ReadLine();

                    // Set email
                    System.out.print("Email: ");
                    String email = InputManager.ReadLine();

                    // Generate good random password
                    String password = GeneratePassword.generateSecurePassword();
                    System.out.println("Password: " + password);

                    Credentials credentials = new Credentials(email, password);

                    // Set name
                    System.out.print("Name: ");
                    String name = InputManager.ReadLine();

                    // set country
                    System.out.print("Country: ");
                    String country = InputManager.ReadLine();
                    // set age
                    System.out.print("Age: ");
                    int age = InputManager.ReadAPositiveNumber();
                    System.out.print("Gender: ");
                    String gender = InputManager.ReadLine();

                    // Set birthdate
                    String birthdate = "";
                    do {
                        System.out.print("Birthdate (yyyy-mm-dd): ");
                        birthdate = InputManager.ReadLine();
                    } while (!InputManager.IsDate(birthdate));
                    LocalDateTime dateOfBirth = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            .atStartOfDay();

                    user.setUsername(input);
                    user.setExperience(0);
                    user.setInformation(new User.Information.InformationBuilder()
                            .credentials(credentials)
                            .name(name)
                            .country(country)
                            .age(age)
                            .gender(gender)
                            .dateOfBirth(dateOfBirth)
                            .build());

                } else if (choice == 2) {
                    // Delete user
                    // pRINT ALL USERS
                    System.out.println("Users:");
                    for (int i = 0; i < users.size(); i++) {
                        System.out.println((i + 1) + ". " + users.get(i).getEmail());
                    }
                    System.out.print("Email of the user to delete: ");
                    input = InputManager.ReadLine();

                    // find user
                    User<?> user = null;
                    for (User<?> u : users) {
                        if (u.getEmail().equals(input)) {
                            user = u;
                            break;
                        }
                    }

                    if (user != null) {
                        if (user instanceof Staff<?>) {
                            Staff<?> staff = (Staff<?>) user;
                            for (Object item : staff.getContributions()) {
                                if (item instanceof Actor) {
                                    Actor actor = (Actor) item;
                                    for (User<?> u : users) {
                                        if (u.getAccountType() == AccountType.Admin) {
                                            ((Staff<?>) u).addActorSystem(actor);
                                        }
                                    }
                                } else if (item instanceof Production) {
                                    Production production = (Production) item;
                                    for (User<?> u : users) {
                                        if (u.getAccountType() == AccountType.Admin) {
                                            ((Staff<?>) u).addProductionSystem(production);
                                        }
                                    }
                                }
                            }
                        }

                        users.remove(user);
                    } else
                        throw new InvalidCommandException("User not found");

                } else if (choice == 3) {
                    break;
                } else
                    throw new InvalidCommandException("Invalid command");

            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }

        } while (true);
    }

    private void MainMenu() {
        do {
            int choice = 0;
            options = 0;
            String input = "";

            System.out.println("\nMenu:");
            System.out.println(
                    "1. View productions details\n2. View actors details\n3. View notifications\n4. Search for actor/movie/series\n5. Add/Delete actor/movie/series to/from favorites\n6. Requests");
            options += 6;

            menuOptions.add(MenuOptions.ViewProductionsDetails);
            menuOptions.add(MenuOptions.ViewActorsDetails);
            menuOptions.add(MenuOptions.ViewNotifications);
            menuOptions.add(MenuOptions.Search);
            menuOptions.add(MenuOptions.AddDeleteToFavorites);
            menuOptions.add(MenuOptions.Requests);

            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor
                    || IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Admin) {

                System.out.println(++options + ". Add/Delete actor/movie/series to/from system");
                menuOptions.add(MenuOptions.AddDeleteToSystem);
                System.out.println(++options + ". Update actor");
                menuOptions.add(MenuOptions.UpdateActor);
                System.out.println(++options + ". Update production");
                menuOptions.add(MenuOptions.UpdateProduction);

                if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Admin) {
                    System.out.println(++options + ". Add/Delete user");
                    menuOptions.add(MenuOptions.AddDeleteUser);
                }
            }

            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular) {
                System.out.println(++options + ". Add rating");
                menuOptions.add(MenuOptions.AddRating);
                System.out.println(++options + ". Delete rating");
                menuOptions.add(MenuOptions.DeleteRating);

            }

            // if regular or contributor possibility to add request
            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular
                    || IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
                // ADD SEE REQUESTS
                System.out.println(++options + ". See requests");
                menuOptions.add(MenuOptions.SeeMyRequest);
                System.out.println(++options + ". Add request");
                menuOptions.add(MenuOptions.AddRequest);
                // also delete request
                System.out.println(++options + ". Delete request");
                menuOptions.add(MenuOptions.DeleteRequest);

            }

            /*********** Get input ************/
            try {
                input = InputManager.ReadLine();
                // transform input to number
                choice = InputManager.ToPositiveNumber(input);
                // Execute menu command
                if (choice > 0 && choice <= options)
                    ExecuteMainMenuCommand(choice);
                else
                    throw new InvalidCommandException("Invalid command");

            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                continue;
            }

        } while (true);
    }

    public static void findUser(Credentials credentials) {
        for (User<?> u : IMDB.getInstance().getUsers()) {
            if (u.verifyCredentials(credentials)) {
                IMDB.getInstance().setCurrentLoggedInUser(u);
                IMDB.getInstance().is_LoggedIn = true;
                break;
            }
        }
    }

    // Search for actor/movie/series
    public static List<Object> search(String input) {
        List<Object> results = new ArrayList<>();

        // lowercase input
        input = input.toLowerCase();

        // Search in actors
        for (Actor actor : IMDB.getInstance().getActors()) {
            if (actor.getName().toLowerCase().startsWith(input)) {
                results.add(actor);
            }
        }

        // Search in productions
        for (Production production : IMDB.getInstance().getProductions()) {
            if (production.getTitle().toLowerCase().startsWith(input)) {
                results.add(production);
            }
        }

        return results;
    }

    private static Object findActorAndProdcutions(String input, List<Actor> actors,
            List<Production> productions) {
        // Search in actors
        Actor actor = findActor(actors, input);
        if (actor != null)
            return actor;

        // Search in productions
        Production production = findProduction(productions, input);
        if (production != null)
            return production;

        return null;
    }

    private static Actor findActor(List<Actor> actors, String input) {
        // Search in actors
        for (Actor actor : actors) {
            // if actor name begins with input
            if (actor.getName().equals(input)) {
                return actor;
            }
        }

        return null;
    }

    public static Production findProduction(List<Production> productions, String input) {
        // Search in productions
        for (Production production : productions) {
            if (production.getTitle().equals(input)) {
                return production;
            }
        }

        return null;
    }

}
