package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.user.*;
import org.imdb.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;

import org.production.*;

import java.util.ArrayList;

import java.util.Map;
import org.gui.buttons.*;

public class ProductionDetailsPage extends JPanel {
    List<JComponent> episodesPane = new ArrayList<JComponent>();

    public JButton backButton = new JButton();

    int y = 0;

    public ProductionDetailsPage(Production production) {
        setLayout(new GridBagLayout());
        if (production instanceof Series) {
            y = 30;
        } else {
            y = 100;
        }

        JLabel image = GUIAppFlow.SimplyImage("posters/" + production.getPoster(), -1, -1, 400, 600);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(50, 50, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(image, c);

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "back.png");
        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);
        backButton.setIcon(new ImageIcon(scaledImage));

        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        c.insets = new Insets(25, 1100, 0, 0);
        add(backButton, c);

        User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
        boolean isActorInFavorites = false;
        for (Object favorite : user.getFavorites()) {

            if (favorite instanceof Production) {
                Production favoriteProduction = (Production) favorite;
                if (favoriteProduction.getTitle().equals(production.getTitle())) {

                    isActorInFavorites = true;
                    break;
                }
            }
        }

        FavoritesStarButton favoritesStarButton = new FavoritesStarButton(isActorInFavorites, production);
        c.insets = new Insets(25, 1045, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(favoritesStarButton, c);

        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular) {
            CommentButton commentButton = new CommentButton(production);
            c.insets = new Insets(25, 990, 0, 0);
            c.anchor = GridBagConstraints.NORTHWEST;
            add(commentButton, c);
        }

        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular
                || IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
            boolean isInContributions = false;
            int x = 935;
            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
                x = 990;
                Contributor<?> contributor = (Contributor<?>) IMDB.getInstance().getCurrentLoggedInUser();
                if (contributor.getContributions().contains(production)) {
                    isInContributions = true;
                }

            }
            if (!isInContributions) {
                ReportErrorButton reportErrorButton = new ReportErrorButton(production);
                c.insets = new Insets(25, x, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(reportErrorButton, c);
            }
        }

        if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Staff) {
            Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
            if (staff.getContributions().contains(production)) {

                UpdateButton updateButton = new UpdateButton(production);
                c.insets = new Insets(25, 990, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(updateButton, c);

                RemoveContributionButton removeContributionButton = new RemoveContributionButton(production);
                c.insets = new Insets(25, 935, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(removeContributionButton, c);

            }

        }

        String title = production.getTitle();
        int titleWidth = 640;
        if (production instanceof Series)
            titleWidth = 400;
        JTextPane titlePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, title, titleWidth, 100,
                new Font("Fira Code", Font.BOLD, 24));
        c.insets = new Insets(y, 542, 0, 0);
        add(titlePane, c);
        y += 60;

        String actors = "Actors: ";
        List<String> actorsList = production.getActors();

        for (int i = 0; i < actorsList.size(); i++) {
            if (i == actorsList.size() - 1) {
                actors += actorsList.get(i);
            } else {
                actors += actorsList.get(i) + ", ";
            }
        }

        JTextPane actorsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, actors, 600, 100,
                new Font("Fira Code", Font.PLAIN, 20));

        c.insets = new Insets(y, 500, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(actorsPane, c);
        y += 25;

        String directors = "Directors: ";
        List<String> directorsList = production.getDirectors();

        for (int i = 0; i < directorsList.size(); i++) {
            if (i == directorsList.size() - 1) {
                directors += directorsList.get(i);
            } else {
                directors += directorsList.get(i) + ", ";
            }
        }

        JTextPane directorsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, directors,
                600, 100,
                new Font("Fira Code", Font.PLAIN, 20));
        c.insets = new Insets(y, 500, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(directorsPane, c);
        y += 25;

        String genres = "Genres: ";
        List<Genre> genresList = production.getGenres();

        for (int i = 0; i < genresList.size(); i++) {
            if (i == genresList.size() - 1) {
                genres += genresList.get(i);
            } else {
                genres += genresList.get(i) + ", ";
            }
        }

        JTextPane genresPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, genres, 600,
                100,
                new Font("Fira Code", Font.PLAIN, 20));

        c.insets = new Insets(y, 500, 0, 0);
        add(genresPane, c);
        y += 25;

        String averageRating = "Average rating: " + production.getAverageRating();
        JTextPane averageRatingPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, averageRating, 600,
                100,
                new Font("Fira Code", Font.PLAIN, 20));
        c.insets = new Insets(y, 500, 0, 0);
        add(averageRatingPane, c);
        y += 25;

        if (production instanceof Movie) {
            Movie movie = (Movie) production;

            int releaseDate = movie.getReleaseYear();
            String releaseDateString = "Release date: " + releaseDate;
            JTextPane releaseDatePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                    releaseDateString, 600, 100,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(releaseDatePane, c);
            y += 25;

            int duration = movie.getDuration();
            String durationString = "Duration: " + duration;
            JTextPane durationPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                    durationString, 600, 100,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(durationPane, c);
            y += 50;

            String description = movie.getDescription();

            int height = getContentHeight(600, description);

            JTextPane descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, description, 600,
                    height,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(descriptionPane, c);

            y += height + 25;

        }

        if (production instanceof Series) {
            Series series = (Series) production;
            int releaseDate = series.getReleaseYear();
            String releaseDateString = "Release date: " + releaseDate;
            JTextPane releaseDatePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                    releaseDateString, 600, 25,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(releaseDatePane, c);
            y += 25;

            int numberOfSeasons = series.getNoSeasons();
            String numberOfSeasonsString = "Number of seasons: " + numberOfSeasons;
            JTextPane numberOfSeasonsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                    numberOfSeasonsString, 600, 25,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(numberOfSeasonsPane, c);
            y += 50;

            String description = series.getDescription();

            int height = getContentHeight(600, description);

            JTextPane descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, description, 600,
                    height,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(descriptionPane, c);

            y += height + 25;

        }

        if (production.getRatings() == null || production.getRatings().size() == 0) {
            String noReviews = "No reviews yet";
            JTextPane noReviewsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, noReviews, 600,
                    25,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(noReviewsPane, c);
            y += 50;
        } else {
            String reviews = "Reviews: ";
            JTextPane reviewsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, reviews, 600,
                    25,
                    new Font("Fira Code", Font.PLAIN, 20));
            c.insets = new Insets(y, 500, 0, 0);
            add(reviewsPane, c);
            y += 35;

            production.sortRatingsByScore();
            ReviewPanel reviewPanel = new ReviewPanel(production.getRatings());
            c.insets = new Insets(y, 500, 0, 0);
            add(reviewPanel, c);
            y += 100;
        }
        if (production instanceof Series) {
            Series series = (Series) production;

            Map<String, List<Episode>> seasons = series.getEpisodes();

            JComboBox<String> seasonsComboBox = new JComboBox<String>();
            for (String season : seasons.keySet()) {
                seasonsComboBox.addItem(season);
            }

            seasonsComboBox.setFont(new Font("Fira Code", Font.PLAIN, 20));

            seasonsComboBox.setPreferredSize(new Dimension(400, 30));

            c.insets = new Insets(y, 300, 0, 0);
            add(seasonsComboBox, c);
            y += 40;

            seasonsComboBox.addActionListener(e -> {

                String selectedSeason = (String) seasonsComboBox.getSelectedItem();

                List<Episode> episodes = seasons.get(selectedSeason);

                for (JComponent component : episodesPane) {
                    remove(component);
                }
                episodesPane.clear();

                int newY = y;
                for (int i = 0; i <= episodes.size() / 2; i++) {

                    Episode episode = episodes.get(i);
                    String episodeString = +(i + 1) + ": " + episode.getName() + " (" + episode.getDuration()
                            + " min)";
                    int neededHeight = getContentHeight(325, episodeString, new Font("Fira Code", Font.PLAIN, 16));

                    if (i == (int) (episodes.size() / 2))
                        neededHeight += 10;

                    JTextPane episodePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, episodeString,
                            325,
                            neededHeight,
                            new Font("Fira Code", Font.PLAIN, 16));
                    c.insets = new Insets(newY, 225, 0, 0);
                    add(episodePane, c);
                    episodesPane.add(episodePane);
                    newY += neededHeight;
                }
                newY = y;
                for (int i = episodes.size() / 2 + 1; i < episodes.size(); i++) {
                    Episode episode = episodes.get(i);
                    String episodeString = +(i + 1) + ": " + episode.getName() + " (" + episode.getDuration()
                            + " min)";
                    int neededHeight = getContentHeight(300, episodeString, new Font("Fira Code", Font.PLAIN, 16));

                    if (i == episodes.size() - 1)
                        neededHeight += 10;

                    JTextPane episodePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, episodeString,
                            300,
                            neededHeight,
                            new Font("Fira Code", Font.PLAIN, 16));
                    c.insets = new Insets(newY, 875, 0, 0);
                    add(episodePane, c);
                    episodesPane.add(episodePane);
                    newY += neededHeight;
                }

                GUIAppFlow.layeredPane.repaint();

            });

            seasonsComboBox.setSelectedIndex(0);
        }

        JTextPane invisiblePoint = HelperFunctions.CreateText(SwingConstants.LEFT, ".", 1, 1,
                new java.awt.Font("Fira Code", 1, 1));
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        invisiblePoint.setForeground(new Color(0, 0, 0, 0));
        add(invisiblePoint, c);

    }

    public static int getContentHeight(int width, String content) {
        JEditorPane dummyEditorPane = new JEditorPane();
        dummyEditorPane.setFont(new Font("Fira Code", Font.PLAIN, 20));
        dummyEditorPane.setSize(width, Short.MAX_VALUE);
        dummyEditorPane.setText(content);
        return dummyEditorPane.getPreferredSize().height;

    }

    public static int getContentHeight(int width, String content, Font font) {
        JEditorPane dummyEditorPane = new JEditorPane();
        dummyEditorPane.setFont(font);
        dummyEditorPane.setSize(width, Short.MAX_VALUE);
        dummyEditorPane.setText(content);
        return dummyEditorPane.getPreferredSize().height;

    }
}
