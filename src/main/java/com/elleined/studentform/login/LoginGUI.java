package com.elleined.studentform.login;
import lombok.Value;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Value
class LoginGUI {
    private static LoginGUI instance;
    JFrame frame = new JFrame("Login Form");
    JTextField tfUsername;
    JPasswordField tfPassword;
    JButton btnLogin;
    JButton btnRegister;
    JCheckBox cbSeePassword;

    private LoginGUI() {
        // Labels
        JLabel lblIntro = new JLabel("Login Form");
        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        // Designing Labels
        lblIntro.setFont(new Font("Serif", Font.BOLD, 20));
        // TextFields
        tfUsername = new JTextField();
        tfPassword = new JPasswordField();
        // Buttons
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        // CheckBox
        cbSeePassword = new JCheckBox(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/see.png"))));
        // Placing Labels
        lblIntro.setBounds(150, 10, 100, 40);
        lblUsername.setBounds(30, 60, 100, 40);
        lblPassword.setBounds(30, 110, 100, 40);
        // Placing TextFields
        tfUsername.setBounds(110, 60, 250, 40);
        tfPassword.setBounds(110, 110, 250, 40);
        // Placing Buttons
        btnLogin.setBounds(110, 160, 100, 40);
        btnRegister.setBounds(260, 160, 100, 40);
        // Placing CheckBox
        cbSeePassword.setBounds(360, 120, 20, 20);
        // Adding Components in JFrame
        frame.add(lblIntro);
        frame.add(lblUsername); frame.add(tfUsername);
        frame.add(lblPassword);frame.add(tfPassword);frame.add(cbSeePassword);
        frame.add(btnLogin);frame.add(btnRegister);

        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static LoginGUI getInstance() {
        if (instance == null) instance = new LoginGUI();
        return instance;
    }
}
