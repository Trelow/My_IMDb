package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;

import org.production.*;

import java.util.Map;

public class UpdateSeriesPanel extends AddSeriesPanel {
    public JScrollPane scrollPane;

    public UpdateSeriesPanel(Series series) {
        super(series);

        titleField.setText(series.getTitle());
        descriptionArea.setText(series.getDescription());

        for (Component component : genrePanel.getComponents()) {
            JCheckBox checkBox = (JCheckBox) component;
            if (series.getGenres().contains(Genre.valueOf(checkBox.getText()))) {
                checkBox.setSelected(true);
            }
        }

        actorsPanel.removeAll();

        for (String actor : series.getActors()) {
            JTextField actorRow = addRow(actorsPanel);
            actorRow.setText(actor);

        }

        directorsPanel.removeAll();

        for (String director : series.getDirectors()) {
            JTextField directorRow = addRow(directorsPanel);
            directorRow.setText(director);

        }

        releaseYearComboBox.setSelectedItem(series.getReleaseYear());

        Map<String, List<Episode>> seasons = series.getEpisodes();

        for (String season : seasons.keySet()) {
            addSeason();

            List<Episode> episodes = seasons.get(season);

            JPanel seasonPanel = seasonPanels.get(seasonPanels.size() - 1);
            for (Episode episode : episodes) {
                EpisodePanel episodePanel = new EpisodePanel(seasonPanel);
                episodePanel.episodeNameField.setText(episode.getName());
                episodePanel.episodeDurationField.setText(Integer.toString(episode.getDuration()));

            }
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.anchor = GridBagConstraints.NORTHWEST;
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
        c.insets = new Insets(15, 1100, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(backButton, c);

        backButton.addActionListener(e -> {

            GUIAppFlow.layeredPane.remove(scrollPane);
            GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);

            HelperFunctions.CreateNewPage(series, 1);
            GUIAppFlow.layeredPane.repaint();
            GUIAppFlow.layeredPane.revalidate();

        });

    }
}