package org.imdb;

import java.util.List;
import java.util.Map;

import org.actor.*;
import org.exceptions.InvalidCommandException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.production.*;
import org.terminal.AplicationFlow;
import org.user.*;
import org.request.*;
import org.gui.LoginForm;
import org.gui.GUIAppFlow;

import lombok.Setter;
import lombok.Getter;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.*;

import java.lang.Exception;
import java.lang.RuntimeException;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

@Getter
@Setter
public class IMDB {
    private static IMDB instance;
    private List<User<?>> users;
    private List<Actor> actors;
    private List<Production> productions;
    private List<Request> requests;
    private User<?> currentLoggedInUser = null;

    private List<Actor> newCreatedActors = new ArrayList<>();

    public boolean is_LoggedIn = false;

    private IMDB() {
        this.users = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.productions = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    static {
        try {
            instance = new IMDB();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static IMDB getInstance() {
        return instance;
    }

    private void readActors(JSONParser parser) {
        try {
            Path path = Paths.get("src", "main", "resources", "input", "actors.json");
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONArray actors = (JSONArray) obj;
            for (Object actor : actors) {
                JSONObject actorObject = (JSONObject) actor;
                String name = (String) actorObject.get("name");
                String description = (String) actorObject.get("biography");

                List<Filmography> filmography = new ArrayList<>();
                JSONArray filmographyArray = (JSONArray) actorObject.get("performances");
                for (Object film : filmographyArray) {
                    JSONObject filmObject = (JSONObject) film;
                    String filmName = (String) filmObject.get("title");
                    String filmType = (String) filmObject.get("type");
                    ProductionType type = ProductionType.valueOf(filmType);
                    filmography.add(new Filmography(filmName, type));
                }

                Actor actorToAdd = new Actor(name, description, filmography);
                this.actors.add(actorToAdd);
            }
        } catch (FileNotFoundException e) {
            System.out.println("actors.json not found");
        } catch (IOException e) {
            System.out.println("IOException occurred");
        } catch (ParseException e) {
            System.out.println("ParseException occurred");
        }

    }

    private void readProduction(JSONParser parser) {
        // Read production.json
        try {
            Path path = Paths.get("src", "main", "resources", "input", "production.json");
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONArray productions = (JSONArray) obj;
            for (Object production : productions) {
                JSONObject productionObject = (JSONObject) production;
                String title = (String) productionObject.get("title");
                ProductionType type = ProductionType.valueOf((String) productionObject.get("type"));
                JSONArray directorsArray = (JSONArray) productionObject.get("directors");
                List<String> directors = new ArrayList<>();
                for (Object director : directorsArray) {
                    directors.add((String) director);
                }
                JSONArray actorsArray = (JSONArray) productionObject.get("actors");
                List<String> actors = new ArrayList<>();
                for (Object actor : actorsArray) {
                    actors.add((String) actor);
                    boolean actorExists = false;
                    for (Actor actorToAdd : this.actors) {
                        if (actorToAdd.getName().equals((String) actor)) {
                            actorExists = true;
                            break;
                        }
                    }
                    if (!actorExists) {
                        List<Filmography> filmography = new ArrayList<>();
                        filmography.add(new Filmography(title, type));
                        Actor actorToAdd = new Actor((String) actor, "", filmography);
                        this.actors.add(actorToAdd);
                        newCreatedActors.add(actorToAdd);
                    }
                }
                JSONArray genresArray = (JSONArray) productionObject.get("genres");
                List<Genre> genres = new ArrayList<>();
                for (Object genre : genresArray) {
                    genres.add(Genre.valueOf((String) genre));
                }
                JSONArray ratingsArray = (JSONArray) productionObject.get("ratings");
                List<Rating> ratings = new ArrayList<>();
                for (Object rating : ratingsArray) {
                    JSONObject ratingObject = (JSONObject) rating;
                    String username = (String) ratingObject.get("username");
                    int ratingValue = (int) (long) ratingObject.get("rating");
                    String comment = (String) ratingObject.get("comment");
                    Rating ratingToAdd = new Rating(username, ratingValue, comment);

                    ratings.add(ratingToAdd);

                }

                String description = (String) productionObject.get("plot");
                double averageRating = (double) productionObject.get("averageRating");

                Production productionToAdd = null;

                if (type == ProductionType.Movie) {
                    String duration = (String) productionObject.get("duration");
                    int durationInMinutes = Integer.parseInt(duration.replaceAll("[^0-9]", ""));
                    int realeaseYear = -1;
                    if (productionObject.get("releaseYear") != null)
                        realeaseYear = (int) (long) productionObject.get("releaseYear");

                    Movie movieToAdd = new Movie();
                    productionToAdd = movieToAdd;
                    movieToAdd.setDuration(durationInMinutes);
                    movieToAdd.setReleaseYear(realeaseYear);

                    this.productions.add(movieToAdd);
                } else if (type == ProductionType.Series) {
                    Series seriesToAdd = new Series();
                    int realeaseYear = -1;
                    Map<String, List<Episode>> episodes = new LinkedHashMap<>();
                    if (productionObject.get("releaseYear") != null)
                        realeaseYear = (int) (long) productionObject.get("releaseYear");
                    int numOfSeasons = (int) (long) productionObject.get("numSeasons");
                    JSONObject seasons = (JSONObject) productionObject.get("seasons");

                    for (int i = 0; i < numOfSeasons; i++) {
                        JSONArray episodesArray = (JSONArray) seasons.get("Season " + (i + 1));

                        List<Episode> seasonEpisodes = new ArrayList<>();
                        for (Object episode : episodesArray) {
                            JSONObject episodeObject = (JSONObject) episode;
                            String episodeName = (String) episodeObject.get("episodeName");
                            String episodeDuration = (String) episodeObject.get("duration");
                            int episodeDurationInMinutes = Integer
                                    .parseInt(episodeDuration.replaceAll("[^0-9]", ""));
                            seasonEpisodes.add(new Episode(episodeName, episodeDurationInMinutes));
                        }

                        episodes.put("Season " + (i + 1), seasonEpisodes);

                        seriesToAdd.setReleaseYear(realeaseYear);
                        seriesToAdd.setNoSeasons(numOfSeasons);
                        seriesToAdd.setEpisodes(episodes);
                    }

                    productionToAdd = seriesToAdd;
                    this.productions.add(seriesToAdd);

                }

                productionToAdd.setTitle(title);
                productionToAdd.setDirectors(directors);
                productionToAdd.setActors(actors);
                productionToAdd.setGenres(genres);
                productionToAdd.setRatings(ratings);
                productionToAdd.setDescription(description);
                productionToAdd.setAverageRating(averageRating);
                productionToAdd.recalculateAverageRating();

                if (productionObject.get("poster") != null)
                    productionToAdd.setPoster((String) productionObject.get("poster"));
                else
                    productionToAdd.setPoster("NoImage.png");

            }
        } catch (FileNotFoundException e) {
            System.out.println("productions.json not found");
        } catch (IOException e) {
            System.out.println("IOException occurred");
        } catch (ParseException e) {
            System.out.println("ParseException occurred");
        }
    }

    private void readAccounts(JSONParser parser) {
        // Read accounts.json
        try {
            Path path = Paths.get("src", "main", "resources", "input", "accounts.json");
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONArray accounts = (JSONArray) obj;
            for (Object account : accounts) {
                JSONObject accountObject = (JSONObject) account;

                String role = (String) accountObject.get("userType");
                AccountType accountType = AccountType.valueOf(role);

                User<?> user = UserFactory.createUser(accountType);

                user.setUsername((String) accountObject.get("username"));
                if (accountObject.get("experience") != null)
                    user.setExperience(Integer.parseInt(((String) accountObject.get("experience"))));
                else
                    user.setExperience(Integer.MAX_VALUE);

                JSONObject informationObject = (JSONObject) accountObject.get("information");
                JSONObject credentialsObject = (JSONObject) informationObject.get("credentials");

                Credentials credentials = new Credentials((String) credentialsObject.get("email"),
                        (String) credentialsObject.get("password"));
                String dateString = (String) informationObject.get("birthDate");
                LocalDateTime dateOfBirth = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        .atStartOfDay();

                user.setInformation(new User.Information.InformationBuilder()
                        .credentials(credentials)
                        .name((String) informationObject.get("name"))
                        .country((String) informationObject.get("country"))
                        .age((int) (long) informationObject.get("age"))
                        .gender((String) informationObject.get("gender"))
                        .dateOfBirth(dateOfBirth)
                        .build());

                // Add favorites
                JSONArray favoriteProductions = (JSONArray) accountObject.get("favoriteProductions");
                if (favoriteProductions != null)
                    for (Object production : favoriteProductions) {
                        String productionName = (String) production;
                        // find production in productions list
                        for (Production productionToAdd : productions) {
                            if (productionToAdd.getTitle().equals(productionName)) {
                                user.addProductionToFavorites(productionToAdd);
                            }
                        }
                    }
                JSONArray favoriteActors = (JSONArray) accountObject.get("favoriteActors");
                if (favoriteActors != null)
                    for (Object actor : favoriteActors) {
                        String actorName = (String) actor;
                        // find actor in actors list
                        for (Actor actorToAdd : actors) {
                            if (actorToAdd.getName().equals(actorName)) {
                                user.addActorToFavorites(actorToAdd);

                            }
                        }
                    }
                // if user is regular
                if (accountType == AccountType.Regular) {
                    for (Production production : productions) {
                        List<Rating> ratings = production.getRatings();
                        for (Rating rating : ratings) {
                            if (rating.getUsername().equals(user.getUsername())) {
                                production.getUsersWhoRatedAtLeastOnce().add(user);
                            }
                        }
                    }

                }

                for (Production production : productions) {
                    List<Rating> ratings = production.getRatings();
                    for (Rating rating : ratings) {
                        if (rating.getUsername().equals(user.getUsername())) {
                            rating.register(user);
                            rating.setUser(user);
                        }
                    }
                }

                if (accountType == AccountType.Admin) {
                    Admin<?> admin = (Admin<?>) user;
                    for (Actor actor : newCreatedActors) {
                        admin.addActorToContributions(actor);
                    }
                }

                // Contributor || Admin
                if (accountType == AccountType.Contributor || accountType == AccountType.Admin) {
                    Staff<?> staff = (Staff<?>) user;
                    JSONArray productionContribution = (JSONArray) accountObject.get("productionsContribution");
                    if (productionContribution != null)
                        for (Object production : productionContribution) {
                            String productionName = (String) production;
                            // find production in productions list
                            for (Production productionToAdd : productions) {
                                // System.out.println(productionToAdd.getTitle());
                                if (productionToAdd.getTitle().equals(productionName)) {
                                    staff.addProductionToContributions(productionToAdd);
                                }
                            }
                        }
                    JSONArray actorContribution = (JSONArray) accountObject.get("actorsContribution");
                    if (actorContribution != null)
                        for (Object actor : actorContribution) {
                            String actorName = (String) actor;
                            // find actor in actors list
                            for (Actor actorToAdd : actors) {
                                if (actorToAdd.getName().equals(actorName)) {
                                    staff.addActorToContributions(actorToAdd);

                                }
                            }
                        }
                }
                
                // Get notifications
                List<String> notificationsList = new ArrayList<>();
                JSONArray notifications = (JSONArray) accountObject.get("notifications");
                if (notifications != null)
                    for (Object notification : notifications) {
                        String notificationString = (String) notification;
                        notificationsList.add(notificationString);
                    }

                user.setNotifications(notificationsList);
                user.setAccountType(accountType);

                users.add(user);
            }
        } catch (

        FileNotFoundException e) {
            System.out.println("accounts.json not found");
        } catch (IOException e) {
            System.out.println("IOException occurred");
        } catch (ParseException e) {
            System.out.println("ParseException occurred");
        }
    }

    private void readRequests(JSONParser parser) {
        // Read requests.json
        try {
            Path path = Paths.get("src", "main", "resources", "input", "requests.json");
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONArray requests = (JSONArray) obj;
            for (Object request : requests) {
                JSONObject requestObject = (JSONObject) request;
                RequestType type = RequestType.valueOf((String) requestObject.get("type"));
                String dateString = (String) requestObject.get("createdDate");
                LocalDateTime creationDate = LocalDateTime.parse(dateString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                String username = (String) requestObject.get("username");
                String resolverUsername = (String) requestObject.get("to");
                String description = (String) requestObject.get("description");

                Request requestToAdd = new Request(type, creationDate, description, username, resolverUsername);

                if (resolverUsername.equals("ADMIN")) {
                    RequestsHolder.addRequest(requestToAdd);
                }
                for (User<?> user : users) {
                    if (user.getUsername().equals(username)) {
                        if (user.getAccountType() == AccountType.Regular)
                            ((Regular<?>) user).createRequest(requestToAdd);
                        else if (user.getAccountType() == AccountType.Contributor)
                            ((Contributor<?>) user).createRequest(requestToAdd);
                        requestToAdd.register(user);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("requests.json not found");
        } catch (IOException e) {
            System.out.println("IOException occurred");
        } catch (ParseException e) {
            System.out.println("ParseException occurred");
        }
    }

    private void readJSON() {
        // Load data from JSON files
        JSONParser parser = new JSONParser();
        // Read actors.json
        readActors(parser);

        // Read production.json
        readProduction(parser);

        // Read accounts.json
        readAccounts(parser);

        // Read requests.json
        readRequests(parser);

    }

    public void run() {
        // Load data from JSON files
        readJSON();

        // Sort productions by title
        productions.sort(Production::compareTo);
        // sort actors by name
        actors.sort(Actor::compareTo);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int interfaceChoice = 0;
        String input = null;

        boolean is_Done = false;
        do {
            System.out.println("Choose the way you want to use the application (terminal  or graphic)");
            System.out.println("   1. Terminal");
            System.out.println("   2. Graphic");
            try {
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    System.out.println("IOException occurred");
                }

                if (input != null)
                    // Check if the input is a number
                    if (input.matches("[0-9]"))
                        interfaceChoice = Integer.parseInt(input);
                    else
                        throw new InvalidCommandException("Invalid command");

                if (interfaceChoice != 1 && interfaceChoice != 2) {
                    throw new InvalidCommandException("Invalid command");
                }
                is_Done = true;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (!is_Done);

        if (interfaceChoice == 1) {
            // Terminal
            System.out.println("You chose terminal");
            AplicationFlow aplicationFlow = new AplicationFlow();
            aplicationFlow.Start();

        } else if (interfaceChoice == 2)

        {
            JDialog.setDefaultLookAndFeelDecorated(true);

            FlatMacDarkLaf.setup();

            System.out.println("You chose graphic");

            // Create login form
            while (true) {
                LoginForm form = new LoginForm();

                // stop the execution until the user logs in
                while (!is_LoggedIn) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException occurred");
                    }
                }

                // sleep for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException occurred");
                }

                form.dispose();

                System.out.println(currentLoggedInUser.getAccountType());
                new GUIAppFlow();

                while (is_LoggedIn) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException occurred");
                    }
                }
            }
        }
    }

    public void findUser(Credentials credentials) {
        for (User<?> u : users) {
            if (u.verifyCredentials(credentials)) {
                IMDB.getInstance().setCurrentLoggedInUser(u);
                is_LoggedIn = true;
                break;
            }
        }
    }

    // Searach for actor/movie/series
    public List<Object> search(String input) {
        List<Object> results = new ArrayList<>();

        // lowercase input
        input = input.toLowerCase();

        // Search in actors
        for (Actor actor : actors) {
            // if actor name begins with input
            if (actor.getName().toLowerCase().startsWith(input)) {
                results.add(actor);
            }
        }

        // Search in productions
        for (Production production : productions) {
            if (production.getTitle().toLowerCase().startsWith(input)) {
                results.add(production);
            }
        }

        return results;
    }
}
