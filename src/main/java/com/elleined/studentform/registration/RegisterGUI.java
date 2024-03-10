package com.elleined.studentform.registration;
import lombok.Value;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Value
class RegisterGUI {
    static RegisterGUI instance;
    JFrame frame;
    JTextField tfName;
    JTextField tfUsername;
    JPasswordField tfPass1;
    JPasswordField tfPass2;
    JTextField tfPhoneNumber;
    JButton btnRegister;
    JButton btnReset;
    JButton btnCancel;
    JCheckBox cbSeePass1;
    JCheckBox cbSeePass2;

    private RegisterGUI() {
        frame = new JFrame("Register Form");
        // Labels
        JLabel lblIntro = new JLabel("Register Form");
        JLabel lblName = new JLabel("Name :");
        JLabel lblUsername = new JLabel("Username :");
        JLabel lblPass1 = new JLabel("Password :");
        JLabel lblPass2 = new JLabel("Confirm Password :");
        JLabel lblPhoneNumber = new JLabel("Phone :");
        // Designing Labels
        lblIntro.setFont(new Font("Serif", Font.BOLD, 30));
        lblName.setFont(new Font("Century", Font.BOLD, 20));
        lblUsername.setFont(new Font("Century", Font.BOLD, 20));
        lblPass1.setFont(new Font("Century", Font.BOLD, 20));
        lblPass2.setFont(new Font("Century", Font.BOLD, 20));
        lblPhoneNumber.setFont(new Font("Century", Font.BOLD, 20));
        // TextFields
        tfName = new JTextField();
        tfUsername = new JTextField();
        tfPass1 = new JPasswordField();
        tfPass2 = new JPasswordField();
        tfPhoneNumber = new JTextField();
        // Buttons
        btnRegister = new JButton("Register");
        btnReset = new JButton("Reset");
        btnCancel = new JButton("Cancel");
        // CheckBox
        cbSeePass1 = new JCheckBox(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/see.png"))));
        cbSeePass2 = new JCheckBox(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/see.png"))));
        // Placing Labels
        lblIntro.setBounds(20, 10, 200, 50);
        lblName.setBounds(140, 70, 100, 20);
        lblUsername.setBounds(100, 110, 140, 20);
        lblPass1.setBounds(105, 150, 140, 20);
        lblPass2.setBounds(20, 190, 200, 20);
        lblPhoneNumber.setBounds(140, 230, 100, 20);
        // Placing TextFields
        tfName.setBounds(230, 65, 250, 35);
        tfUsername.setBounds(230, 105, 250, 35);
        tfPass1.setBounds(230, 145, 250, 35);
        tfPass2.setBounds(230, 185, 250, 35);
        tfPhoneNumber.setBounds(230, 225, 250, 35);
        // Placing Buttons
        btnRegister.setBounds(20, 280, 100, 50);
        btnReset.setBounds(200, 280, 100, 50);
        btnCancel.setBounds(380, 280, 100, 50);
        // Placing CheckBox
        cbSeePass1.setBounds(480, 155, 20, 20);
        cbSeePass2.setBounds(480, 195, 20, 20);
        // Adding Components to frame
        frame.add(lblIntro);
        frame.add(lblName);frame.add(tfName);
        frame.add(lblUsername);frame.add(tfUsername);
        frame.add(lblPass1);frame.add(tfPass1);frame.add(cbSeePass1);
        frame.add(lblPass2);frame.add(tfPass2);frame.add(cbSeePass2);
        frame.add(lblPhoneNumber);frame.add(tfPhoneNumber);
        frame.add(btnRegister);frame.add(btnReset);frame.add(btnCancel);
        // Setting Frame
        frame.setLayout(null);
        frame.setSize(520, 390);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static RegisterGUI getInstance() {
        if (instance == null) instance = new RegisterGUI();
        return instance;
    }

    JFrame getFrame() {
        return frame;
    }
    JTextField getTfName() {
        return tfName;
    }
    JTextField getTfUsername() {
        return tfUsername;
    }
    JPasswordField getTfPass1() {
        return tfPass1;
    }
    JPasswordField getTfPass2() {
        return tfPass2;
    }
    JTextField getTfPhoneNumber() {
        return tfPhoneNumber;
    }
    JButton getBtnRegister() {
        return btnRegister;
    }
    JButton getBtnReset() {
        return btnReset;
    }
    JButton getBtnCancel() {
        return btnCancel;
    }
    JCheckBox getCbSeePass1() {
        return cbSeePass1;
    }
    JCheckBox getCbSeePass2() {
        return cbSeePass2;
    }
}
