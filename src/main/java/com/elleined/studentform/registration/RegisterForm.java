package com.elleined.studentform.registration;

import com.elleined.studentform.SQLiteConnection;
import com.elleined.studentform.UsernameValidator;
import com.elleined.studentform.login.LoginForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@Value
public class RegisterForm implements UsernameValidator {
    static class RegisterEntity {
        // try mo inner class address pa
        private String name;
        private String username;
        private String pass1;
        private String password;
        private long phoneNumber;
        private String getName() {
            return name;
        }
        private void setName(String name) {
            this.name = name;
        }
        private String getUsername() {
            return username;
        }
        private void setUsername(String username) {
            this.username = username;
        }
        private String getPass1() {
            return pass1;
        }
        private void setPass1(String pass1) {
            this.pass1 = pass1;
        }
        private String getPassword() {
            return password;
        }
        private void setPassword(String password) {
            this.password = password;
        }
        private long getPhoneNumber() {
            return phoneNumber;
        }
        private void setPhoneNumber(long phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
    static RegisterForm instance;
    final static HashMap<String, String> loginInfoMap = new HashMap<>();
    @Getter(AccessLevel.NONE) RegisterGUI registerGUI = RegisterGUI.getInstance();
    @Getter(AccessLevel.NONE) RegisterEntity REGISTER = new RegisterEntity();

    private RegisterForm() {
        registerGUI.getTfUsername().setText(GMAIL);
        registerGUI.getBtnRegister().addActionListener(this::register);
        registerGUI.getBtnReset().addActionListener(this::reset);
        registerGUI.getBtnCancel().addActionListener(this::cancel);
        registerGUI.getCbSeePass1().addItemListener(this::showPassword);
        registerGUI.getCbSeePass2().addItemListener(this::showPassword);
    }

    private boolean checkName() {
        String name = registerGUI.getTfName().getText().strip();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Name cannot be empty!");
            registerGUI.getTfName().requestFocus();
            return true;
        }
        REGISTER.setName(name);
        return false;
    }

    @Override
    public boolean checkEmptyUsername(String username) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Username must not be empty!");
            registerGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEndsWithAtUsername(String username) {
        if (!username.endsWith(GMAIL)) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Username must end with @gmail.com! \nExample: juandelacruz@gmail.com");
            registerGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStartsWithAtUsername(String username) {
        if (username.startsWith("@")) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Enter a valid username! \nExample: juandelacruz@gmail.com");
            registerGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsername() {
        String username = registerGUI.getTfUsername().getText()
                .strip()
                .replaceAll(" ", "");
        if (checkEmptyUsername(username)) return true;
        if (checkEndsWithAtUsername(username)) return true;
        if (checkStartsWithAtUsername(username)) return true;
        if (checkIfExistUsername(username)) return true;
        REGISTER.setUsername(username);
        return false;
    }

    private boolean checkIfExistUsername(String username) {
        if (loginInfoMap.containsKey(username)) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Username already been used! cannot use " + username);
            registerGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    private boolean checkPassEmpty(String pass) {
        return pass.isEmpty();
    }

    private boolean checkPassSpace(String pass) {
        return pass.contains(" ");
    }

    private boolean checkPass1() {
        String pass1 = new String(registerGUI.getTfPass1().getPassword());
        if (checkPassEmpty(pass1)) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Password cannot be empty!");
            registerGUI.getTfPass1().requestFocus();
            return true;
        }
        if (checkPassSpace(pass1)) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Password cannot contain spaces!");
            registerGUI.getTfPass1().requestFocus();
            return true;
        }
        if (pass1.length() < 8) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Password must be 8 characters long!");
            registerGUI.getTfPass1().requestFocus();
            return true;
        }
        REGISTER.setPass1(pass1);
        return false;
    }

    private boolean validatePassword() {
        if (checkPass1()) return true;
        String pass2 = new String(registerGUI.getTfPass2().getPassword());
        if (!REGISTER.getPass1().equals(pass2)) {
            JOptionPane.showMessageDialog(registerGUI.getFrame(), "Password not match! Try again.");
            registerGUI.getTfPass1().requestFocus();
            registerGUI.getTfPass1().setText(null);
            registerGUI.getTfPass2().setText(null);
            return true;
        }
        REGISTER.setPassword(pass2);
        return false;
    }

