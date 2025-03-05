package org.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.production.*;
import org.user.*;
import org.user.experience.IssueStrategy;
import org.imdb.*;
import org.request.*;

import org.gui.utils.*;

import java.awt.*;
import org.actor.*;
import org.gui.listRenderers.*;

public class GraphicObjectList {

    public JList<Object> list;
    private Object prevObject = null;
    public JScrollPane scrollPane = null;
    JTextPane noProductionsLabel = null;

    public boolean isFavoriteList = false;
    public boolean isRequestList = false;

    public GraphicObjectList(List<Object> objects, int type) {
        initializeUI(objects, type);
    }

    private void initializeUI(List<Object> objects, int type) {

        DefaultListModel<Object> combinedModel = new DefaultListModel<>();

        if (type == 0 || type == 1)
            for (Object object : objects) {
                if (object instanceof Production) {
                    combinedModel.addElement((Production) object);
                } else if (object instanceof Actor) {
                    combinedModel.addElement((Actor) object);
                }
            }
        else if (type == 2)
            for (Object object : objects) {
                if (object instanceof User) {
                    combinedModel.addElement((User<?>) object);
                }
            }
        else if (type == 3 || type == 4) {
            for (Object object : objects) {
                if (object instanceof Request) {
                    combinedModel.addElement((Request) object);
                }
            }
            isRequestList = true;
        }

        list = new JList<>(combinedModel);
        if (type == 0) {
            list.setCellRenderer(new NoImageRenderer());
        } else if (type == 1)
            list.setCellRenderer(new ImageRenderer());
        else if (type == 2)
            list.setCellRenderer(new UserRenderer());
        else if (type == 3 || type == 4) {
            list.setCellRenderer(new RequestsRenderer(type));
        }

        scrollPane = new JScrollPane(list);
        GUIAppFlow.AddToPage(scrollPane, 0);
        scrollPane.setBounds(0, 0, 1200, 710);

        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Object object = list.getSelectedValue();

                if (object != null && object.equals(prevObject)) {

                    if (type != 3 && type != 4)
                        HelperFunctions.CreateNewPage(object, 1);
                    else {

                        Request request = (Request) object;
                        if (type == 4) {

                            String[] options = { "Resolve", "Delete", "Close" };
                            int result = JOptionPane.showOptionDialog(null, "What do you want to do with this request?",
                                    "Request", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                                    options,
                                    options[0]);
                            if (result == 0) {

                                request.notifyObservers("Your request to " + request.getResolverUsername()
                                        + " has been resolved");

                                User<?> user = null;

                                for (User<?> u : IMDB.getInstance().getUsers()) {
                                    if (u.getUsername().equals(request.getUserUsername())) {
                                        user = u;
                                        break;
                                    }
                                }
                                if (request.getType() == RequestType.ACTOR_ISSUE
                                        || request.getType() == RequestType.MOVIE_ISSUE) {
                                    user.updateExperience(new IssueStrategy());
                                }

                                RequestsManager requestsManager = (RequestsManager) user;

                                requestsManager.removeRequest(request);
                                if (request.getResolverUsername().toUpperCase().equals("ADMIN")) {
                                    RequestsHolder.deleteRequest(request);
                                }

                                RemoveObject(object);

                            }
                            if (result == 1) {
                                request.notifyObservers("Your request to " + request.getResolverUsername()
                                        + " has been rejected");

                                User<?> user = null;

                                for (User<?> u : IMDB.getInstance().getUsers()) {
                                    if (u.getUsername().equals(request.getUserUsername())) {
                                        user = u;
                                        break;
                                    }
                                }

                                RequestsManager requestsManager = (RequestsManager) user;

                                requestsManager.removeRequest(request);
                                if (request.getResolverUsername().toUpperCase().equals("ADMIN")) {
                                    RequestsHolder.deleteRequest(request);
                                }

                                RemoveObject(object);
                            }
                        } else if (type == 3)
                            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular
                                    || IMDB
                                            .getInstance()
                                            .getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
                                String[] options = { "Delete", "Close" };
                                int result = JOptionPane.showOptionDialog(null,
                                        "Want to delete request?",
                                        "Request", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                                        options[0]);
                                if (result == 0) {

                                    RequestsManager requestsManager = (RequestsManager) IMDB.getInstance()
                                            .getCurrentLoggedInUser();

                                    requestsManager.removeRequest(request);
                                    if (request.getResolverUsername().toUpperCase().equals("ADMIN")) {
                                        RequestsHolder.deleteRequest(request);
                                    }

                                    RemoveObject(object);

                                }
                            }
                    }

                }
                prevObject = object;

            }
        });

        if (combinedModel.isEmpty()) {
            PrintTheMessageIfListIsEmpty(combinedModel);
            scrollPane.setVisible(false);
        } else {
            scrollPane.setVisible(true);
        }

    }

    public void PrintTheMessageIfListIsEmpty(DefaultListModel<Object> combinedModel) {
        noProductionsLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER,
                "The list is empty", 1000, 100, Fonts.getInstance().font.deriveFont(45f));
        noProductionsLabel.setForeground(Color.WHITE);
        noProductionsLabel.setBounds(0, 0, 1000, 50);
        GUIAppFlow.AddToPage(noProductionsLabel, 0);
        noProductionsLabel.setVisible(true);
        noProductionsLabel.setLocation(100, 275);
    }

    public void RemoveObject(Object object) {
        DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();
        model.removeElement(object);
        if (model.isEmpty()) {
            PrintTheMessageIfListIsEmpty(model);
            scrollPane.setVisible(false);
            noProductionsLabel.setVisible(true);
        }

    }

    public void AddObject(Object object) {
        DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();
        model.addElement(object);
        if (!model.isEmpty()) {
            scrollPane.setVisible(true);
            if (noProductionsLabel != null)
                noProductionsLabel.setVisible(false);
        }

    }
}
