package form.app;

import lombok.Value;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

@Value
class StudentGUI {
    static StudentGUI studentGuiInstance;
    JFrame frame;
    JTextField tfId;
    JTextField tfName;
    JRadioButton rbMale;
    JRadioButton rbFemale;
    JTextField tfAge;
    JTextField tfYearAndSection;
    JButton btnInsert;
    JButton btnUpdate;
    JButton btnDelete;
    JButton btnRead;
    JButton btnFinalUpdate;
    JTable table;
    JButton btnRefresh;

    private StudentGUI() {
        frame = new JFrame("Student Information System");
        // Labels
        JLabel lblIntro = new JLabel("STUDENT INFORMATION SYSTEM");
        JLabel lblId = new JLabel("Student Id: ");
        JLabel lblName = new JLabel("Student Name: ");
        JLabel lblAge = new JLabel("Student Age: ");
        JLabel lblSex = new JLabel("Student Sex: ");
        JLabel lblYearAndSection = new JLabel("Student Year&Section: ");
        // Fields
        tfId = new JTextField();
        tfName = new JTextField();
        tfAge = new JTextField();
        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        tfYearAndSection = new JTextField();
        // Buttons
        btnInsert = new JButton("Insert", new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/insert.png"))));
        btnUpdate = new JButton("Update", new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/update.png"))));
        btnDelete = new JButton("Delete", new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/delete.png"))));
        btnRead = new JButton("Read", new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/read.png"))));
        btnRefresh = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/refresh.png"))));
        btnFinalUpdate = new JButton("Update", new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/update.png"))));
        btnFinalUpdate.setVisible(false);
        // Table
            // Table Content
        Vector<Vector<Object>> rows = new Vector<>();
        rows.add(new Vector<>(Arrays.asList(0, "SAMPLE", "SAMPLE", "SAMPLE", "SAMPLE")));
        Vector<String> columns = new Vector<>(Arrays.asList("ID", "NAME", "AGE", "SEX", "Year/Section"));
        table = new JTable(rows, columns);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(280);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(2).setResizable(false);
        table.getColumnModel().getColumn(3).setResizable(false);
        table.getColumnModel().getColumn(4).setResizable(false);
        JScrollPane sp = new JScrollPane(table);
        // Designing Texts
        lblIntro.setFont(new Font("Serif", Font.BOLD, 20));
        // Placing Labels
        lblIntro.setBounds(80, 10, 400, 30);
        lblId.setBounds(10, 50, 100, 20);
        lblName.setBounds(10, 80, 100, 20);
        lblAge.setBounds(10, 110, 100, 20);
        lblSex.setBounds(10, 140, 100, 20);
        lblYearAndSection.setBounds(10, 170, 150, 20);
        // Placing TextFields
        tfId.setBounds(150, 50, 300, 30);
        tfName.setBounds(150, 80, 300, 30);
        tfAge.setBounds(150, 110, 300, 30);
        rbMale.setBounds(150, 140, 100, 30);
        rbFemale.setBounds(250, 140, 100, 30);
        tfYearAndSection.setBounds(150, 170, 300, 30);
        // Placing Buttons
        btnInsert.setBounds(10, 220, 200, 50);
        btnUpdate.setBounds(250, 220, 200, 50);
        btnDelete.setBounds(10, 290, 200, 50);
        btnRead.setBounds(250, 290, 200, 50);
        btnRefresh.setBounds(955, 10, 30, 30);
        btnFinalUpdate.setBounds(150, 240, 200, 50);
        // Placing Table and ScrollPane
        sp.setBounds(470, 10, 480, 340);
        // Adding Components to frame
        sexOptions();
        frame.add(lblIntro);
        frame.add(lblId);frame.add(tfId);
        frame.add(lblName);frame.add(tfName);
        frame.add(lblAge);frame.add(tfAge);
        frame.add(lblSex);frame.add(rbMale);frame.add(rbFemale);
        frame.add(lblYearAndSection);frame.add(tfYearAndSection);
        frame.add(btnInsert);frame.add(btnUpdate);
        frame.add(btnDelete);frame.add(btnRead);
        frame.add(sp);
        frame.add(btnRefresh);
        frame.add(btnFinalUpdate);

        frame.setLayout(null);
        frame.setVisible(false);
        frame.setResizable(false);
        frame.setSize(1010, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tfId.requestFocusInWindow();
    }

    void clearFields() {
        tfId.setText(null);
        tfName.setText(null);
        tfAge.setText(null);
        tfYearAndSection.setText(null);
    }

    void hideButtons() {
        btnInsert.setVisible(false);
        btnRead.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    void showButtons() {
        btnInsert.setVisible(true);
        btnRead.setVisible(true);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);
    }

    private void sexOptions() {
        ButtonGroup sexOption = new ButtonGroup();
        sexOption.add(rbMale);
        sexOption.add(rbFemale);
        rbMale.setSelected(true);
    }

    static StudentGUI getGuiInstance() {
        if (studentGuiInstance == null) studentGuiInstance = new StudentGUI();
        return studentGuiInstance;
    }
}
