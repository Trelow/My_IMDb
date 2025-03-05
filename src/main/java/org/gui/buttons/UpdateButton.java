package org.gui.buttons;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import org.production.*;

import java.awt.*;

import org.actor.*;
import org.gui.UpdateMoviePanel;
import org.gui.UpdateSeriesPanel;
import org.gui.UpdateActorPanel;
import org.gui.GUIAppFlow;

public class UpdateButton extends JButton {

    public UpdateButton(Object object) {

        setForeground(Color.WHITE);
        setFont(new Font("Fira Code", Font.BOLD, 17));

        // add image to the button
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "update.png");

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
                // MainPage.ClearPage(true);
                JScrollPane scrollPane = null;
                if (object instanceof Movie) {
                    UpdateMoviePanel updateProductionPanel = new UpdateMoviePanel((Movie) object);
                    scrollPane = new JScrollPane(updateProductionPanel);
                    updateProductionPanel.scrollPane = scrollPane;
                } else if (object instanceof Series) {
                    UpdateSeriesPanel updateProductionPanel = new UpdateSeriesPanel((Series) object);
                    scrollPane = new JScrollPane(updateProductionPanel);
                    updateProductionPanel.scrollPane = scrollPane;
                } else if (object instanceof Actor) {
                    UpdateActorPanel updateActorPanel = new UpdateActorPanel((Actor) object);
                    scrollPane = new JScrollPane(updateActorPanel);
                    updateActorPanel.scrollPane = scrollPane;
                }

                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setBounds(0, 0, 1200, 710);
                GUIAppFlow.AddToPage(scrollPane, 2);
            }
        });

    }

}