package form.app;

import form.SQLiteConnection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Value
public class StudentForm {
    private static StudentForm instance;
    @Data
    private static class StudentEntity {
        private int id;
        private String name;
        private String sex;
        private int age;
        private String yearAndSection;

        public @NotNull String getYearAndSection() {
            return yearAndSection.toUpperCase();
        }

        public String getSex() {
            return sex.toUpperCase();
        }
    }
    StudentGUI studentGUI = StudentGUI.getGuiInstance();
    @Getter(AccessLevel.NONE) Set<Integer> idSet = new HashSet<>();
    @Getter(AccessLevel.NONE) StudentEntity STUDENT = new StudentEntity();

    private StudentForm() {
        // Everytime user do something table will refresh
        studentGUI.getBtnInsert().addActionListener(this::refreshTable);
        studentGUI.getBtnDelete().addActionListener(this::refreshTable);
        studentGUI.getBtnFinalUpdate().addActionListener(this::refreshTable);
        studentGUI.getBtnRead().addActionListener(this::refreshTable);
        studentGUI.getBtnInsert().addActionListener(this::insert);
        studentGUI.getBtnDelete().addActionListener(this::delete);
        studentGUI.getBtnUpdate().addActionListener(this::update);
        studentGUI.getBtnRefresh().addActionListener(this::refreshTable);
        studentGUI.getBtnFinalUpdate().addActionListener(this::finalUpdate);
    }

