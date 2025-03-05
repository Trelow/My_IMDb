package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.text.StyleConstants;

import java.awt.*;
import org.gui.utils.HelperFunctions;

import org.production.*;
import java.util.Calendar;

public class AddProductionPanel extends JPanel {
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = null;
        JPanel genrePanel;
        JPanel actorsPanel;
        JPanel directorsPanel;
        JComboBox<Integer> releaseYearComboBox;
        public JButton backButton = new JButton();

        public AddProductionPanel() {
                setLayout(new GridBagLayout());

                JTextPane titlePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Title:", 100, 100,
                                new Font("Fira Code", Font.BOLD, 26));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                c.weighty = 1.0;
                c.insets = new Insets(25, 50, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;

                add(titlePane, c);

                titleField.setPreferredSize(new Dimension(600, 35));
                titleField.setFont(new Font("Fira Code", Font.PLAIN, 25));
                c.insets = new Insets(25, 250, 0, 0);

                add(titleField, c);

                JTextPane descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Description:", 200,
                                100,
                                new Font("Fira Code", Font.BOLD, 20));
                c.insets = new Insets(75, 50, 0, 0);

                add(descriptionPane, c);

                descriptionArea = new JTextArea();
                descriptionArea.setLineWrap(true);
                descriptionArea.setWrapStyleWord(true);

                JScrollPane scrollPane = new JScrollPane(descriptionArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setPreferredSize(new Dimension(600, 200));

                descriptionArea.setFont(new Font("Fira Code", Font.PLAIN, 20));

                c.insets = new Insets(75, 250, 0, 0);

                add(scrollPane, c);

                JTextPane genresPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Genres:", 200, 100,
                                new Font("Fira Code", Font.BOLD, 20));
                c.insets = new Insets(325, 50, 0, 0);
                add(genresPane, c);

                genrePanel = new JPanel(new GridLayout(2, 0));
                Genre[] genresEnum = Genre.values();

                for (Genre genre : genresEnum) {
                        JCheckBox checkBox = new JCheckBox(genre.toString());
                        genrePanel.add(checkBox);
                }

                c.insets = new Insets(325, 250, 0, 0);
                add(genrePanel, c);

                JTextPane actorsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Actors:", 200, 100,
                                new Font("Fira Code", Font.BOLD, 20));
                c.insets = new Insets(385, 50, 0, 0);
                add(actorsPane, c);

                actorsPanel = new JPanel();

                actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.X_AXIS));
                addRow(actorsPanel);
                c.insets = new Insets(385, 250, 0, 0);
                add(actorsPanel, c);

                JButton addActorButton = new JButton("Add actor");
                c.insets = new Insets(435, 250, 0, 0);
                add(addActorButton, c);

                addActorButton.addActionListener(e -> addRow(actorsPanel));

                JTextPane directorsPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Directors:", 200, 100,
                                new Font("Fira Code", Font.BOLD, 20));
                c.insets = new Insets(485, 50, 0, 0);
                add(directorsPane, c);

                directorsPanel = new JPanel();

                directorsPanel.setLayout(new BoxLayout(directorsPanel, BoxLayout.X_AXIS));
                addRow(directorsPanel);
                c.insets = new Insets(485, 250, 0, 0);
                add(directorsPanel, c);

                JButton addDirectors = new JButton("Add director");
                c.insets = new Insets(535, 250, 0, 0);
                add(addDirectors, c);

                addDirectors.addActionListener(e -> addRow(directorsPanel));

                JTextPane releaseYearPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Release year:", 200,
                                100,
                                new Font("Fira Code", Font.BOLD, 20));
                c.insets = new Insets(585, 50, 0, 0);
                add(releaseYearPane, c);

                releaseYearComboBox = new JComboBox<>();
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                for (int year = 1950; year <= currentYear; year++) {
                        releaseYearComboBox.addItem(year);
                }
                c.insets = new Insets(585, 250, 0, 0);
                add(releaseYearComboBox, c);

                c = new GridBagConstraints();
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
        }

        public JTextField addRow(JPanel parentPanel) {

                JPanel actorRow = new JPanel();
                JTextField actorField = new JTextField(15);
                JButton removeButton = new JButton("-");

                removeButton.addActionListener(e -> {
                        parentPanel.remove(actorRow);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                });

                actorRow.add(actorField);
                actorRow.add(removeButton);

                parentPanel.add(actorRow);
                parentPanel.revalidate();
                parentPanel.repaint();

                return actorField;

        }

}