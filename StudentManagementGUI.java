import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Student {
    private String name;
    private String roll;
    private String grade;

    public Student(String name, String roll, String grade) {
        this.name = name;
        this.roll = roll;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getGrade() {
        return grade;
    }

    public String toFileFormat() {
        return name + "," + roll + "," + grade;
    }

    public static Student fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Student(parts[0], parts[1], parts[2]);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll: " + roll + ", Grade: " + grade;
    }
}

class StudentManager {
    private final String FILE = "students.txt";
    private java.util.List<Student> students = new ArrayList<>();

    public StudentManager() {
        loadFromFile();
    }

    public void addStudent(Student s) {
        students.add(s);
        saveToFile();
    }

    public boolean removeStudent(String roll) {
        for (Student s : students) {
            if (s.getRoll().equalsIgnoreCase(roll)) {
                students.remove(s);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public Student searchStudent(String roll) {
        for (Student s : students) {
            if (s.getRoll().equalsIgnoreCase(roll)) {
                return s;
            }
        }
        return null;
    }

    public String displayAllStudents() {
        StringBuilder sb = new StringBuilder();
        for (Student s : students) {
            sb.append(s).append("\n");
        }
        return sb.toString().isEmpty() ? "No student data." : sb.toString();
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Student s : students) {
                bw.write(s.toFileFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving data.");
        }
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(Student.fromFileFormat(line));
            }
        } catch (IOException e) {
            // First run, file might not exist
        }
    }
}

public class StudentManagementGUI extends JFrame {
    private JTextField nameField, rollField, gradeField, searchField;
    private JTextArea displayArea;
    private StudentManager manager = new StudentManager();

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll No:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton addBtn = new JButton("Add Student");
        inputPanel.add(addBtn);
        addBtn.addActionListener(e -> addStudent());

        JButton removeBtn = new JButton("Remove by Roll");
        inputPanel.add(removeBtn);
        removeBtn.addActionListener(e -> removeStudent());

        add(inputPanel, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search Roll:"));
        searchField = new JTextField(10);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        searchBtn.addActionListener(e -> searchStudent());

        JButton showAllBtn = new JButton("Show All");
        searchPanel.add(showAllBtn);
        showAllBtn.addActionListener(e -> showAllStudents());

        add(searchPanel, BorderLayout.SOUTH);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || roll.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        manager.addStudent(new Student(name, roll, grade));
        JOptionPane.showMessageDialog(this, "Student added.");
        clearFields();
        showAllStudents();
    }

    private void removeStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Roll number to remove.");
            return;
        }

        if (manager.removeStudent(roll)) {
            JOptionPane.showMessageDialog(this, "Student removed.");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.");
        }
        clearFields();
        showAllStudents();
    }

    private void searchStudent() {
        String roll = searchField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter roll number to search.");
            return;
        }

        Student s = manager.searchStudent(roll);
        if (s != null) {
            displayArea.setText(s.toString());
        } else {
            displayArea.setText("Student not found.");
        }
    }

    private void showAllStudents() {
        displayArea.setText(manager.displayAllStudents());
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementGUI().setVisible(true));
    }
}