package org.gui;

import javax.swing.*;

import javax.swing.text.StyleConstants;

import org.production.*;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

public class ReviewPanel extends JPanel {
    private JTextPane textPane;
    private int currentPage;
    private List<Rating> ratings = new ArrayList<Rating>();

    public ReviewPanel(List<Rating> ratings) {
        this.ratings = ratings;
        setLayout(new BorderLayout());

        setPreferredSize(new Dimension(600, 100));

        setFont(new java.awt.Font("Fira Code", 1, 25));
        textPane = new JTextPane();
        textPane.setText("");
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setPreferredSize(new Dimension(500, 100));
        textPane.setFont(new Font("Fira Code", Font.PLAIN, 16));
        StyledDocument documentStyle = textPane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_LEFT);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        add(new JScrollPane(textPane), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton previousButton = new JButton("Prev");

        JButton nextButton = new JButton("Next");

        previousButton.addActionListener(e -> showPreviousPage());

        nextButton.addActionListener(e -> showNextPage());

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 420, 0, 0));

        currentPage = 0;
        showPage(currentPage);
    }

    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            showPage(currentPage);
        }
    }

    private void showNextPage() {
        if (currentPage < ratings.size() - 1) {
            currentPage++;
            showPage(currentPage);
        }
    }

    private void showPage(int page) {
        String rating = "";

        rating += "User:\t\t" + ratings.get(page).getUsername() + "\n";

        rating += "Score:\t\t" + ratings.get(page).getScore() + "\n";

        rating += "Comment:\t" + ratings.get(page).getComment();
        textPane.setText(rating);

    }
}
