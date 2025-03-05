package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.StyleConstants;

import org.production.*;
import org.user.*;
import org.imdb.*;

import java.awt.event.ItemEvent;

import java.awt.*;

import org.gui.utils.Fonts;
import org.gui.utils.HelperFunctions;
import org.terminal.AplicationFlow;

public class GUIAppFlow extends JFrame {

    public static JLayeredPane layeredPane;
    public static JPanel rightMenuBar;
    public static JPanel addSelectorMenu;
    public static MainMenuBar menuBar;
    public static JPanel filterPanel;
    public static ProductionFilter filter = new ProductionFilter();
    public static JScrollPane currentPage = null;

    public static GraphicObjectList lastObjectList = null;
    public static JScrollPane commentPanel = null;
    public static JScrollPane requestPanel = null;

    public GUIAppFlow() {

        setUndecorated(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Menu");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        CustomTitleBar titleBar = new CustomTitleBar(this);
        add(titleBar);
        titleBar.setBounds(0, 0, getWidth(), 30);

        menuBar = new MainMenuBar(this);
        add(menuBar);
        menuBar.setBounds(0, 30, getWidth(), 60);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 90, 1200, 710);
        layeredPane.setBackground(new Color(200, 200, 200));
        add(layeredPane);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 40, 150));
        panel.setLayout(null);

        layeredPane.add(panel, 3, 0);
        panel.setBounds(950, 0, 250, 800);
        panel.setVisible(false);
        rightMenuBar = panel;

        JPanel addSelectorMenuCreated = new JPanel();
        addSelectorMenuCreated.setBackground(new Color(40, 40, 40, 150));
        addSelectorMenuCreated.setLayout(null);

        layeredPane.add(addSelectorMenuCreated, 3, 0);
        addSelectorMenuCreated.setBounds(950, 0, 250, 800);
        addSelectorMenuCreated.setVisible(false);
        addSelectorMenu = addSelectorMenuCreated;

        int y = 0;
        JButton allMoviesButton = CreateButton("Movies", 0, y, 250, 50);

        allMoviesButton.setFont(Fonts.getInstance().font.deriveFont(26f));
        panel.add(allMoviesButton);

        allMoviesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClearPage(true);

                JButton filterButton = new JButton();
                filterButton.setBounds(950, 7, 60, 45);

                filterButton.setForeground(Color.WHITE);

                menuBar.add(filterButton);
                menuBar.filterButton = filterButton;
                String workingDirectory = System.getProperty("user.dir");
                Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "filter.png");

                ImageIcon originalIcon = new ImageIcon(path.toString());

                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(
                        originalIcon.getIconWidth() / 6,
                        originalIcon.getIconHeight() / 6,
                        Image.SCALE_SMOOTH);

                filterButton.setIcon(new ImageIcon(scaledImage));

                menuBar.repaint();

                filterButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!filterPanel.isVisible()) {
                            filterPanel.setVisible(true);
                            rightMenuBar.setVisible(false);

                            layeredPane.revalidate();
                            layeredPane.repaint();
                        } else {
                            filterPanel.setVisible(false);

                            layeredPane.revalidate();
                            layeredPane.repaint();

                        }

                    }
                });

                ResetFilterPanel();
                List<Object> objects = new ArrayList<Object>();
                objects.addAll(IMDB.getInstance().getProductions());
                lastObjectList = new GraphicObjectList(objects, 1);

            }
        });
        y += 50;

        JButton allActorsButton = CreateButton("Actors", 0, y, 250, 50);
        allActorsButton.setFont(Fonts.getInstance().font.deriveFont(26f));
        panel.add(allActorsButton);
        allActorsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClearPage(true);

                List<Object> objects = new ArrayList<Object>();

                objects.addAll(IMDB.getInstance().getActors());

                lastObjectList = new GraphicObjectList(objects, 1);

            }
        });
        y += 50;

        User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
        if (user.getAccountType() == AccountType.Admin) {

            JButton allUsersButton = CreateButton("Users", 0, y, 250, 50);
            allUsersButton.setFont(Fonts.getInstance().font.deriveFont(26f));
            panel.add(allUsersButton);
            allUsersButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ShowAllUsers();
                }
            });
            y += 50;
        }

        JButton favoritesButton = CreateButton("My favorites", 0, y, 250, 50);
        favoritesButton.setFont(Fonts.getInstance().font.deriveFont(26f));
        panel.add(favoritesButton);
        favoritesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClearPage(true);

                List<Object> objects = new ArrayList<Object>();

                objects.addAll(IMDB.getInstance().getCurrentLoggedInUser().getFavorites());
                lastObjectList = new GraphicObjectList(objects, 0);
                lastObjectList.isFavoriteList = true;

            }
        });
        y += 50;

        if (user.getAccountType() == AccountType.Admin || user.getAccountType() == AccountType.Contributor) {

            JButton allContributionsButton = CreateButton("My Contributions", 0, y, 250, 50);
            allContributionsButton.setFont(Fonts.getInstance().font.deriveFont(26f));
            panel.add(allContributionsButton);
            allContributionsButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ShowAllContributions();
                }
            });
            y += 50;
        }

        if (user.getAccountType() == AccountType.Contributor || user.getAccountType() == AccountType.Regular) {
            JButton myRequests = CreateButton("My Requests", 0, y, 250, 50);
            myRequests.setFont(Fonts.getInstance().font.deriveFont(26f));
            panel.add(myRequests);
            myRequests.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ClearPage(true);

                    List<Object> objects = new ArrayList<Object>();

                    if (user.getAccountType() == AccountType.Contributor)
                        objects.addAll(((Contributor<?>) user).getRequests());
                    else
                        objects.addAll(((Regular<?>) user).getRequests());

                    lastObjectList = new GraphicObjectList(objects, 3);

                    JButton addNewRequest = new JButton();
                    addNewRequest.setBounds(950, 7, 60, 45);

                    addNewRequest.setForeground(Color.WHITE);

                    menuBar.add(addNewRequest);
                    menuBar.addNewRequest = addNewRequest;

                    String workingDirectory = System.getProperty("user.dir");
                    Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons",
                            "request.png");

                    ImageIcon originalIcon = new ImageIcon(path.toString());

                    Image originalImage = originalIcon.getImage();
                    Image scaledImage = originalImage.getScaledInstance(
                            originalIcon.getIconWidth() / 5,
                            originalIcon.getIconHeight() / 5,
                            Image.SCALE_SMOOTH);

                    addNewRequest.setIcon(new ImageIcon(scaledImage));

                    addNewRequest.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            if (requestPanel != null) {
                                GUIAppFlow.layeredPane.remove(GUIAppFlow.requestPanel);
                                GUIAppFlow.requestPanel = null;
                                GUIAppFlow.layeredPane.revalidate();
                                GUIAppFlow.layeredPane.repaint();
                            } else {

                                RequestPanel requestPanel = new RequestPanel(null, null);
                                JScrollPane scrollPane = new JScrollPane(requestPanel);
                                scrollPane.setBounds(300, 100, 600, 400);
                                GUIAppFlow.AddToPage(scrollPane, 1);
                                GUIAppFlow.requestPanel = scrollPane;

                                layeredPane.revalidate();
                                layeredPane.repaint();
                            }

                        }
                    });

                    menuBar.repaint();

                }
            });
            y += 50;
        }

        if (user.getAccountType() == AccountType.Admin || user.getAccountType() == AccountType.Contributor) {
            JButton requestsToResolve = CreateButton("Solve requests", 0, y, 250, 50);
            requestsToResolve.setFont(Fonts.getInstance().font.deriveFont(26f));
            panel.add(requestsToResolve);
            requestsToResolve.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ClearPage(true);

                    List<Object> objects = new ArrayList<Object>();

                    Staff<?> staff = (Staff<?>) user;
                    objects.addAll(staff.getAssignedRequests());

                    lastObjectList = new GraphicObjectList(objects, 4);

                }
            });
            y += 50;
        }

        JButton myProfileButton = CreateButton("My Profile", 0, y, 250, 50);
        myProfileButton.setFont(Fonts.getInstance().font.deriveFont(26f));
        panel.add(myProfileButton);
        myProfileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClearPage(true);
                MyUserInfo myUserInfoPanel = new MyUserInfo();
                JScrollPane scrollPane = new JScrollPane(myUserInfoPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setBounds(0, 0, 1200, 710);
                GUIAppFlow.AddToPage(scrollPane, 1);
                GUIAppFlow.currentPage = scrollPane;

            }
        });
        y += 50;

        JButton logoutButton = CreateButton("Logout", 0, y, 250, 50);
        logoutButton.setFont(Fonts.getInstance().font.deriveFont(26f));
        panel.add(logoutButton);
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Logout();
            }
        });
        y += 50;

        JButton homeButton = new JButton();
        menuBar.homeButton = homeButton;
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "imdb3.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 41,
                originalIcon.getIconHeight() / 47,
                Image.SCALE_SMOOTH);

        homeButton.setIcon(new ImageIcon(scaledImage));
        homeButton.setBounds(110, 7, 90, 45);
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homeButton.setBackground(new Color(255, 225, 10, 255));

        homeButton.setFocusPainted(false);
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                if (!isMouseOverMenuPanel(e.getLocationOnScreen())) {
                    homeButton.setBackground(new Color(255, 255, 225, 255));

                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                homeButton.setBackground(new Color(255, 225, 10, 255));

            }
        });

        menuBar.add(homeButton);
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClearPage(true);
                CreateMainPage();

            }
        });

        menuBar.repaint();

        JButton addMovieButton = CreateButton("Add Movie", 0, 0, 250, 50);
        addSelectorMenu.add(addMovieButton);
        addMovieButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HelperFunctions.CreateNewPage(null, 3);

                addSelectorMenu.setVisible(false);
                layeredPane.revalidate();
                layeredPane.repaint();

            }
        });

        JButton addSeriesButton = CreateButton("Add Series", 0, 50, 250, 50);
        addSelectorMenu.add(addSeriesButton);
        addSeriesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                HelperFunctions.CreateNewPage(null, 4);

                addSelectorMenu.setVisible(false);
                layeredPane.revalidate();
                layeredPane.repaint();

            }
        });

        JButton addActorButton = CreateButton("Add Actor", 0, 100, 250, 50);
        addSelectorMenu.add(addActorButton);
        addActorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HelperFunctions.CreateNewPage(null, 5);
                addSelectorMenu.setVisible(false);
                layeredPane.revalidate();
                layeredPane.repaint();

            }
        });

        JPanel filterPanelCreated = new JPanel();
        filterPanelCreated.setBackground(new Color(40, 40, 40, 150));
        filterPanelCreated.setLayout(null);
        layeredPane.add(filterPanelCreated, 2, 0);
        filterPanelCreated.setBounds(950, 0, 250, 800);
        filterPanel = filterPanelCreated;
        filterPanel.setVisible(false);

        JButton genreButton = CreateButton("  Genre", 0, 0, 250, 50);
        genreButton.setHorizontalAlignment(SwingConstants.LEFT);
        genreButton.setFont(new java.awt.Font("Fira Code", 1, 18));
        filterPanelCreated.add(genreButton);

        List<Genre> genres = Arrays.asList(Genre.values());
        y = 50;
        for (Genre genre : genres) {
            JCheckBox checkBox = new JCheckBox(genre.toString());
            checkBox.setBounds(20, y, 250, 25);
            checkBox.setBackground(new Color(0, 0, 0, 0));
            checkBox.setForeground(Color.WHITE);
            checkBox.setFont(new java.awt.Font("Fira Code", 1, 16));
            checkBox.setHorizontalAlignment(SwingConstants.LEFT);
            filterPanelCreated.add(checkBox);
            y += 25;

            checkBox.addItemListener(e -> {
                List<Production> productions = IMDB.getInstance().getProductions();
                List<Production> filteredProductions = new ArrayList<>();

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Genre checkBoxGenre = Genre.valueOf(checkBox.getText());
                    filter.addGenre(checkBoxGenre);
                    filteredProductions = filter.SortProductions(productions);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    Genre checkBoxGenre = Genre.valueOf(checkBox.getText());
                    filter.removeGenre(checkBoxGenre);
                    filteredProductions = filter.SortProductions(productions);
                }
                ClearPage(false);
                List<Object> objects = new ArrayList<Object>();
                objects.addAll(filteredProductions);
                lastObjectList = new GraphicObjectList(objects, 1);
            });

        }

        JButton sortByNumberOfRatings = CreateButton("  Ratings", 0, y, 250, 50);
        sortByNumberOfRatings.setHorizontalAlignment(SwingConstants.LEFT);
        sortByNumberOfRatings.setFont(new java.awt.Font("Fira Code", 1, 18));
        filterPanelCreated.add(sortByNumberOfRatings);

        sortByNumberOfRatings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                filter.setByNumberOfRatings(!filter.isByNumberOfRatings());
                if (filter.isByNumberOfRatings())
                    sortByNumberOfRatings.setBackground(new Color(100, 100, 100, 150));
                else
                    sortByNumberOfRatings.setBackground(new Color(45, 45, 45, 150));

                List<Production> productions = IMDB.getInstance().getProductions();
                List<Production> filteredProductions = filter.SortProductions(productions);
                ClearPage(false);
                List<Object> objects = new ArrayList<Object>();
                objects.addAll(filteredProductions);
                lastObjectList = new GraphicObjectList(objects, 1);
            }

            public void mouseExited(MouseEvent e) {

                if (filter.isByNumberOfRatings())
                    sortByNumberOfRatings.setBackground(new Color(100, 100, 100, 150));
                else
                    sortByNumberOfRatings.setBackground(new Color(45, 45, 45, 150));

                repaint();
            }
        });

        CreateMainPage();

        setVisible(true);

    }

    private void Logout() {
        ClearPage(true);
        IMDB.getInstance().getCurrentLoggedInUser().getNotifications().clear();
        IMDB.getInstance().setCurrentLoggedInUser(null);
        IMDB.getInstance().is_LoggedIn = false;
        this.dispose();
    }

    public static void ShowAllContributions() {
        ClearPage(true);

        JButton addContributionButton = new JButton();
        addContributionButton.setBounds(950, 7, 60, 45);

        addContributionButton.setForeground(Color.WHITE);

        menuBar.add(addContributionButton);
        menuBar.addContributionButton = addContributionButton;

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "addDatabase.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 16,
                originalIcon.getIconHeight() / 16,
                Image.SCALE_SMOOTH);

        addContributionButton.setIcon(new ImageIcon(scaledImage));

        addContributionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (addSelectorMenu.isVisible())
                    addSelectorMenu.setVisible(false);
                else {
                    rightMenuBar.setVisible(false);
                    addSelectorMenu.setVisible(true);
                }

                layeredPane.revalidate();
                layeredPane.repaint();

            }
        });

        menuBar.repaint();

        List<Object> objects = new ArrayList<Object>();
        Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
        objects.addAll(staff.getContributions());
        lastObjectList = new GraphicObjectList(objects, 0);

    }

    public static void ShowAllUsers() {
        ClearPage(true);

        JButton addUserButton = new JButton();
        addUserButton.setBounds(950, 7, 60, 45);

        addUserButton.setForeground(Color.WHITE);

        menuBar.add(addUserButton);
        menuBar.addUserButton = addUserButton;

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "add-user.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 14,
                originalIcon.getIconHeight() / 14,
                Image.SCALE_SMOOTH);

        addUserButton.setIcon(new ImageIcon(scaledImage));

        addUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                HelperFunctions.CreateNewPage(null, 2);

            }
        });

        menuBar.repaint();

        List<Object> objects = new ArrayList<Object>();
        objects.addAll(IMDB.getInstance().getUsers());
        lastObjectList = new GraphicObjectList(objects, 2);

    }

    public void Search(String text) {

        if (text.equals(""))
            return;
        ClearPage(true);

        List<Object> objects = AplicationFlow.search(text);

        lastObjectList = new GraphicObjectList(objects, 0);

    }

    public void ResetFilterPanel() {
        for (Component component : filterPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setSelected(false);

            }
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals("  Ratings")) {
                    button.setBackground(new Color(45, 45, 45, 150));
                }
            }
        }

        filter = new ProductionFilter();
    }

    public static void AddToPage(Component component, int layer) {
        layeredPane.add(component, layer, 0);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public static void ClearPage(boolean clearFilterButton) {

        if (clearFilterButton) {
            if (menuBar.filterButton != null) {
                menuBar.remove(menuBar.filterButton);
                menuBar.filterButton = null;
            }
        }
        if (menuBar.addUserButton != null) {
            menuBar.remove(menuBar.addUserButton);
            menuBar.addUserButton = null;
        }

        if (menuBar.addContributionButton != null) {
            menuBar.remove(menuBar.addContributionButton);
            menuBar.addContributionButton = null;
        }

        if (menuBar.addNewRequest != null) {
            menuBar.remove(menuBar.addNewRequest);
            menuBar.addNewRequest = null;
        }

        menuBar.repaint();

        Component[] components = layeredPane.getComponents();

        for (Component component : components) {
            if (component != rightMenuBar && component != filterPanel && component != addSelectorMenu) {
                layeredPane.remove(component);
            }
        }

        lastObjectList = null;

        if (commentPanel != null)
            layeredPane.remove(commentPanel);
        commentPanel = null;

        if (requestPanel != null)
            layeredPane.remove(requestPanel);
        requestPanel = null;

        layeredPane.revalidate();
        layeredPane.repaint();

    }

    private void CreateMainPage() {
        List<Production> productions = IMDB.getInstance().getProductions();
        List<String> productionTitles = new ArrayList<String>();

        for (Production production : productions) {
            if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Staff<?>) {
                Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
                if (staff.getContributions().contains(production))
                    continue;
            }

            if (!production.getPoster().equals("NoImage.png"))
                productionTitles.add(production.getTitle());
        }

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        int numberOfImagesToDisplay = 5;
        int width = 300, height = 450;
        for (int i = 0; i < numberOfImagesToDisplay; i++) {
            int randomIndex = new Random().nextInt(productionTitles.size());
            String randomTitle = productionTitles.get(randomIndex);
            Production prod = AplicationFlow.findProduction(productions, randomTitle);

            JLabel image = CreateImage(prod.getPoster(), 0, 0, width, height);

            image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    HelperFunctions.CreateNewPage(prod, 1);

                }
            });

            imagePanel.add(image);
            productionTitles.remove(randomIndex);
        }

        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setPreferredSize(new Dimension(950, 500));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setBounds(0, 20, 1200, 500);

        scrollPane.getHorizontalScrollBar().setMaximum(350);

        scrollPane.getHorizontalScrollBar().setValue(175);
        layeredPane.add(scrollPane);

        String welcomeString = "Welcome to IMDb, " + IMDB.getInstance().getCurrentLoggedInUser().getUsername() + "!";

        int welcomeTextPaneHeight = 50;
        if (!IMDB.getInstance().getCurrentLoggedInUser().getNotifications().isEmpty()) {
            new NotificationPanel();
            welcomeString += " You have " + IMDB.getInstance().getCurrentLoggedInUser().getNotifications().size()
                    + " new ";
            if (IMDB.getInstance().getCurrentLoggedInUser().getNotifications().size() == 1)
                welcomeString += "notification.";
            else
                welcomeString += "notifications.";
        } else {
            welcomeString += "\nYou have no new notifications.";
            welcomeTextPaneHeight = 100;

        }

        JTextPane welcomeTextPane = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER,
                welcomeString, 900, welcomeTextPaneHeight,
                Fonts.getInstance().font.deriveFont(30f));
        welcomeTextPane.setBounds(150, 550, 900, welcomeTextPaneHeight);

        layeredPane.add(welcomeTextPane);

    }

    class NotificationPanel extends JPanel {
        int currentNotificationIndex = 0;
        JLabel notificationLabel = null;

        public NotificationPanel() {
            setBackground(new Color(45, 45, 45, 0));
            setOpaque(true);
            setLayout(null);
            setBorder(BorderFactory.createLineBorder(new Color(45, 45, 45, 150), 3));
            setBounds(200, 610, 800, 60);

            JButton prevButton = new JButton("Prev");
            prevButton.setFont(Fonts.getInstance().font.deriveFont(20f));
            prevButton.setBounds(10, 15, 100, 30);
            add(prevButton);

            JButton nextButton = new JButton("Next");
            nextButton.setBounds(690, 15, 100, 30);
            nextButton.setFont(Fonts.getInstance().font.deriveFont(20f));
            add(nextButton);

            notificationLabel = new JLabel();
            notificationLabel.setFont(Fonts.getInstance().font.deriveFont(24f));
            notificationLabel.setBounds(125, 0, 550, 60);
            JScrollPane scrollPane = new JScrollPane(notificationLabel);
            notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scrollPane.setBounds(125, 0, 550, 60);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 45, 255), 3));
            add(scrollPane);
            updateNotificationLabel();

            prevButton.addActionListener(e -> {
                if (currentNotificationIndex > 0) {
                    currentNotificationIndex--;
                    updateNotificationLabel();
                }
            });

            nextButton.addActionListener(e -> {
                if (currentNotificationIndex < IMDB.getInstance().getCurrentLoggedInUser().getNotifications().size()
                        - 1) {
                    currentNotificationIndex++;
                    updateNotificationLabel();
                }
            });

            layeredPane.add(this);

        }

        private void updateNotificationLabel() {
            List<String> notifications = IMDB.getInstance().getCurrentLoggedInUser().getNotifications();
            if (!notifications.isEmpty()) {
                notificationLabel.setText(notifications.get(currentNotificationIndex));

                notificationLabel.revalidate();
                notificationLabel.repaint();

                layeredPane.revalidate();
                layeredPane.repaint();

            }
        }
    }

    private JButton CreateButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(45, 45, 45, 150));
        button.setForeground(Color.WHITE);
        button.setFont(new java.awt.Font("Fira Code", 1, 18));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(35, 35, 35)));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                button.setBackground(new Color(45, 45, 45, 200));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(new Color(45, 45, 45, 150));
                repaint();
            }
        });

        return button;
    }

    public static JLabel CreateImage(String name, int x, int y, int width, int height) {
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "posters");

        ImageIcon originalIcon = new ImageIcon(path.toString() + "/" + name);

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel label = new JLabel(scaledIcon);
        label.setBounds(x, y, width, height);

        Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
        Border glowBorder = BorderFactory.createLineBorder(Color.YELLOW, 3);
        label.setBorder(emptyBorder);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isMouseOverMenuPanel(e.getLocationOnScreen())) {
                    label.setBorder(glowBorder);
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBorder(emptyBorder);
                label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return label;
    }

    public static JLabel SimplyImage(String name, int x, int y, int width, int height) {
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input");

        ImageIcon originalIcon = new ImageIcon(path.toString() + "/" + name);

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel label1 = new JLabel(scaledIcon);
        if (y != -1 && x != -1)
            label1.setBounds(x, y, width, height);

        return label1;

    }

    private static boolean isMouseOverMenuPanel(Point mousePoint) {

        if (rightMenuBar == null || !rightMenuBar.isShowing())
            return false;

        Point menuPanelLocation = rightMenuBar.getLocationOnScreen();
        Rectangle menuPanelBounds = new Rectangle(menuPanelLocation.x, menuPanelLocation.y, rightMenuBar.getWidth(),
                rightMenuBar.getHeight());

        return menuPanelBounds.contains(mousePoint);
    }

}
