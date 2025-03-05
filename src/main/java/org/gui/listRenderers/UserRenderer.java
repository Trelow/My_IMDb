package org.gui.listRenderers;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.gui.utils.Fonts;
import org.gui.utils.HelperFunctions;

import org.user.*;
import java.awt.*;

public class UserRenderer extends JPanel implements ListCellRenderer<Object> {
    private JTextPane titleLabel;

    public UserRenderer() {
        setLayout(new GridBagLayout());

        titleLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_CENTER, "", 1000, 50,
                Fonts.getInstance().font.deriveFont(26f));

        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(35, 35, 35)));

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        String title = "";
        if (value instanceof User) {
            User<?> user = (User<?>) value;
            title = user.getUsername();
        }

        titleLabel.setText(title);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        add(titleLabel, c);

        if (isSelected) {
            setBackground(new Color(50, 50, 50));

        } else {
            setBackground(new Color(30, 30, 30));
        }

        return this;
    }
}