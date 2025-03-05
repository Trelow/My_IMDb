package org.gui;

import java.time.LocalDateTime;

import javax.swing.*;

import javax.swing.text.StyleConstants;

import org.production.*;
import org.user.*;
import org.imdb.*;
import org.request.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;
import org.actor.*;

public class RequestPanel extends JPanel {
    JButton xButton = null;
    JTextArea descriptionArea = null;
    JComboBox<RequestType> ratingComboBox = new JComboBox<RequestType>();
    String toUsername = null;

    public RequestPanel(User<?> toUser, Object object) {

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        xButton = new JButton("X");
        xButton.setForeground(Color.WHITE);
        xButton.setFont(new Font("Fira Code", Font.BOLD, 17));
        xButton.setFocusPainted(false);
        xButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        xButton.setBackground(new Color(45, 45, 45, 150));
        xButton.addActionListener(e -> {

            GUIAppFlow.layeredPane.remove(GUIAppFlow.requestPanel);
            GUIAppFlow.commentPanel = null;
            GUIAppFlow.requestPanel = null;
            GUIAppFlow.layeredPane.revalidate();
            GUIAppFlow.layeredPane.repaint();

        });

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(10, 550, 0, 0);
        add(xButton, c);

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

        JTextPane ratingLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Type:", 100, 50,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(50, 50, 0, 0);
        add(ratingLabel, c);

        if (toUser == null) {

            ratingComboBox.addItem(RequestType.DELETE_ACCOUNT);
            ratingComboBox.addItem(RequestType.OTHERS);
        } else {
            if (object instanceof Production) {
                ratingComboBox.addItem(RequestType.MOVIE_ISSUE);
            } else if (object instanceof Actor) {
                ratingComboBox.addItem(RequestType.ACTOR_ISSUE);
            }
        }

        c.insets = new Insets(50, 200, 0, 0);
        add(ratingComboBox, c);

        if (toUser != null)
            toUsername = toUser.getUsername();
        else
            toUsername = "ADMIN";
        JTextPane toUsernameLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "To: ", 100, 50,
                new Font("Fira Code", Font.BOLD, 20));
        c.insets = new Insets(100, 50, 0, 0);
        add(toUsernameLabel, c);

        JTextPane toUsernameLabel2 = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, toUsername, 250, 50,
                new Font("Fira Code", Font.PLAIN, 16));
        c.insets = new Insets(100, 200, 0, 0);
        add(toUsernameLabel2, c);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Fira Code", Font.PLAIN, 16));

        c.insets = new Insets(300, 410, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;

        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        submitButton.addActionListener(e -> {

            LocalDateTime date = LocalDateTime.now();

            Request request = new Request((RequestType) ratingComboBox.getSelectedItem(), date,
                    descriptionArea.getText(),
                    IMDB.getInstance().getCurrentLoggedInUser().getUsername(), toUsername);

            RequestsManager requestsManager = (RequestsManager) IMDB.getInstance().getCurrentLoggedInUser();
            requestsManager.createRequest(request);
            if (toUser == null) {
                RequestsHolder.addRequest(request);

                for (User<?> user : IMDB.getInstance().getUsers()) {
                    if (user.getAccountType() == AccountType.Admin) {
                        request.register(user);
                        request.notifyObservers("New request from " + request.getUserUsername() + " ("
                                + request.getType().toString() + ")");
                        request.unregister(user);
                    }
                }
            } else {
                request.register(toUser);
                request.notifyObservers("New request from " + request.getUserUsername() + " ("
                        + request.getType().toString() + ")");
                request.unregister(toUser);
            }
            request.register(IMDB.getInstance().getCurrentLoggedInUser());

            JOptionPane.showMessageDialog(null, "Request was sent with success", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if (GUIAppFlow.lastObjectList != null && GUIAppFlow.lastObjectList.isRequestList)
                GUIAppFlow.lastObjectList.AddObject(request);
            GUIAppFlow.layeredPane.remove(GUIAppFlow.requestPanel);
            GUIAppFlow.requestPanel = null;
            GUIAppFlow.layeredPane.revalidate();
            GUIAppFlow.layeredPane.repaint();

        });

        add(submitButton, c);

    }

}