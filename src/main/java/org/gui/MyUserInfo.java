package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.user.*;
import org.imdb.*;

import java.awt.*;

import org.gui.utils.Fonts;
import org.gui.utils.HelperFunctions;

public class MyUserInfo extends JPanel {
        JTextPane passwordPane = null;
        boolean passwordVisible = false;

        public MyUserInfo() {
                setLayout(new GridBagLayout());

                User<?> user = IMDB.getInstance().getCurrentLoggedInUser();

                JTextPane emailPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Email: " + user.getEmail(),
                                700,
                                50,
                                Fonts.getInstance().font.deriveFont(30f));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                c.weighty = 1.0;
                c.insets = new Insets(100, 200, 0, 0);
                c.anchor = GridBagConstraints.NORTHWEST;
                add(emailPane, c);

                int width = user.getPassword().length() * 15;
                JTextPane passwordLabel = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Password: ",
                                190,
                                50,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(150, 200, 0, 0);
                add(passwordLabel, c);
                String passworString = "";
                for (int i = 0; i < user.getPassword().length(); i++) {
                        passworString += "* ";
                }
                passwordPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, passworString, width + 30,
                                50,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(150, 330, 0, 0);
                add(passwordPane, c);

                JButton showPasswordButton = new JButton();
                c.insets = new Insets(145, 350 + width, 0, 0);

                showPasswordButton.setForeground(Color.WHITE);
                String workingDirectory = System.getProperty("user.dir");
                Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "eye.png");

                ImageIcon originalIcon = new ImageIcon(path.toString());
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(
                                originalIcon.getIconWidth() / 16,
                                originalIcon.getIconHeight() / 16,
                                Image.SCALE_SMOOTH);

                showPasswordButton.setIcon(new ImageIcon(scaledImage));
                showPasswordButton.setIconTextGap(-15);
                showPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                showPasswordButton.addActionListener(e -> {

                        if (passwordVisible) {
                                passwordVisible = false;
                                String password = "";
                                for (int i = 0; i < user.getPassword().length(); i++) {
                                        password += "* ";
                                }
                                passwordPane.setText(password);
                        } else {
                                passwordVisible = true;
                                passwordPane.setText(user.getPassword());
                        }

                });
                add(showPasswordButton, c);

                JTextPane namePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Name: " + user.getName(),
                                700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(200, 200, 0, 0);
                add(namePane, c);

                JTextPane countryPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Country: " + user.getCountry(),
                                700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(250, 200, 0, 0);
                add(countryPane, c);

                JTextPane agePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Age: " + user.getAge(), 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(300, 200, 0, 0);
                add(agePane, c);

                String birthDate = String.format("%04d-%02d-%02d",
                                user.getDateOfBirth().getYear(),
                                user.getDateOfBirth().getMonthValue(),
                                user.getDateOfBirth().getDayOfMonth());
                JTextPane birthDatePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Birth date: " + birthDate, 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(350, 200, 0, 0);
                add(birthDatePane, c);

                JTextPane accountTypePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Account type: " + user.getAccountType().toString(), 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(400, 200, 0, 0);
                add(accountTypePane, c);

                JTextPane genderTypePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Gender: " + user.getGender().toString(), 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(450, 200, 0, 0);
                add(genderTypePane, c);

                JTextPane usernamePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Username: " + user.getUsername(), 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(500, 200, 0, 0);
                add(usernamePane, c);

                String experienceString = "";
                if (user.getExperience() == Integer.MAX_VALUE) {
                        experienceString = "∞";
                } else {
                        experienceString = String.valueOf(user.getExperience());
                }
                JTextPane experiencePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT,
                                "Experience: " + experienceString, 700,
                                100,
                                Fonts.getInstance().font.deriveFont(30f));
                c.insets = new Insets(550, 200, 0, 0);
                add(experiencePane, c);

        }
}
