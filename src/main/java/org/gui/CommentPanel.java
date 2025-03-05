package org.gui;

import javax.swing.*;

import javax.swing.text.StyleConstants;

import org.production.*;
import org.user.*;
import org.imdb.*;

import java.awt.*;

import org.gui.utils.HelperFunctions;

public class CommentPanel extends JPanel {
    JButton xButton = null;
    JTextArea descriptionArea = null;
    JComboBox<Integer> ratingComboBox = new JComboBox<Integer>();

    public CommentPanel(Object object) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        xButton = new JButton("X");
        xButton.setForeground(Color.WHITE);
        xButton.setFont(new Font("Fira Code", Font.BOLD, 17));
        xButton.setFocusPainted(false);
        xButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        xButton.setBackground(new Color(45, 45, 45, 150));
        xButton.addActionListener(e -> {

            GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
            GUIAppFlow.commentPanel = null;
            GUIAppFlow.layeredPane.revalidate();
            GUIAppFlow.layeredPane.repaint();
        });

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(10, 535, 0, 0);
        add(xButton, c);

        Rating rating = null;
        if (object instanceof Production) {
            Production p = (Production) object;

            for (Rating r : p.getRatings()) {
                if (r.getUsername().equals(IMDB.getInstance().getCurrentLoggedInUser().getUsername())) {
                    rating = r;
                    break;
                }
            }
        }

        if (rating == null) {

            JTextPane messageLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER,
                    "You have not given any rating yet.", 500, 30,
                    new Font("Fira Code", Font.BOLD, 18));

            c.insets = new Insets(0, 0, 20, 0);
            c.anchor = GridBagConstraints.CENTER;
            add(messageLabel, c);

            JButton rateButton = new JButton("Give a Rating");
            rateButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
            c.insets = new Insets(50, 0, 0, 0);
            rateButton.addActionListener(e -> {

                initialize(object, null);
            });
            add(rateButton, c);

        } else {

            initialize(object, rating);
            descriptionArea.setText(rating.getComment());
            ratingComboBox.setSelectedIndex(rating.getScore() - 1);

            JButton deleteButton = new JButton("Delete ");
            deleteButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
            c.insets = new Insets(250, 0, 0, 100);
            c.anchor = GridBagConstraints.CENTER;
            final Rating finalRating = rating;
            deleteButton.addActionListener(e -> {

                Production p = (Production) object;
                p.removeRating(finalRating);

                JOptionPane.showMessageDialog(null, "Rating was deleted with success", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);
                if (object instanceof Movie)
                    HelperFunctions.CreateNewPage((Movie) object, 1);
                else if (object instanceof Series)
                    HelperFunctions.CreateNewPage((Series) object, 1);

                GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
                GUIAppFlow.commentPanel = null;
                GUIAppFlow.layeredPane.revalidate();
                GUIAppFlow.layeredPane.repaint();
            });

            add(deleteButton, c);
        }
    }

    private void initialize(Object object, Rating rating) {

        for (Component component : getComponents()) {
            if (component != xButton) {
                remove(component);
            }
        }
        revalidate();
        repaint();

        JTextPane messageLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                "Username: " + IMDB.getInstance().getCurrentLoggedInUser().getUsername(), 400, 50,
                new Font("Fira Code", Font.BOLD, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(50, 50, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(messageLabel, c);

        JTextPane ratingLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Rating:", 100, 50,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(100, 50, 0, 0);
        add(ratingLabel, c);
        for (int i = 1; i <= 10; i++) {
            ratingComboBox.addItem(i);
        }
        ratingComboBox.setSelectedIndex(4);
        ratingComboBox.setFont(new Font("Fira Code", Font.PLAIN, 17));
        c.insets = new Insets(100, 200, 0, 0);
        add(ratingComboBox, c);

        JTextPane descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Comment:", 140, 50,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(150, 50, 0, 0);

        add(descriptionPane, c);

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 125));

        descriptionArea.setFont(new Font("Fira Code", Font.PLAIN, 16));

        c.insets = new Insets(150, 200, 0, 0);

        add(scrollPane, c);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Fira Code", Font.PLAIN, 16));
        c.anchor = GridBagConstraints.CENTER;
        if (rating != null)
            c.insets = new Insets(250, 100, 0, 0);
        else
            c.insets = new Insets(250, 0, 0, 0);

        submitButton.addActionListener(e -> {
            if (object instanceof Production) {
                Production p = (Production) object;

                if (rating == null) {
                    Rating newRating = new Rating(IMDB.getInstance().getCurrentLoggedInUser().getUsername(),
                            (Integer) ratingComboBox.getSelectedItem(), descriptionArea.getText());
                    newRating.setUser(IMDB.getInstance().getCurrentLoggedInUser());
                    ((Regular<?>) IMDB.getInstance().getCurrentLoggedInUser()).addReview(newRating, p);

                    Staff<?> staff = null;
                    for (User<?> user : IMDB.getInstance().getUsers()) {
                        if (user instanceof Staff<?>) {
                            Staff<?> s = (Staff<?>) user;
                            if (s.getContributions().contains(p)) {
                                staff = s;
                                break;
                            }
                        }
                    }
                    if (staff != null) {

                        newRating.register(staff);
                        newRating.notifyObservers("User " + IMDB.getInstance().getCurrentLoggedInUser().getUsername()
                                + " has rated your contribution \"" + p.getTitle() + "\"" + " with " + newRating.getScore() + "  ");
                        newRating.unregister(staff) ;

                    }

                    for (Rating r : p.getRatings()) {
                        r.notifyObservers("User " + IMDB.getInstance().getCurrentLoggedInUser().getUsername()
                                + " has rated " + p.getTitle());

                    }
                    newRating.register(IMDB.getInstance().getCurrentLoggedInUser());
                } else {

                    rating.setScore((Integer) ratingComboBox.getSelectedItem());
                    rating.setComment(descriptionArea.getText());
                    p.recalculateAverageRating();

                }

                JOptionPane.showMessageDialog(null, "Rating was added with success", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
                GUIAppFlow.commentPanel = null;
                GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);
                if (object instanceof Movie)
                    HelperFunctions.CreateNewPage((Movie) object, 1);
                else if (object instanceof Series)
                    HelperFunctions.CreateNewPage((Series) object, 1);

                GUIAppFlow.layeredPane.revalidate();
                GUIAppFlow.layeredPane.repaint();
            }

        });

        add(submitButton, c);

    }
}