    private boolean checkIdEmpty() {
        if (studentGUI.getTfId().getText().isEmpty()) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "ID cannot be empty!");
            studentGUI.getTfId().setText(null);
            studentGUI.getTfId().requestFocus();
            return true;
        }
        return false;
    }

    private boolean checkIdRange() {
        int idField = Integer.parseInt(studentGUI.getTfId().getText());
        if (idField <= 0) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "ID must not be less than 0!");
            studentGUI.getTfId().setText(null);
            studentGUI.getTfId().requestFocus();
            return true;
        }
        if (idField > 250) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "ID must not be greater than 250!");
            studentGUI.getTfId().setText(null);
            studentGUI.getTfId().requestFocus();
            return true;
        }
        return false;
    }

    private boolean checkIdExist() {
        int idField;
        try {
            idField = Integer.parseInt(studentGUI.getTfId().getText());
            boolean anyMatch = getIdSet().stream().anyMatch(i -> i.equals(idField));
            if (anyMatch) {
                JOptionPane.showMessageDialog(studentGUI.getFrame(), "ID already exist in database!");
                studentGUI.getTfId().setText(null);
                studentGUI.getTfId().requestFocus();
                return true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Id must be a number!");
            studentGUI.getTfId().setText(null);
            studentGUI.getTfId().requestFocus();
            return true;
        }
        STUDENT.setId(idField);
        return false;
    }

    private boolean checkIdNotExist() {
        int idField;
        try {
            idField = Integer.parseInt(studentGUI.getTfId().getText());
            boolean noneMatch = getIdSet().stream().noneMatch(i -> i.equals(idField));
            if (noneMatch) {
                JOptionPane.showMessageDialog(studentGUI.getFrame(), "ID didn't exist in database!");
                studentGUI.getTfId().setText(null);
                studentGUI.getTfId().requestFocus();
                return true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Id must be a number!");
            studentGUI.getTfId().setText(null);
            studentGUI.getTfId().requestFocus();
            return true;
        }
        STUDENT.setId(idField);
        return false;
    }

    private boolean checkEmptyName() {
        String name = studentGUI.getTfName().getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Name must not be empty!");
            studentGUI.getTfName().setText(null);
            studentGUI.getTfName().requestFocus();
            return true;
        }
        STUDENT.setName(name);
        return false;
    }

    private boolean checkEmptyAge() {
        try {
            if (studentGUI.getTfAge().getText().isEmpty()) {
                JOptionPane.showMessageDialog(studentGUI.getFrame(), "Age cannot be empty!");
                studentGUI.getTfAge().setText(null);
                studentGUI.getTfAge().requestFocus();
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("checkAge Its Okay");
        }
        return false;
    }

    private boolean checkAgeRange() {
        int age = 0;
        try {
            age = Integer.parseInt(studentGUI.getTfAge().getText());
            Predicate<Integer> isValid = i -> i >= 18 && i <= 25;
            if (isValid.negate().test(age)) {
                JOptionPane.showMessageDialog(studentGUI.getFrame(), "Age must only be 18 to 25!");
                studentGUI.getTfAge().setText(null);
                studentGUI.getTfAge().requestFocus();
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Inside checkAge Its Okay");
        }
        STUDENT.setAge(age);
        return false;
    }

    private boolean checkEmptyYearAndSection() {
        if (studentGUI.getTfYearAndSection().getText().isEmpty()) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Year and section must not be empty!");
            studentGUI.getTfYearAndSection().setText(null);
            studentGUI.getTfYearAndSection().requestFocus();
            return true;
        }
        return false;
    }

    private boolean checkYearAndSectionLength() {
        if (studentGUI.getTfYearAndSection().getText().length() > 2) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Year and section must only be Example: 3D");
            studentGUI.getTfYearAndSection().setText(null);
            studentGUI.getTfYearAndSection().requestFocus();
            return true;
        }
        return false;
    }

    private boolean checkYearAndSectionFormat() {
        String year = studentGUI.getTfYearAndSection().getText();
        boolean yearNumber = Stream.of(1, 2, 3, 4).noneMatch(i -> year.startsWith(String.valueOf(i)));
        boolean sectionLetter = Stream.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
                .noneMatch(c -> year.endsWith(String.valueOf(c)) || year.endsWith(String.valueOf(c).toUpperCase()) );
        if (yearNumber || sectionLetter) {
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Year must only be between 1 to 4, Example: 3D!");
            studentGUI.getTfYearAndSection().setText(null);
            studentGUI.getTfYearAndSection().requestFocus();
            return true;
        }
        STUDENT.setYearAndSection(year);
        return false;
    }

    void insert(ActionEvent insertButtonPressed) {
        if (checkIdEmpty() || checkIdRange() || checkIdExist()) return;
        if (checkEmptyName()) return;
        if (checkEmptyAge() || checkAgeRange()) return;
        STUDENT.setSex(studentGUI.getRbMale().isSelected() ? studentGUI.getRbMale().getText() : studentGUI.getRbFemale().getText());
        if (checkEmptyYearAndSection() || checkYearAndSectionLength() || checkYearAndSectionFormat()) return;
        try (Connection conn = SQLiteConnection.myOwnGuiDbConnection()) {
            PreparedStatement query = conn.prepareStatement("INSERT INTO STUDENT_DETAILS VALUES (?, ?, ?, ?, ?)");
            query.setInt(1, STUDENT.getId());
            query.setString(2, STUDENT.getName());
            query.setString(3, STUDENT.getSex().toUpperCase());
            query.setInt(4, STUDENT.getAge());
            query.setString(5, STUDENT.getYearAndSection().toUpperCase());
            query.executeUpdate();
            studentGUI.clearFields();
            studentGUI.getTfId().requestFocus();
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Inserted Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Insertion Failed");
        }
        System.out.println(STUDENT);
    }

    void delete(ActionEvent deleteButtonPressed) {
        if (checkIdEmpty() || checkIdRange() || checkIdNotExist()) return;
        try (Connection conn = SQLiteConnection.myOwnGuiDbConnection()){
            PreparedStatement deleteQuery = conn.prepareStatement("DELETE FROM STUDENT_DETAILS WHERE STUDENT_ID=?");
            deleteQuery.setInt(1, STUDENT.getId());
            idSet.remove(STUDENT.getId());
            deleteQuery.executeUpdate();
            studentGUI.clearFields();
            studentGUI.getTfId().requestFocus();
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Deletion Success!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Deletion Failed");
        }
    }

    void update(ActionEvent updateButtonPressed) {
        JFrame checkBoxFrame = new JFrame("Update");

        JCheckBox nameCheckBox = new JCheckBox("Student Name");
        JCheckBox ageCheckBox  = new JCheckBox("Student Age");
        JCheckBox yearAndSectionCheckBox  = new JCheckBox("Year and Section");
        JButton updateSelectedButton = new JButton("Update Selected");

        updateSelectedButton.addActionListener(pressed -> {
            checkBoxFrame.setVisible(false);
            studentGUI.getRbFemale().setVisible(false);
            studentGUI.getRbMale().setVisible(false);
            studentGUI.hideButtons();
            studentGUI.getBtnFinalUpdate().setVisible(true);
            studentGUI.getTfName().setEnabled(nameCheckBox.isSelected());
            studentGUI.getTfAge().setEnabled(ageCheckBox.isSelected());
            studentGUI.getTfYearAndSection().setEnabled(yearAndSectionCheckBox.isSelected());
        });

        checkBoxFrame.add(nameCheckBox);
        checkBoxFrame.add(ageCheckBox);
        checkBoxFrame.add(yearAndSectionCheckBox);
        checkBoxFrame.add(updateSelectedButton);

        checkBoxFrame.setLayout(new GridLayout(4, 0));
        checkBoxFrame.setVisible(true);
        checkBoxFrame.setSize(200, 300);
        checkBoxFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    void finalUpdate(ActionEvent executeFinalUpdatePressed) {
        studentGUI.getTfId().requestFocus();
        if (checkIdEmpty() || checkIdRange() || checkIdNotExist()) return;
        if (studentGUI.getTfName().isEnabled() && checkEmptyName()) return;
        if (studentGUI.getTfAge().isEnabled() && (checkEmptyAge() || checkAgeRange())) return;
        if (studentGUI.getTfYearAndSection().isEnabled() && (checkEmptyYearAndSection() || checkYearAndSectionLength() || checkYearAndSectionFormat())) return;
        try (Connection conn = SQLiteConnection.myOwnGuiDbConnection()){
            PreparedStatement query = conn.prepareStatement("SELECT STUDENT_NAME, STUDENT_AGE, STUDENT_YEAR_SECTION FROM STUDENT_DETAILS WHERE STUDENT_ID=?");
            query.setInt(1, STUDENT.getId());
            ResultSet data = query.executeQuery();
            String name = null;
            int age = 0;
            String yearAndSection = null;
            while (data.next()) {
                name = data.getString(1);
                age = data.getInt(2);
                yearAndSection = data.getString(3);
            }
            String nameEntryText = studentGUI.getTfName().isEnabled() ? STUDENT.getName() : name;
            int ageEntryText = studentGUI.getTfAge().isEnabled() ? STUDENT.getAge() : age;
            String yearAndSectionEntryText = studentGUI.getTfYearAndSection().isEnabled() ? STUDENT.getYearAndSection() : yearAndSection;

            PreparedStatement executeFinalUpdate = conn.prepareStatement("UPDATE STUDENT_DETAILS SET STUDENT_NAME=?, STUDENT_AGE=?, STUDENT_YEAR_SECTION=? WHERE STUDENT_ID=? ");
            executeFinalUpdate.setString(1, nameEntryText);
            executeFinalUpdate.setInt(2, ageEntryText);
            executeFinalUpdate.setString(3, yearAndSectionEntryText);
            executeFinalUpdate.setInt(4, STUDENT.getId());
            executeFinalUpdate.executeUpdate();
            // Clearing, Enabling, Show Configured JComponents
            studentGUI.showButtons();
            studentGUI.clearFields();
            studentGUI.getRbFemale().setVisible(true);
            studentGUI.getRbMale().setVisible(true);
            studentGUI.getTfName().setEnabled(true);
            studentGUI.getTfAge().setEnabled(true);
            studentGUI.getTfYearAndSection().setEnabled(true);
            studentGUI.getBtnFinalUpdate().setVisible(false);
            studentGUI.getTfId().requestFocus();
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Update Success!");
        } catch (SQLException error) {
            error.printStackTrace();
            JOptionPane.showMessageDialog(studentGUI.getFrame(), "Error Occurred! Update Failed!");
        }
    }

    public void refreshTable(ActionEvent refreshTablePressed) {
        Vector<Vector<Object>> rows = getTableContent();
        Vector<Object> columns = new Vector<>(Arrays.asList("ID", "NAME", "SEX", "AGE", "YEAR/SECTION"));
        DefaultTableModel tableModel = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        studentGUI.getTable().setModel(tableModel);
    }

    private @NotNull Vector<Vector<Object>> getTableContent() {
        Vector<Vector<Object>> rows = new Vector<>();
        try (Connection conn = SQLiteConnection.myOwnGuiDbConnection()){
            PreparedStatement query = conn.prepareStatement("SELECT * FROM STUDENT_DETAILS");
            ResultSet result = query.executeQuery();
            while (result.next()) {
                rows.add(new Vector<>(Arrays.asList(
                        result.getInt("STUDENT_ID"),
                        result.getString("STUDENT_NAME"),
                        result.getString("STUDENT_SEX"),
                        result.getInt("STUDENT_AGE"),
                        result.getString("STUDENT_YEAR_SECTION"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Fetching Table Content Error!");
        }
        return rows;
    }

    public static StudentForm getInstance() {
        if (instance == null) instance = new StudentForm();
        return instance;
    }

    public JFrame getStudentGUI() {
        return studentGUI.getFrame();
    }

    private Set<Integer> getIdSet() {
        try (Connection conn = SQLiteConnection.myOwnGuiDbConnection()) {
            PreparedStatement fetchIdQuery = conn.prepareStatement("SELECT STUDENT_ID FROM STUDENT_DETAILS");
            ResultSet allId = fetchIdQuery.executeQuery();
            while (allId.next()) {
                idSet.add(allId.getInt("STUDENT_ID"));
            }
        } catch (SQLException e) {
            System.out.println("Check ID Error!");
        }
        return idSet;
    }
}
