package org.gui;

import java.util.List;

import javax.swing.*;

import org.gui.utils.HelperFunctions;

import org.actor.*;

public class UpdateActorPanel extends AddActorPanel {
    public JScrollPane scrollPane;

    public UpdateActorPanel(Actor actor) {
        super(actor);

        titleField.setText(actor.getName());
        descriptionArea.setText(actor.getDescription());

        filmographyPanel.removeAll();

        List<Filmography> filmography = actor.getFilmography();
        for (Filmography filmographyItem : filmography) {
            FilmographyPanel filmographyRow = new FilmographyPanel(filmographyPanel);
            filmographyRow.productionName.setText(filmographyItem.getName());
            filmographyRow.productionType.setSelectedItem(filmographyItem.getType());
        }

        backButton.addActionListener(e -> {

            GUIAppFlow.layeredPane.remove(scrollPane);
            GUIAppFlow.layeredPane.remove(GUIAppFlow.currentPage);

            HelperFunctions.CreateNewPage(actor, 1);
            GUIAppFlow.layeredPane.repaint();
            GUIAppFlow.layeredPane.revalidate();

        });

    }
}
