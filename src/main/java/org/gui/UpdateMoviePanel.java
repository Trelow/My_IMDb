package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;

import org.production.*;

public class UpdateMoviePanel extends AddMoviePanel {
    public JScrollPane scrollPane;

    public UpdateMoviePanel(Movie movie) {
        super(movie);

        titleField.setText(movie.getTitle());
        descriptionArea.setText(movie.getDescription());

        for (Component component : genrePanel.getComponents()) {
            JCheckBox checkBox = (JCheckBox) component;
            if (movie.getGenres().contains(Genre.valueOf(checkBox.getText()))) {
                checkBox.setSelected(true);
            }
        }

        actorsPanel.removeAll();

        for (String actor : movie.getActors()) {
            JTextField actorRow = addRow(actorsPanel);
            actorRow.setText(actor);

        }

        directorsPanel.removeAll();

        for (String director : movie.getDirectors()) {
            JTextField directorRow = addRow(directorsPanel);
            directorRow.setText(director);

        }

        releaseYearComboBox.setSelectedItem(movie.getReleaseYear());

        durationField.setText(Integer.toString(movie.getDuration()));

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

            HelperFunctions.CreateNewPage(movie, 1);
            GUIAppFlow.layeredPane.repaint();
            GUIAppFlow.layeredPane.revalidate();

        });

    }
}