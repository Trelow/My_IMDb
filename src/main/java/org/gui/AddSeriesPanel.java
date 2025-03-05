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
import java.util.LinkedHashMap;

public class AddSeriesPanel extends AddProductionPanel {
    public int numberOfSeasons = 0;
    public List<JPanel> seasonPanels = new ArrayList<>();
    public JLabel invisiblePoint = new JLabel(".");

    public AddSeriesPanel(Series seriesToModify) {

        JTextPane seasonsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Seasons:", 200, 40,
                new Font("Fira Code", Font.BOLD, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(635, 50, 0, 0);
        add(seasonsPane, c);

        invisiblePoint.setPreferredSize(new Dimension(1, 50));
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 0;
        spacerConstraints.gridy = GridBagConstraints.RELATIVE;
        spacerConstraints.weightx = 1.0;
        spacerConstraints.weighty = 1.0;
        spacerConstraints.fill = GridBagConstraints.VERTICAL;
        spacerConstraints.anchor = GridBagConstraints.SOUTH;
        add(invisiblePoint, spacerConstraints);

        JButton addSeasonButton = new JButton("Add season");
        addSeasonButton.setPreferredSize(new Dimension(130, 30));
        addSeasonButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
        c.insets = new Insets(635, 250, 0, 0);
        add(addSeasonButton, c);

        addSeasonButton.addActionListener(e -> {
            addSeason();

        });

        JButton addSeriesButton;
        if (seriesToModify == null) {
            String workingDirectory = System.getProperty("user.dir");
            Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "addIcon.png");
            ImageIcon originalIcon = new ImageIcon(path.toString());

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(
                    (int) (originalIcon.getIconWidth() / 4.7),
                    (int) (originalIcon.getIconHeight() / 4.7),
                    Image.SCALE_SMOOTH);

            addSeriesButton = new JButton();
            addSeriesButton.setIcon(new ImageIcon(scaledImage));

            addSeriesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

            addSeriesButton = new JButton();
            addSeriesButton.setIcon(new ImageIcon(scaledImage));

            addSeriesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        }

        c.anchor = GridBagConstraints.NORTHWEST;
        add(addSeriesButton, c);

        addSeriesButton.addActionListener(e -> {

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

            Map<String, List<Episode>> seasons = new LinkedHashMap<>();

            for (JPanel seasonPanel : seasonPanels) {

                JTextPane seasonPane = (JTextPane) seasonPanel.getComponent(0);
                String season = seasonPane.getText();

                List<Episode> episodes = new ArrayList<Episode>();

                for (Component component : seasonPanel.getComponents()) {
                    if (component instanceof EpisodePanel) {
                        EpisodePanel episodePanel = (EpisodePanel) component;
                        String episodeName = episodePanel.episodeNameField.getText();
                        String episodeDuration = episodePanel.episodeDurationField.getText();
                        if (!episodeName.equals("") && !episodeDuration.equals("")) {

                            int duration = 0;
                            try {
                                duration = Integer.parseInt(episodeDuration);
                            } catch (NumberFormatException exception) {
                                JOptionPane.showMessageDialog(null, "Duration must be a number", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            Episode episode = new Episode(episodeName, duration);
                            episodes.add(episode);

                        } else {
                            JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                seasons.put(season, episodes);

            }

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

            // check if title already exists
            for (Production production : IMDB.getInstance().getProductions()) {
                if (production.getTitle().equals(title) && production != seriesToModify) {
                    JOptionPane.showMessageDialog(null, "Title already exists", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
            Staff<?> staff = (Staff<?>) user;
            Series series = null;
            if (seriesToModify == null) {
                series = new Series();
            } else {
                series = seriesToModify;
                staff.removeProductionFromContributions(series);
            }
            series.setTitle(title);
            series.setDescription(description);
            series.setGenres(genres);
            series.setActors(actors);
            series.setDirectors(directors);
            series.setReleaseYear(releaseYear);
            series.setNoSeasons(numberOfSeasons);
            series.setEpisodes(seasons);

            if (series.getPoster() == null)
                series.setPoster("NoImage.png");

            if (seriesToModify == null) {
                staff.addProductionSystem(series);

                JOptionPane.showMessageDialog(null, "Series added successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.ShowAllContributions();
            } else {
                staff.addProductionToContributions(series);
                JOptionPane.showMessageDialog(null, "Series information updated", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            }
        });

    }

    public void addSeason() {
        numberOfSeasons++;

        JPanel seasonPanel = new JPanel();
        seasonPanel.setLayout(new BoxLayout(seasonPanel, BoxLayout.Y_AXIS));

        JTextPane seasonPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Season " + numberOfSeasons,
                200, 40, new Font("Fira Code", Font.BOLD, 20));
        seasonPanel.add(seasonPane);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addEpisodesButton = new JButton("Add episodes");
        addEpisodesButton.setPreferredSize(new Dimension(175, 30));
        addEpisodesButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
        buttonsPanel.add(addEpisodesButton);

        addEpisodesButton.addActionListener(e -> {
            new EpisodePanel(seasonPanel);
        });

        JButton removeSeasonButton = new JButton("Remove season");
        removeSeasonButton.setPreferredSize(new Dimension(175, 30));
        removeSeasonButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
        removeSeasonButton.addActionListener(e -> {
            this.remove(seasonPanel);
            this.revalidate();
            this.repaint();
            numberOfSeasons--;
            seasonPanels.remove(seasonPanel);
            updateRemoveButtons();
        });
        buttonsPanel.add(removeSeasonButton);

        seasonPanels.add(seasonPanel);

        seasonPanel.add(buttonsPanel);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;

        c.insets = new Insets(685, 400 * (numberOfSeasons - 1) + 50, 0, 0);
        this.add(seasonPanel, c);
        updateRemoveButtons();

    }

    private void updateRemoveButtons() {
        for (int i = 0; i < seasonPanels.size(); i++) {
            JPanel seasonPanel = seasonPanels.get(i);

            JPanel buttonsPanel = (JPanel) seasonPanel.getComponent(1);
            JButton removeButton = (JButton) buttonsPanel.getComponent(1);
            removeButton.setVisible(i == seasonPanels.size() - 1);
        }
    }

    class EpisodePanel extends JPanel {
        JTextField episodeNameField;
        JTextField episodeDurationField;

        EpisodePanel(JPanel parentPanel) {

            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            episodeNameField = new JTextField(10);
            episodeDurationField = new JTextField(3);

            JButton removeButton = new JButton("-");
            removeButton.addActionListener(e -> {
                parentPanel.remove(this);
                parentPanel.revalidate();
                parentPanel.repaint();

            });

            this.add(new JLabel("Name:"));
            this.add(episodeNameField);
            this.add(new JLabel("Duration:"));
            this.add(episodeDurationField);
            this.add(removeButton);

            parentPanel.add(this);
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }

}
