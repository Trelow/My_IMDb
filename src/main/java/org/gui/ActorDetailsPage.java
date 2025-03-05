package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.user.*;
import org.imdb.*;
import org.actor.*;

import java.awt.*;
import org.gui.utils.HelperFunctions;
import org.gui.buttons.*;;

public class ActorDetailsPage extends JPanel {
    JTextPane titlePane;
    JTextPane descriptionPane;
    JTextPane filmographyPane;
    JTextPane filmographyMoviesPane;
    public JButton backButton = new JButton();
    JButton favoriteButton = new JButton();
    JButton commentButton = new JButton();
    int y = 50;

    public ActorDetailsPage(Actor actor) {
        setLayout(new GridBagLayout());

        // Add title
        titlePane = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER, actor.getName(), 640, 100,
                new Font("Fira Code", Font.BOLD, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(y, 0, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(titlePane, c);
        y += 100;

        int height = getContentHeight(600, actor.getDescription(), new Font("Fira Code", Font.PLAIN, 20));

        if (IMDB.getInstance().getCurrentLoggedInUser() instanceof Staff) {
            Staff<?> staff = (Staff<?>) IMDB.getInstance().getCurrentLoggedInUser();
            if (staff.getContributions().contains(actor)) {
                // add update button
                UpdateButton updateButton = new UpdateButton(actor);
                c.insets = new Insets(25, 990, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(updateButton, c);

                // add also remove button
                RemoveContributionButton removeContributionButton = new RemoveContributionButton(actor);
                c.insets = new Insets(25, 935, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(removeContributionButton, c);

            }

        }

        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular
                || IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
            boolean isInContributions = false;
            if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Contributor) {
                Contributor<?> contributor = (Contributor<?>) IMDB.getInstance().getCurrentLoggedInUser();
                if (contributor.getContributions().contains(actor)) {
                    isInContributions = true;
                }

            }
            if (!isInContributions) {
                ReportErrorButton reportErrorButton = new ReportErrorButton(actor);
                c.insets = new Insets(25, 990, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(reportErrorButton, c);
            }
        }

        descriptionPane = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER, actor.getDescription(), 600, height,
                new Font("Fira Code", Font.PLAIN, 20));
        c.insets = new Insets(y, 0, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(descriptionPane, c);
        y += height + 50;

        // Add filmography
        filmographyPane = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER, "Filmography:", 300, 100,
                new Font("Fira Code", Font.BOLD, 30));

        c.insets = new Insets(y, 0, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(filmographyPane, c);
        y += 60;

        List<Filmography> filmography = actor.getFilmography();
        String filmogrphyStr = "";
        for (int i = 0; i < filmography.size(); i++) {
            Filmography pair = filmography.get(i);
            filmogrphyStr += (i + 1) + ". " + pair.getName() + " (" + pair.getType() + ")\n";
        }
        height = 25 * filmography.size() + 30;
        filmographyMoviesPane = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER, filmogrphyStr, 700, height,
                new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(y, 0, 0, 0);
        c.anchor = GridBagConstraints.NORTH;
        add(filmographyMoviesPane, c);
        y += height + 50;

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "back.png");
        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);

        backButton.setIcon(new ImageIcon(scaledImage));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.insets = new Insets(25, 1100, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(backButton, c);

        User<?> user = IMDB.getInstance().getCurrentLoggedInUser();
        boolean isActorInFavorites = false;
        for (Object favorite : user.getFavorites()) {
            if (favorite instanceof Actor) {
                Actor favoriteActor = (Actor) favorite;
                if (favoriteActor.getName().equals(actor.getName())) {
                    isActorInFavorites = true;
                    break;
                }
            }
        }

        FavoritesStarButton favoritesStarButton = new FavoritesStarButton(isActorInFavorites, actor);
        c.insets = new Insets(25, 1045, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(favoritesStarButton, c);

        if (IMDB.getInstance().getCurrentLoggedInUser().getAccountType() == AccountType.Regular) {
            CommentButton commentButton = new CommentButton(actor);
            c.insets = new Insets(25, 990, 0, 0);
            c.anchor = GridBagConstraints.NORTHWEST;
            add(commentButton, c);
        }

    }

    public static int getContentHeight(int width, String content, Font font) {
        JEditorPane dummyEditorPane = new JEditorPane();
        dummyEditorPane.setFont(font);
        dummyEditorPane.setSize(width, Short.MAX_VALUE);
        dummyEditorPane.setText(content);
        return dummyEditorPane.getPreferredSize().height;

    }

}