    private boolean checkPhoneNumber() {
            String phoneNumber = registerGUI.getTfPhoneNumber().getText().strip();
            long number;
            try {
                number = Long.parseLong(phoneNumber);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(registerGUI.getFrame(), "Phone Number can only contain numbers! and must not be empty!");
                registerGUI.getTfPhoneNumber().requestFocus();
                return true;
            }
            if (phoneNumber.length() != 11) {
                JOptionPane.showMessageDialog(registerGUI.getFrame(), "Phone number must 11 digits long!");
                registerGUI.getTfPhoneNumber().requestFocus();
                return true;
            }
            if (!phoneNumber.startsWith("09")) {
                JOptionPane.showMessageDialog(registerGUI.getFrame(), "Phone number must starts with 09!");
                registerGUI.getTfPhoneNumber().requestFocus();
                return true;
            }
            REGISTER.setPhoneNumber(number);
        return false;
    }

    private void insert() {
        try (Connection conn = SQLiteConnection.studentFormDB()){
            PreparedStatement query = conn.prepareStatement("INSERT INTO REGISTRATION_DETAILS VALUES (?, ?, ?, ?)");
            query.setString(1, REGISTER.getName());
            query.setString(2, REGISTER.getUsername());
            query.setString(3, REGISTER.getPassword());
            query.setLong(4, REGISTER.getPhoneNumber());
            query.executeUpdate();
            clearFields();
        } catch (SQLException e) {
            System.out.println("Error Occurred! Insertion Failed.");
        }
    }

    private void showPassword(ItemEvent cbSeePassChecked) {
        Icon icon1 = selectIcon(registerGUI.getCbSeePass1());
        Icon icon2 = selectIcon(registerGUI.getCbSeePass2());
        char seePass1 = seePassword(registerGUI.getCbSeePass1());
        char seePass2 = seePassword(registerGUI.getCbSeePass2());
        if (cbSeePassChecked.getSource() == registerGUI.getCbSeePass1()) {
            registerGUI.getCbSeePass1().setIcon(icon1);
            registerGUI.getTfPass1().setEchoChar(seePass1);
        }
        if (cbSeePassChecked.getSource() == registerGUI.getCbSeePass2()) {
            registerGUI.getCbSeePass2().setIcon(icon2);
            registerGUI.getTfPass2().setEchoChar(seePass2);
        }
    }

    private void register(ActionEvent btnRegisterPressed) {
        fetchLoginInfo(); // for validation
        if (checkName()) return;
        if (checkUsername()) return;
        if (validatePassword()) return;
        if (checkPhoneNumber()) return;
        insert();
        JOptionPane.showMessageDialog(registerGUI.getFrame(), "Registration Success!");
        registerGUI.getFrame().setVisible(false);
        LoginForm.getInstance().getLoginFormFrame().setVisible(true);
    }

    private void fetchLoginInfo() {
        try (Connection conn = SQLiteConnection.studentFormDB()){
            PreparedStatement query = conn.prepareStatement("SELECT REGISTRATION_USERNAME, REGISTRATION_PASSWORD FROM REGISTRATION_DETAILS");
            ResultSet userAndPass = query.executeQuery();
            while (userAndPass.next()) {
                loginInfoMap.put(userAndPass.getString("REGISTRATION_USERNAME"), userAndPass.getString("REGISTRATION_PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Fetch Failed!");
        }
    }

    private void cancel(ActionEvent btnCancelPressed) {
        int selected = JOptionPane.showConfirmDialog(registerGUI.getFrame(), "Are you sure do you want to cancel registration?", "Select an Option", JOptionPane.YES_NO_OPTION);
        if (selected != JOptionPane.YES_OPTION) {
            return;
        }
        registerGUI.getFrame().setVisible(false);
        LoginForm.getInstance().getLoginFormFrame().setVisible(true);
    }

    private void reset(ActionEvent btnResetPressed) {
        clearFields();
    }

    private void clearFields() {
        registerGUI.getTfName().setText(null);
        registerGUI.getTfUsername().setText(null);
        registerGUI.getTfPass1().setText(null);
        registerGUI.getTfPass2().setText(null);
        registerGUI.getTfPhoneNumber().setText(null);
        registerGUI.getTfName().requestFocus();
    }

    private char seePassword(JCheckBox checkBox) {
        return checkBox.isSelected() ? 0 : '*';
    }

    private Icon selectIcon(JCheckBox checkBox) {
        Icon see = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/see.png")));
        Icon unSee = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/unsee.png")));
        return checkBox.isSelected() ? unSee : see;
    }

    public static RegisterForm getInstance() {
        if (instance == null) instance = new RegisterForm();
        return instance;
    }

    public JFrame getRegisterGUI() {
        return registerGUI.getFrame();
    }

}