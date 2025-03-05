package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

import org.user.*;
import org.imdb.*;

import java.awt.*;

import org.production.Production;
import org.production.Rating;
import java.time.LocalDateTime;

public class UpdateUserPanel extends AddUserPanel {
    public JScrollPane scrollPane;
    public JButton deleteButton = new JButton();

    public UpdateUserPanel(User<?> user) {
        super(user);

        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        nameField.setText(user.getName());
        countryField.setText(user.getCountry());
        ageComboBox.setSelectedItem(user.getAge());
        genderComboBox.setSelectedItem(user.getGender());

        LocalDateTime birthDate = user.getDateOfBirth();

        dayComboBox.setSelectedItem(birthDate.getDayOfMonth());
        monthComboBox.setSelectedItem(birthDate.getMonthValue());
        yearComboBox.setSelectedItem(birthDate.getYear());
        accountTypeComboBox.setSelectedItem(user.getAccountType());
        usernameField.setText(user.getUsername());
        accountTypeComboBox.setSelectedItem(user.getAccountType().toString());

        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons",
                "removeContribution.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);

        deleteButton.setIcon(new ImageIcon(scaledImage));

        deleteButton.setIconTextGap(-15);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 990, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;

        add(deleteButton, c);

        if (user.equals(IMDB.getInstance().getCurrentLoggedInUser())) {
            deleteButton.setVisible(false);
        }

        deleteButton.addActionListener(e -> {

            int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {

                User<?> currentUser = IMDB.getInstance().getCurrentLoggedInUser();
                Admin<?> admin = (Admin<?>) currentUser;
                admin.removeUser(user);

                // for each rating in production, if the user is the one who rated, remove it
                for (Production production : IMDB.getInstance().getProductions()) {
                    for (Rating rating : production.getRatings()) {
                        if (rating.getUser().equals(user)) {
                            production.getRatings().remove(rating);
                            break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "User removed successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.ShowAllUsers();
            }

        });
    }

}