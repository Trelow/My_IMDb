package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.user.*;
import org.imdb.*;
import org.actor.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;
import org.production.*;

import java.util.ArrayList;

public class AddActorPanel extends JPanel {
    JTextField titleField = new JTextField();
    JTextArea descriptionArea = null;
    JPanel filmographyPanel = null;
    public JButton backButton = new JButton();

    public AddActorPanel(Actor actorToModify) {
        filmographyPanel = new JPanel();
        filmographyPanel.setLayout(new BoxLayout(filmographyPanel, BoxLayout.Y_AXIS));

        setLayout(new GridBagLayout());

        JTextPane titlePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Name:", 100, 100,
                new Font("Fira Code", Font.BOLD, 26));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(35, 50, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;

        add(titlePane, c);

        titleField.setPreferredSize(new Dimension(600, 35));
        titleField.setFont(new Font("Fira Code", Font.PLAIN, 25));
        c.insets = new Insets(35, 250, 0, 0);

        add(titleField, c);

        JTextPane descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Description:", 200, 100,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(85, 50, 0, 0);
        add(descriptionPane, c);

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        descriptionArea.setFont(new Font("Fira Code", Font.PLAIN, 20));

        c.insets = new Insets(85, 250, 0, 0);

        add(scrollPane, c);

        JTextPane filmographyPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Filmography:", 200, 100,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(335, 50, 0, 0);
        add(filmographyPane, c);

        JButton addFilmographyButton = new JButton("Add production");
        c.insets = new Insets(335, 250, 0, 0);
        add(addFilmographyButton, c);
        addFilmographyButton.addActionListener(e -> {
            new FilmographyPanel(filmographyPanel);
        });

        new FilmographyPanel(filmographyPanel);

        c.insets = new Insets(385, 250, 0, 0);
        add(filmographyPanel, c);

        JButton addActorButton;
        if (actorToModify == null) {
            String workingDirectory = System.getProperty("user.dir");
            Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "addIcon.png");
            ImageIcon originalIcon = new ImageIcon(path.toString());

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(
                    (int) (originalIcon.getIconWidth() / 4.7),
                    (int) (originalIcon.getIconHeight() / 4.7),
                    Image.SCALE_SMOOTH);

            addActorButton = new JButton();
            addActorButton.setIcon(new ImageIcon(scaledImage));
            addActorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

            addActorButton = new JButton();
            addActorButton.setIcon(new ImageIcon(scaledImage));
            addActorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        }

        c.anchor = GridBagConstraints.NORTHWEST;
        add(addActorButton, c);

        addActorButton.addActionListener(e -> {
            String name = titleField.getText();
            String description = descriptionArea.getText();
            List<Filmography> filmography = new ArrayList<Filmography>();

            for (Component component : filmographyPanel.getComponents()) {
                FilmographyPanel filmographyPanel = (FilmographyPanel) component;
                String productionName = filmographyPanel.productionName.getText();
                String productionType = (String) filmographyPanel.productionType.getSelectedItem();
                ProductionType type = ProductionType.valueOf(productionType);
                if (!productionName.equals("")) {
                    Filmography filmographyItem = new Filmography(productionName, type);
                    filmography.add(filmographyItem);
                } else {
                    JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (name.equals("") || description.equals("")) {
                JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check if actor already exists
            for (Actor actor : IMDB.getInstance().getActors()) {
                if (actor.getName().equals(name) && actor != actorToModify) {
                    JOptionPane.showMessageDialog(null, "Actor already exists", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Actor actor = null;
            if (actorToModify == null) {
                actor = new Actor(name, description, filmography);
                Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
                staff.addActorSystem(actor);

                JOptionPane.showMessageDialog(null, "Actor added successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.ShowAllContributions();
            } else {
                Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
                actor = actorToModify;
                staff.removeActorFromContributions(actor);
                actor.setName(name);
                actor.setDescription(description);
                actor.setFilmography(filmography);
                staff.addActorToContributions(actor);

                JOptionPane.showMessageDialog(null, "Actor information updated", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });

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

    class FilmographyPanel extends JPanel {
        JTextField productionName;

        JComboBox<String> productionType;

        FilmographyPanel(JPanel parentPanel) {

            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            productionName = new JTextField(10);
            productionType = new JComboBox<>();
            productionType.addItem("Movie");
            productionType.addItem("Series");

            JButton removeButton = new JButton("-");
            removeButton.addActionListener(e -> {
                parentPanel.remove(this);
                parentPanel.revalidate();
                parentPanel.repaint();

            });

            this.add(new JLabel("Title:"));
            this.add(productionName);
            this.add(new JLabel("Type:"));
            this.add(productionType);
            this.add(removeButton);

            parentPanel.add(this);
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }
}
