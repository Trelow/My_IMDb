package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.user.*;
import org.imdb.*;

import java.awt.*;

import org.exceptions.InformationIncompleteException;
import org.gui.utils.HelperFunctions;

import org.utils.*;
import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.util.Calendar;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class AddUserPanel extends JPanel {
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField nameField = new JTextField();
    JTextField countryField = new JTextField();
    JComboBox<Integer> ageComboBox = new JComboBox<>();
    JComboBox<String> genderComboBox = new JComboBox<>();

    JComboBox<Integer> dayComboBox = new JComboBox<>();
    JComboBox<Integer> monthComboBox = new JComboBox<>();
    JComboBox<Integer> yearComboBox = new JComboBox<>();
    JComboBox<String> accountTypeComboBox = new JComboBox<>();
    JTextField usernameField = new JTextField();

    JButton addButton = new JButton();
    public JButton backButton = new JButton();

    public AddUserPanel(User<?> userToModify) {
        setLayout(new GridBagLayout());

        JTextPane emailPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Email:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(100, 50, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(emailPane, c);

        emailField.setPreferredSize(new Dimension(600, 35));
        emailField.setFont(new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(100, 300, 0, 0);
        add(emailField, c);

        JTextPane passwordJTextPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Password:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(150, 50, 0, 0);
        add(passwordJTextPane, c);

        passwordField.setPreferredSize(new Dimension(400, 35));
        passwordField.setFont(new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(150, 300, 0, 0);
        add(passwordField, c);

        JButton generatePasswordButton = new JButton();
        generatePasswordButton.setForeground(Color.WHITE);
        String workingDirectory = System.getProperty("user.dir");
        Path path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "dice.png");

        ImageIcon originalIcon = new ImageIcon(path.toString());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 16,
                originalIcon.getIconHeight() / 16,
                Image.SCALE_SMOOTH);

        generatePasswordButton.setIcon(new ImageIcon(scaledImage));
        generatePasswordButton.setIconTextGap(-15);
        c.insets = new Insets(145, 755, 0, 0);
        add(generatePasswordButton, c);

        generatePasswordButton.addActionListener(e -> {

            String password = GeneratePassword.generateSecurePassword();
            passwordField.setText(password);
        });

        JButton showPasswordButton = new JButton();
        c.insets = new Insets(145, 705, 0, 0);

        showPasswordButton.setForeground(Color.WHITE);

        path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "eye.png");

        originalIcon = new ImageIcon(path.toString());
        originalImage = originalIcon.getImage();
        scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 16,
                originalIcon.getIconHeight() / 16,
                Image.SCALE_SMOOTH);

        showPasswordButton.setIcon(new ImageIcon(scaledImage));
        showPasswordButton.setIconTextGap(-15);

        final boolean[] isPasswordVisible = { false };

        showPasswordButton.addActionListener(e -> {

            isPasswordVisible[0] = !isPasswordVisible[0];

            if (isPasswordVisible[0]) {

                passwordField.setEchoChar((char) 0);
            } else {

                passwordField.setEchoChar('•');
            }
        });
        add(showPasswordButton, c);

        JTextPane namePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Name:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(200, 50, 0, 0);
        add(namePane, c);

        nameField.setPreferredSize(new Dimension(300, 35));
        nameField.setFont(new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(200, 300, 0, 0);
        add(nameField, c);

        JTextPane countryPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Country:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(250, 50, 0, 0);
        add(countryPane, c);

        countryField.setPreferredSize(new Dimension(300, 35));
        countryField.setFont(new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(250, 300, 0, 0);
        add(countryField, c);

        JTextPane agePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Age:", 100, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(300, 50, 0, 0);
        add(agePane, c);

        for (int age = 1; age <= 100; age++) {
            ageComboBox.addItem(age);
        }
        c.insets = new Insets(300, 300, 0, 0);
        add(ageComboBox, c);

        JTextPane birthDatePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Birth date:", 250, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(350, 50, 0, 0);
        add(birthDatePane, c);

        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        c.insets = new Insets(350, 300, 0, 0);
        add(dayComboBox, c);

        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(month);
        }

        c.insets = new Insets(350, 380, 0, 0);
        add(monthComboBox, c);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear; year >= 1900; year--) {
            yearComboBox.addItem(year);
        }

        c.insets = new Insets(350, 460, 0, 0);
        add(yearComboBox, c);

        JTextPane accountTypePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Account type:", 250, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(400, 50, 0, 0);
        add(accountTypePane, c);

        accountTypeComboBox.addItem("Regular");
        accountTypeComboBox.addItem("Contributor");
        accountTypeComboBox.addItem("Admin");
        c.insets = new Insets(400, 300, 0, 0);
        add(accountTypeComboBox, c);

        JTextPane genderPane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Gender:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(450, 50, 0, 0);
        add(genderPane, c);

        genderComboBox.addItem("Male");
        genderComboBox.addItem("Female");
        genderComboBox.addItem("None");

        c.insets = new Insets(450, 300, 0, 0);
        add(genderComboBox, c);

        JTextPane usernamePane = HelperFunctions.CreateText(StyleConstants.ALIGN_LEFT, "Username:", 200, 100,
                new Font("Fira Code", Font.BOLD, 26));
        c.insets = new Insets(500, 50, 0, 0);
        add(usernamePane, c);

        usernameField.setPreferredSize(new Dimension(400, 35));
        usernameField.setFont(new Font("Fira Code", Font.PLAIN, 22));
        c.insets = new Insets(500, 300, 0, 0);
        add(usernameField, c);

        // add button to generate random username
        JButton generateUsernameButton = new JButton();
        generateUsernameButton.setForeground(Color.WHITE);
        workingDirectory = System.getProperty("user.dir");
        path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "dice.png");

        originalIcon = new ImageIcon(path.toString());
        originalImage = originalIcon.getImage();
        scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 16,
                originalIcon.getIconHeight() / 16,
                Image.SCALE_SMOOTH);

                generateUsernameButton.setIcon(new ImageIcon(scaledImage));
                generateUsernameButton.setIconTextGap(-15);
        c.insets = new Insets(497, 705, 0, 0);
        add(generateUsernameButton, c);

        // set this button visivle only then the field name is filled
        generateUsernameButton.setVisible(false);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (nameField.getText().split(" ").length > 1)
                    generateUsernameButton.setVisible(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (nameField.getText().split(" ").length <= 1)
                generateUsernameButton.setVisible(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                generateUsernameButton.setVisible(true);
                 if (nameField.getText().split(" ").length > 1)
                    generateUsernameButton.setVisible(true);
                else if (nameField.getText().split(" ").length <= 1)
                    generateUsernameButton.setVisible(false);
            }
        });


        generateUsernameButton.addActionListener(e -> { 
           
            String username = "";
            boolean isUnique = false;
          
            // get name of user
            String name = nameField.getText();
            // get the first name
            String[] names = name.split(" ");
            // add to username, but smaller case
            username += names[0].toLowerCase();
            username += "_";
            // get the last name
            String[] lastNames = names[names.length - 1].split(" ");
            // add to username, but smaller case
            username += lastNames[0].toLowerCase();
            // add random number
            int rand_num;
            do{
            rand_num = (int) (Math.random() * 1000);

            // check if username is unique
            for (User<?> user : IMDB.getInstance().getUsers()) {
                if (user.getUsername().equals(username)) {
                    isUnique = false;
                    break;
                } else {
                    isUnique = true;
                }
            }

        } while (!isUnique);

            username += "_" +rand_num;

            usernameField.setText(username);

        });


        if (userToModify == null) {

            path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "addIcon.png");
            originalIcon = new ImageIcon(path.toString());

            originalImage = originalIcon.getImage();
            scaledImage = originalImage.getScaledInstance(
                    (int) (originalIcon.getIconWidth() / 4.7),
                    (int) (originalIcon.getIconHeight() / 4.7),
                    Image.SCALE_SMOOTH);

            addButton.setIcon(new ImageIcon(scaledImage));

            addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        } else {

            path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "save.png");
            originalIcon = new ImageIcon(path.toString());

            originalImage = originalIcon.getImage();
            scaledImage = originalImage.getScaledInstance(
                    originalIcon.getIconWidth() / 12,
                    originalIcon.getIconHeight() / 12,
                    Image.SCALE_SMOOTH);

            addButton.setIcon(new ImageIcon(scaledImage));

            addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.insets = new Insets(15, 1045, 0, 0);
        }

        c.anchor = GridBagConstraints.NORTHWEST;
        add(addButton, c);

        addButton.addActionListener(e -> {
            try {
                if (emailField.getText().equals("") || passwordField.getPassword().length == 0)
                    throw new InformationIncompleteException("Credentials must not be empty");
            } catch (InformationIncompleteException ex) {
                JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nameField.getText().equals("") || countryField.getText().equals("")
                    || usernameField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "All fields must be filled", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            for (User<?> user : IMDB.getInstance().getUsers()) {
                if (user.getUsername().equals(usernameField.getText()) && !user.equals(userToModify)) {
                    JOptionPane.showMessageDialog(null, "Username already exists", "Error",
                            JOptionPane.ERROR_MESSAGE);
                     return;
                }
            }

          //  if(userToModify == null)
            for (User<?> user : IMDB.getInstance().getUsers()) {
                if (user.getEmail().equals(emailField.getText()) && !user.equals(userToModify)) {
                    JOptionPane.showMessageDialog(null, "Email already exists", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String password = new String(passwordField.getPassword());
            Credentials credentials = new Credentials(emailField.getText(), password);
            LocalDateTime dateOfBirth = null;
            try {
                dateOfBirth = LocalDateTime.of((Integer) yearComboBox.getSelectedItem(),
                        (Integer) monthComboBox.getSelectedItem(), (Integer) dayComboBox.getSelectedItem(), 0, 0);
            } catch (DateTimeException exception) {
                JOptionPane.showMessageDialog(null, "Invalid date", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User<?> user = null;

            if (userToModify == null) {

                AccountType accountType = AccountType.valueOf((String) accountTypeComboBox.getSelectedItem());

                user = UserFactory.createUser((accountType));
            } else {
                user = userToModify;
            }

            user.setUsername(usernameField.getText());
            user.setExperience(0);
            user.setAccountType(AccountType.valueOf((String) accountTypeComboBox.getSelectedItem()));
            user.setInformation(new User.Information.InformationBuilder()
                    .credentials(credentials)
                    .name(nameField.getText())
                    .country(countryField.getText())
                    .age((Integer) ageComboBox.getSelectedItem())
                    .gender(genderComboBox.getSelectedItem().toString())
                    .dateOfBirth(dateOfBirth)
                    .build());

            if (userToModify == null) {

                Admin<?> admin = (Admin<?>) IMDB.getInstance().getCurrentLoggedInUser();
                admin.addUser(user);
                JOptionPane.showMessageDialog(null, "User added successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                GUIAppFlow.ShowAllUsers();
            } else {
                JOptionPane.showMessageDialog(null, "User information updated", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.anchor = GridBagConstraints.NORTHWEST;

        path = Paths.get(workingDirectory, "src", "main", "resources", "input", "buttons", "back.png");
        originalIcon = new ImageIcon(path.toString());

        originalImage = originalIcon.getImage();
        scaledImage = originalImage.getScaledInstance(
                originalIcon.getIconWidth() / 12,
                originalIcon.getIconHeight() / 12,
                Image.SCALE_SMOOTH);

        backButton.setIcon(new ImageIcon(scaledImage));

        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.insets = new Insets(15, 1100, 0, 0);
        c.anchor = GridBagConstraints.NORTHWEST;
        add(backButton, c);

    }

}