package org.gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

import org.gui.utils.Fonts;
import org.imdb.IMDB;
import org.user.Credentials;

import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField tfEmail;
    private JPasswordField pfPassword;

    public LoginForm() {
        setUndecorated(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome");
        setSize(600, 475);
        setLocationRelativeTo(null);
        setLayout(null);

        CustomTitleBar titleBar = new CustomTitleBar(this);
        add(titleBar);
        titleBar.setBounds(0, 0, getWidth(), 30);

        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "input", "buttons",
                "imdb2.png");
        ImageIcon image = new ImageIcon(path.toString());
        if (image.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.out.println("Image not found");
        }
        JLabel imageLabel = new JLabel(image);
        imageLabel.setIcon(image);
        add(imageLabel);
        imageLabel.setBounds(0, -140, getWidth(), getHeight());

        JLabel label = new JLabel("Email");
        label.setBounds(72, 223, 200, 35);
        label.setFont(Fonts.getInstance().font);
        label.setForeground(Color.white);
        add(label);

        tfEmail = new JTextField();
        tfEmail.setBounds(250, 220, 300, 40);
        tfEmail.setFont(new Font("Fira Code", Font.BOLD, 18));
        add(tfEmail);

        JLabel label2 = new JLabel("Password");
        label2.setBounds(72, 303, 200, 35);
        label2.setFont(Fonts.getInstance().font);
        label2.setForeground(Color.white);
        add(label2);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(250, 300, 300, 40);
        pfPassword.setFont(new Font("Fira Code", Font.BOLD, 26));
        add(pfPassword);

        JButton loginButton = new JButton("Login");

        loginButton.setFont(Fonts.getInstance().font);

        loginButton.setBounds(200, 400, 200, 50);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());
                Credentials credentials = new Credentials(email, password);

                IMDB.getInstance().findUser(credentials);

                if (IMDB.getInstance().getCurrentLoggedInUser() != null) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Logged in as " + email, "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid credentials, try again", "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loginButton);

        setVisible(true);
    }

}
