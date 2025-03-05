package org.gui.buttons;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.gui.GUIAppFlow;
import org.gui.CommentPanel;

import java.awt.*;

public class CommentButton extends JButton {
    public CommentButton(Object object) {
        setForeground(Color.WHITE);
        setFont(new Font("Fira Code", Font.BOLD, 17));
        // add image to the button
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "comment.png");

        // Load the original icon
        ImageIcon originalIcon = new ImageIcon(path.toString());

        // Scale the image to be 4 times smaller
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        setIcon(new ImageIcon(scaledImage));
        // set icon more to the left
        setIconTextGap(-15);

        // add listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GUIAppFlow.commentPanel != null) {
                    GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
                    GUIAppFlow.commentPanel = null;
                    GUIAppFlow.layeredPane.revalidate();
                    GUIAppFlow.layeredPane.repaint();
                } else {
                    if (GUIAppFlow.requestPanel != null) {
                        GUIAppFlow.layeredPane.remove(GUIAppFlow.requestPanel);
                        GUIAppFlow.requestPanel = null;
                        GUIAppFlow.layeredPane.revalidate();
                        GUIAppFlow.layeredPane.repaint();
                    }

                    CommentPanel commentPanel = new CommentPanel(object);
                    JScrollPane scrollPane = new JScrollPane(commentPanel);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.setBounds(300, 100, 600, 400);
                    GUIAppFlow.AddToPage(scrollPane, 2);
                    GUIAppFlow.commentPanel = scrollPane;
                }
            }
        });

    }
}
