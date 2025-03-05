package org.gui.buttons;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.production.*;
import org.user.*;
import org.imdb.*;
import org.gui.utils.*;
import org.actor.*;
import org.gui.GUIAppFlow;

import java.awt.*;

public class FavoritesStarButton extends JButton {
    boolean isInFavorite = false;
    ImageIcon[] sprites = new ImageIcon[2];

    public FavoritesStarButton(boolean isInFavorite, Object favoriteObject) {
        this.isInFavorite = isInFavorite;
        String workingDirectory = System.getProperty("user.dir");
        Path path1 = null, path2 = null;
        path1 = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "star1.png");
        path2 = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "star2.png");

        sprites[0] = new ImageIcon(HelperFunctions.ScaleImage(path1.toString(), 24, 24));
        sprites[1] = new ImageIcon(HelperFunctions.ScaleImage(path2.toString(), 24, 24));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (isInFavorite)
            setIcon(sprites[1]);
        else
            setIcon(sprites[0]);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (FavoritesStarButton.this.isInFavorite) {
                    setIcon(sprites[0]);
                    // remove from favorites
                    User<?> user = IMDB.getInstance().getCurrentLoggedInUser();

                    if (favoriteObject instanceof Actor) {
                        Actor actor = (Actor) favoriteObject;
                        user.removeActorFromFavorites(actor);
                    } else {
                        Production production = (Production) favoriteObject;
                        user.removeProductionFromFavorites(production);
                    }
                    if (GUIAppFlow.lastObjectList != null && GUIAppFlow.lastObjectList.isFavoriteList)
                        GUIAppFlow.lastObjectList.RemoveObject(favoriteObject);

                } else {
                    setIcon(sprites[1]);
                    // add to favorites
                    User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
                    if (favoriteObject instanceof Actor) {
                        Actor actor = (Actor) favoriteObject;
                        user.addActorToFavorites(actor);
                    } else {
                        Production production = (Production) favoriteObject;
                        user.addProductionToFavorites(production);
                    }
                    if (GUIAppFlow.lastObjectList != null && GUIAppFlow.lastObjectList.isFavoriteList)
                        GUIAppFlow.lastObjectList.AddObject(favoriteObject);
                }

                FavoritesStarButton.this.isInFavorite = !FavoritesStarButton.this.isInFavorite;
            }
        });
    }
}
