package org.gui.listRenderers;

import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.text.StyleConstants;
import org.request.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import org.gui.utils.Fonts;
import org.gui.utils.HelperFunctions;

public class RequestsRenderer extends JPanel implements ListCellRenderer<Object> {
    private JTextPane typeLabel;
    JTextPane dateLabel;
    JTextPane usernameLabel;
    JTextPane descriptionLabel;
    boolean isSolveRequest = false;

    public RequestsRenderer(int type) {
        setLayout(new GridBagLayout());

        typeLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "", 300, 50,
                Fonts.getInstance().font.deriveFont(26f));

        dateLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "", 200, 100,

                Fonts.getInstance().font.deriveFont(26f));

        if (type == 4)
            isSolveRequest = true;
        else
            isSolveRequest = false;
        usernameLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "", 425, 50,
                Fonts.getInstance().font.deriveFont(26f));

        descriptionLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "", 500, 150,
                Fonts.getInstance().font.deriveFont(26f));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(30, 500, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;

        add(typeLabel, c);

        c.insets = new Insets(45, 10, 0, 0);
        add(dateLabel, c);

        c.insets = new Insets(125, 10, 0, 0);
        add(usernameLabel, c);

        c.insets = new Insets(125, 500, 0, 0);
        add(descriptionLabel, c);

        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(35, 35, 35)));

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        Request request = (Request) value;
        RequestType type = request.getType();
        if (typeLabel != null)
            typeLabel.setText(type.toString());
        LocalDateTime date = request.getCreationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = date.format(formatter);
        if (dateLabel != null)
            dateLabel.setText(formattedDate);

        String username = "";
        if (isSolveRequest)
            username = "From: " + request.getUserUsername();
        else
            username = "To: " + request.getResolverUsername();

        if (usernameLabel != null)
            usernameLabel.setText(username);

        // Get description
        String description = request.getDescription();
        if (descriptionLabel != null)
            descriptionLabel.setText(description);

        if (isSelected) {
            setBackground(new Color(50, 50, 50));

        } else {
            setBackground(new Color(30, 30, 30));
        }

        return this;
    }
}