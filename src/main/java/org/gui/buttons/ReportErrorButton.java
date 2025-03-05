package org.gui.buttons;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.user.*;
import org.imdb.*;

import org.gui.GUIAppFlow;
import java.awt.*;
import org.gui.RequestPanel;

public class ReportErrorButton extends JButton {
    Staff<?> contributor = null;

    public ReportErrorButton(Object object) {

        setForeground(Color.WHITE);
        setFont(new Font("Fira Code", Font.BOLD, 17));

        // add image to the button
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "error.png");

        // Load the original icon
        ImageIcon originalIcon = new ImageIcon(path.toString());

        // Scale the image to be 4 times smaller
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                (int) (originalIcon.getIconWidth() / 4.7),
                (int) (originalIcon.getIconHeight() / 4.7),
                Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        setIcon(new ImageIcon(scaledImage));
        // set icon more to the left
        setIconTextGap(-15);

        // found the contributor for the object

        for (User<?> user : IMDB.getInstance().getUsers()) {
            if (user instanceof Staff<?>) {
                Staff<?> staff = (Staff<?>) user;
                if (staff.getContributions().contains(object)) {
                    this.contributor = staff;
                    break;
                }
            }
        }

        // add listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GUIAppFlow.requestPanel != null) {
                    GUIAppFlow.layeredPane.remove(GUIAppFlow.requestPanel);
                    GUIAppFlow.requestPanel = null;
                    GUIAppFlow.layeredPane.revalidate();
                    GUIAppFlow.layeredPane.repaint();
                } else {
                    if (GUIAppFlow.commentPanel != null) {
                        GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
                        GUIAppFlow.commentPanel = null;
                        GUIAppFlow.layeredPane.revalidate();
                        GUIAppFlow.layeredPane.repaint();
                    }

                    RequestPanel requestPanel = new RequestPanel(contributor, object);
                    JScrollPane scrollPane = new JScrollPane(requestPanel);
                    scrollPane.setBounds(300, 100, 600, 400);
                    GUIAppFlow.AddToPage(scrollPane, 2);
                    GUIAppFlow.requestPanel = scrollPane;

                    GUIAppFlow.layeredPane.revalidate();
                    GUIAppFlow.layeredPane.repaint();
                }
            }
        });

    }
}