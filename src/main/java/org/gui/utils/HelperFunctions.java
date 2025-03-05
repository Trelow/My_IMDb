package org.gui.utils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.production.*;
import org.user.*;
import org.actor.*;

import org.gui.GUIAppFlow;
import org.gui.ActorDetailsPage;
import org.gui.ProductionDetailsPage;
import org.gui.AddUserPanel;
import org.gui.AddMoviePanel;
import org.gui.AddSeriesPanel;
import org.gui.AddActorPanel;
import org.gui.UpdateUserPanel;

public class HelperFunctions {

    static public JTextPane CreateText(int alignment, String text, int width, int height, Font font) {
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setPreferredSize(new Dimension(width, height));
        textPane.setFont(font);
        StyledDocument documentStyle = textPane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, alignment);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
        return textPane;
    }

    static public Image ScaleImage(String path, int widthFactor, int heightFactor) {
        // Scale the image to be 4 times smaller
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / widthFactor,
                originalIcon.getIconHeight() / heightFactor,
                Image.SCALE_SMOOTH);
        return scaledImage;
    }

    static public void CreateNewPage(Object object, int type) {
        JPanel detailsPage = null;
        JButton backButton = null;
        if (type == 1) {
            if (object instanceof Actor) {
                detailsPage = new ActorDetailsPage((Actor) object);
                backButton = ((ActorDetailsPage) detailsPage).backButton;
            } else if (object instanceof Production) {
                detailsPage = new ProductionDetailsPage((Production) object);
                backButton = ((ProductionDetailsPage) detailsPage).backButton;
            } else if (object instanceof User<?>) {
                detailsPage = new UpdateUserPanel((User<?>) object);
                backButton = ((UpdateUserPanel) detailsPage).backButton;
            }
        } else if (type == 2) {
            detailsPage = new AddUserPanel(null);
            backButton = ((AddUserPanel) detailsPage).backButton;
        } else if (type == 3) {
            // add movie
            detailsPage = new AddMoviePanel(null);
            backButton = ((AddMoviePanel) detailsPage).backButton;
        } else if (type == 4) {
            // add series
            detailsPage = new AddSeriesPanel(null);
            backButton = ((AddSeriesPanel) detailsPage).backButton;
        } else if (type == 5) {
            // add actor
            detailsPage = new AddActorPanel(null);
            backButton = ((AddActorPanel) detailsPage).backButton;
        }

        // ProductionDetailsPage detailsPage = new ProductionDetailsPage((Production)
        // object);
        JScrollPane scrollPane = new JScrollPane(detailsPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 0, 1200, 710);
        GUIAppFlow.AddToPage(scrollPane, 1);

        GUIAppFlow.currentPage = scrollPane;
        GUIAppFlow.filterPanel.setVisible(false);
        if (GUIAppFlow.menuBar.filterButton != null)
            GUIAppFlow.menuBar.filterButton.setVisible(false);
        if (GUIAppFlow.menuBar.addUserButton != null)
            GUIAppFlow.menuBar.addUserButton.setVisible(false);
        if (GUIAppFlow.menuBar.addContributionButton != null)
            GUIAppFlow.menuBar.addContributionButton.setVisible(false);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GUIAppFlow.currentPage != null) {
                    GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);
                    GUIAppFlow.currentPage = null;
                }

                if (GUIAppFlow.menuBar.filterButton != null)
                    GUIAppFlow.menuBar.filterButton.setVisible(true);
                if (GUIAppFlow.commentPanel != null) {
                    GUIAppFlow.layeredPane.remove(GUIAppFlow.commentPanel);
                    GUIAppFlow.commentPanel = null;
                }

                if (GUIAppFlow.menuBar.addUserButton != null)
                    GUIAppFlow.menuBar.addUserButton.setVisible(true);

                if (GUIAppFlow.menuBar.addContributionButton != null)
                    GUIAppFlow.menuBar.addContributionButton.setVisible(true);

                GUIAppFlow.layeredPane.revalidate();
                GUIAppFlow.layeredPane.repaint();
            }
        });
    }

}
