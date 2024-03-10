package com.elleined.studentform.login;

import com.elleined.studentform.SQLiteConnection;
import com.elleined.studentform.UsernameValidator;
import com.elleined.studentform.registration.RegisterForm;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import com.elleined.studentform.app.StudentForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

@Value
public class LoginForm implements UsernameValidator {
    @Data
    static class LoginEntity {
        private String username;
        private String password;
        private LoginEntity() {

        }
    }
    static LoginForm instance;
    final static HashMap<String, String> loginInfoMap = new HashMap<>();
    @Getter(AccessLevel.NONE) LoginGUI loginGUI = LoginGUI.getInstance();
    @Getter(AccessLevel.NONE) LoginEntity ENTITY = new LoginEntity();
    @Getter(AccessLevel.NONE) StudentForm studentForm = StudentForm.getInstance();

    private LoginForm() {
        loginGUI.getTfUsername().setText(GMAIL);
        loginGUI.getBtnLogin().addActionListener(this::btnLoginPressed);
        loginGUI.getBtnRegister().addActionListener(this::btnRegisterPressed);
        loginGUI.getCbSeePassword().addItemListener(this::cbSeePasswordPressed);

        // when login is hit table will show
        loginGUI.getBtnLogin().addActionListener(studentForm::refreshTable);
    }

    @Override
    public boolean checkEmptyUsername(String username) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Username must not be empty!");
            loginGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEndsWithAtUsername(String username) {
        if (!username.endsWith(GMAIL)) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Username must end with @gmail.com! \nExample: juandelacruz@gmail.com");
            loginGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStartsWithAtUsername(String username) {
        if (username.startsWith("@")) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Enter a valid username! \nExample: juandelacruz@gmail.com");
            loginGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsername() {
        String username = loginGUI.getTfUsername().getText().replaceAll(" ", "");
        if (checkEmptyUsername(username)) return true;
        if (checkEndsWithAtUsername(username)) return true;
        if (checkStartsWithAtUsername(username)) return true;
        ENTITY.setUsername(username);
        return false;
    }

    private boolean validateUsername() {
        String pass = loginInfoMap.get(ENTITY.getUsername());
        if (!loginInfoMap.containsKey(ENTITY.getUsername())) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Login Failed! Incorrect Username.");
            loginGUI.getTfUsername().requestFocusInWindow();
            return true;
        }
        ENTITY.setPassword(pass);
        return false;
    }

    private boolean validatePassword() {
        String fetchPassword = ENTITY.getPassword();
        String enteredPassword = new String(loginGUI.getTfPassword().getPassword());
        if (enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Enter your password!");
            loginGUI.getTfPassword().requestFocusInWindow();
            return true;
        }
        if (!fetchPassword.equals(enteredPassword)) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Login Failed! Incorrect Password.");
            loginGUI.getTfPassword().setText(null);
            loginGUI.getTfPassword().requestFocusInWindow();
            return true;
        }
        return false;
    }

    private void btnRegisterPressed(ActionEvent btnRegisterPressed) {
        int selected = JOptionPane.showConfirmDialog(loginGUI.getFrame(), "Do you want to register?", "Select an Option", JOptionPane.YES_NO_OPTION);
        if (selected != JOptionPane.YES_OPTION) {
            return;
        }
        loginGUI.getFrame().setVisible(false);
        RegisterForm.getInstance().getRegisterGUI().setVisible(true);
    }

    private void btnLoginPressed(ActionEvent btnLoginPressed) {
        fetchLoginInfo(); // Fetching login credential for validation
        if (checkUsername()) return;
        if (validateUsername()) return;
        if (validatePassword()) return;
        JOptionPane.showMessageDialog(loginGUI.getFrame(), "     Login Success!");
        loginGUI.getFrame().setVisible(false);
        studentForm.getStudentGUI().setVisible(true);
    }

    private void cbSeePasswordPressed(ItemEvent cbSeePasswordPressed) {
        Icon see = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/see.png")));
        Icon unSee = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/unsee.png")));
        Icon icon = loginGUI.getCbSeePassword().isSelected() ? unSee : see;
        loginGUI.getCbSeePassword().setIcon(icon);
        loginGUI.getTfPassword().setEchoChar(loginGUI.getCbSeePassword().isSelected() ? 0 : '*');
    }

    private void fetchLoginInfo() {
        try (Connection conn = SQLiteConnection.studentFormDB()) {
            PreparedStatement query = conn.prepareStatement("SELECT REGISTRATION_USERNAME, REGISTRATION_PASSWORD FROM REGISTRATION_DETAILS");
            ResultSet loginInfo = query.executeQuery();
            while (loginInfo.next()) {
                loginInfoMap.put(loginInfo.getString("REGISTRATION_USERNAME"), loginInfo.getString("REGISTRATION_PASSWORD"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Error Occurred! Fetching Failed!");
        }
    }

    public JFrame getLoginFormFrame() {
        return loginGUI.getFrame();
    }

    public static LoginForm getInstance() {
        if (instance == null) instance = new LoginForm();
        return instance;
    }
}
