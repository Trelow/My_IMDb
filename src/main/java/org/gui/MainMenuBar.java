package org.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainMenuBar extends JPanel {
    public JButton filterButton = null;
    public JButton addUserButton = null;
    public JButton addContributionButton = null;
    public JButton homeButton = null;
    public JButton addNewRequest = null;

    public JTextField searchBar = null;

    public MainMenuBar(GUIAppFlow mainPage) {

        setBackground(new Color(40, 40, 40));

        setLayout(null);

        searchBar = new JTextField();
        searchBar.setBounds(220, 7, 650, 45);

        searchBar.setBackground(new Color(60, 60, 60));
        searchBar.setFont(new Font("Fira Code", Font.BOLD, 17));

        add(searchBar);

        JButton searchButton = new JButton();
        searchButton.setForeground(Color.WHITE);
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "search-260.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 16,
                originalIcon.getIconHeight() / 16,
                Image.SCALE_SMOOTH);

        searchButton.setIcon(new ImageIcon(scaledImage));

        searchButton.setBounds(880, 7, 60, 45);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mainPage.Search(searchBar.getText());
            }
        });

        add(searchButton);

        MenuButton menuButton = new MenuButton(mainPage);
        menuButton.setBounds(1100, 7, 60, 45);
        add(menuButton);

    }
}

class MenuButton extends JButton {

    public MenuButton(GUIAppFlow mainPage) {

        setForeground(Color.WHITE);
        setFont(new Font("Fira Code", Font.BOLD, 17));

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "menuIcon.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 4,
                originalIcon.getIconHeight() / 4,
                Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledImage));

        setIconTextGap(-15);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!GUIAppFlow.rightMenuBar.isVisible()) {

                    GUIAppFlow.rightMenuBar.setVisible(true);
                    GUIAppFlow.filterPanel.setVisible(false);

                } else {

                    GUIAppFlow.rightMenuBar.setVisible(false);

                }

                GUIAppFlow.addSelectorMenu.setVisible(false);

                mainPage.revalidate();
                mainPage.repaint();

            }
        });

    }

}
