package org.gui.listRenderers;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.gui.utils.Fonts;
import org.gui.utils.HelperFunctions;

import org.production.*;
import org.actor.*;
import java.awt.*;

public class ImageRenderer extends JPanel implements ListCellRenderer<Object> {
    private JTextPane titleLabel;
    private JTextPane type;
    private JTextPane description;
    private JLabel imageLabel;
    private JTextPane filmographyPane;
    private JTextPane filmographyMoviesPane;

    public ImageRenderer() {
        setLayout(new GridBagLayout());

        titleLabel = new JTextPane();
        description = new JTextPane();
        type = new JTextPane();
        imageLabel = new JLabel();
        filmographyPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Filmography:", 400, 100,
                new Font("Fira Code", Font.BOLD, 24));
        filmographyPane.setVisible(false);

        filmographyMoviesPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "", 400, 200,
                new Font("Fira Code", Font.BOLD, 16));
        filmographyMoviesPane.setVisible(false);

        titleLabel.setEditable(false);
        titleLabel.setOpaque(false);
        titleLabel.setPreferredSize(new Dimension(500, 100));
        titleLabel.setFont(Fonts.getInstance().font.deriveFont(30f));
        titleLabel.setForeground(Color.WHITE);

        StyledDocument documentStyle = titleLabel.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        description.setEditable(false);
        description.setOpaque(false);

        type.setEditable(false);
        type.setOpaque(false);
        type.setPreferredSize(new Dimension(100, 100));
        type.setFont(Fonts.getInstance().font.deriveFont(24f));
        type.setForeground(Color.WHITE);

        documentStyle = description.getStyledDocument();
        centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        description.setFont(Fonts.getInstance().font.deriveFont(20f));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(35, 35, 35)));

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        if (value instanceof Production) {
            Production production = (Production) value;
            titleLabel.setText(production.getTitle());
            description.setFont(new java.awt.Font("Fira Code", 0, 20));
            description.setPreferredSize(new Dimension(500, 200));
            description.setText(production.getDescription());

            filmographyPane.setVisible(false);
            filmographyMoviesPane.setVisible(false);

            // Set the image icon

            String workingDirectory = System.getProperty("user.dir");
            Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "resized");
            ImageIcon originalIcon = new ImageIcon(path.toString() + "/" + production.getPoster());

            imageLabel.setIcon(originalIcon);

            if (value instanceof Movie) {
                type.setText("Movie");
            } else if (value instanceof Series) {
                type.setText("Series");
            }

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(20, 220, 0, 0);
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.anchor = GridBagConstraints.NORTH;
            add(titleLabel, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.anchor = GridBagConstraints.WEST;
            add(imageLabel, c);

            c.insets = new Insets(0, 220, 0, 0);
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.SOUTH;
            add(description, c);

            c.insets = new Insets(50, 1000, 0, 0);
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.SOUTH;
            add(type, c);

        } else if (value instanceof Actor) {
            Actor actor = (Actor) value;
            titleLabel.setText(actor.getName());
            titleLabel.setPreferredSize(new Dimension(500, 50));
            imageLabel.setIcon(null);

            StyledDocument documentStyle = titleLabel.getStyledDocument();
            SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
            documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

            // move title to the left
            GridBagConstraints c = new GridBagConstraints();

            c.insets = new Insets(15, 0, 0, 0);
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.anchor = GridBagConstraints.CENTER;
            add(titleLabel, c);

        }

        if (isSelected) {
            setBackground(new Color(50, 50, 50));

        } else {
            setBackground(new Color(30, 30, 30));
        }

        return this;
    }
}