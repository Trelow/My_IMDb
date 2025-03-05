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

public class AddMoviePanel extends AddProductionPanel {
    JTextField durationField;
    JButton addMovieButton;

    public AddMoviePanel(Movie movieToModify) {

        JTextPane durationPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Duration:", 200, 40,
                new Font("Fira Code", Font.BOLD, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(635, 50, 0, 0);
        add(durationPane, c);

        durationField = new JTextField();
        durationField.setPreferredSize(new Dimension(100, 35));
        durationField.setFont(new Font("Fira Code", Font.PLAIN, 16));
        c.insets = new Insets(635, 250, 0, 0);
        add(durationField, c);

        JButton addMovieButton;
        if (movieToModify == null) {
            String workingDirectory = System.getProperty("user.dir");
            Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "addIcon.png");
            ImageIcon originalIcon = new ImageIcon(path.toString());

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(
                    (int) (originalIcon.getIconWidth() / 4.7),
                    (int) (originalIcon.getIconHeight() / 4.7),
                    Image.SCALE_SMOOTH);

            addMovieButton = new JButton();
            addMovieButton.setIcon(new ImageIcon(scaledImage));

            addMovieButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        } else {
            String workingDirectory = System.getProperty("user.dir");
            Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "save.png");
            ImageIcon originalIcon = new ImageIcon(path.toString());

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(
                    originalIcon.getIconWidth() / 12,
                    originalIcon.getIconHeight() / 12,
                    Image.SCALE_SMOOTH);

            addMovieButton = new JButton();
            addMovieButton.setIcon(new ImageIcon(scaledImage));

            addMovieButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        }

        c.anchor = GridBagConstraints.NORTHWEST;
        add(addMovieButton, c);

        addMovieButton.addActionListener(e -> {

            String title = titleField.getText();

            String description = descriptionArea.getText();

            List<Genre> genres = new ArrayList<Genre>();
            for (Component component : genrePanel.getComponents()) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    genres.add(Genre.valueOf(checkBox.getText()));
                }
            }

            List<String> actors = new ArrayList<String>();
            for (Component component : actorsPanel.getComponents()) {
                JPanel actorPanel = (JPanel) component;
                JTextField actorField = (JTextField) actorPanel.getComponent(0);
                actors.add(actorField.getText());
            }

            List<String> directors = new ArrayList<String>();
            for (Component component : directorsPanel.getComponents()) {
                JPanel directorPanel = (JPanel) component;
                JTextField directorField = (JTextField) directorPanel.getComponent(0);
                directors.add(directorField.getText());
            }

            int releaseYear = (int) releaseYearComboBox.getSelectedItem();

            if (title.equals("") || description.equals("") || genres.size() == 0) {
                JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (String actor : actors) {
                if (actor.equals("")) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            for (String director : directors) {
                if (director.equals("")) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int duration = 0;
            try {
                duration = Integer.parseInt(durationField.getText());
            } catch (NumberFormatException exception) {

                JOptionPane.showMessageDialog(null, "Duration must be a number", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check if title already exists
            for (Production production : IMDB.getInstance().getProductions()) {
                if (production.getTitle().equals(title) && production != movieToModify) {
                    JOptionPane.showMessageDialog(null, "Title already exists", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
            Staff<?> staff = (Staff<?>) user;
            Movie movie = null;
            if (movieToModify == null)
                movie = new Movie();
            else {
                movie = movieToModify;
                staff.removeProductionFromContributions(movie);
            }
            movie.setTitle(title);
            movie.setDescription(description);
            movie.setGenres(genres);
            movie.setActors(actors);
            movie.setDirectors(directors);
            movie.setReleaseYear(releaseYear);
            movie.setDuration(duration);
            if (movie.getPoster() == null)
                movie.setPoster("NoImage.png");

            if (movieToModify == null) {

                staff.addProductionSystem(movie);

                JOptionPane.showMessageDialog(null, "Movie added successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.ShowAllContributions();
            } else {
                staff.addProductionToContributions(movie);
                JOptionPane.showMessageDialog(null, "Movie information updated", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

}