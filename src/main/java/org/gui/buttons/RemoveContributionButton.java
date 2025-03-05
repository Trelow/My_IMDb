package org.gui.buttons;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.production.*;
import org.user.*;
import org.imdb.*;

import org.gui.GUIAppFlow;
import org.actor.*;
import java.awt.*;

public class RemoveContributionButton extends JButton {

    public RemoveContributionButton(Object object) {

        setForeground(Color.WHITE);
        setFont(new Font("Fira Code", Font.BOLD, 17));

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons",
                "removeContribution.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledImage));
        setIconTextGap(-15);

        // add listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this production?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    try {
                        if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Staff<?>) {
                            Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();

                            if (object instanceof Production) {
                                Production production = (Production) object;
                                staff.removeProductionSystem(production.getTitle());

                                JOptionPane.showMessageDialog(null, "Production was removed with success", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);

                                GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);
                                if (GUIAppFlow.menuBar.filterButton != null)
                                    GUIAppFlow.menuBar.filterButton.setVisible(true);

                                if (GUIAppFlow.menuBar.addContributionButton != null)
                                    GUIAppFlow.menuBar.addContributionButton.setVisible(true);

                                if (GUIAppFlow.lastObjectList != null)
                                    GUIAppFlow.lastObjectList.RemoveObject(object);

                                GUIAppFlow.layeredPane.revalidate();
                                GUIAppFlow.layeredPane.repaint();
                            } else if (object instanceof Actor) {
                                Actor actor = (Actor) object;
                                staff.removeActorSystem(actor.getName());

                                // Show success message
                                JOptionPane.showMessageDialog(null, "Actor was removed with success", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);

                                // Update UI elements
                                GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);
                                if (GUIAppFlow.menuBar.filterButton != null)
                                    GUIAppFlow.menuBar.filterButton.setVisible(true);

                                if (GUIAppFlow.menuBar.addContributionButton != null)
                                    GUIAppFlow.menuBar.addContributionButton.setVisible(true);

                                if (GUIAppFlow.lastObjectList != null)
                                    GUIAppFlow.lastObjectList.RemoveObject(object);

                                GUIAppFlow.layeredPane.revalidate();
                                GUIAppFlow.layeredPane.repaint();
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }
}