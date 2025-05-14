import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentGradeCalculatorGUI extends JFrame {

    private JTextField[] markFields;
    private JTextArea resultArea;
    private JButton calculateButton, resetButton, exitButton;
    private int numSubjects;
    private JPanel inputPanel;
    private JScrollPane scrollPane;

    public StudentGradeCalculatorGUI() {
        setTitle("Student Grade Calculator - Swing Edition");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        initializeMenuBar();
        getSubjectCountFromUser();
        initializeComponents();
        layoutComponents();
        addActionListeners();
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void getSubjectCountFromUser() {
        boolean validInput = false;
        while (!validInput) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter the number of subjects:",
                    "Subject Count",
                    JOptionPane.QUESTION_MESSAGE
            );

            try {
                numSubjects = Integer.parseInt(input);
                if (numSubjects <= 0) throw new NumberFormatException();
                validInput = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid positive number for subjects.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initializeComponents() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        markFields = new JTextField[numSubjects];

        for (int i = 0; i < numSubjects; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("Marks for Subject " + (i + 1) + ": ");
            label.setPreferredSize(new Dimension(150, 25));
            markFields[i] = new JTextField(10);
            row.add(label);
            row.add(markFields[i]);
            inputPanel.add(row);
        }

        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createTitledBorder("Results"));
        scrollPane = new JScrollPane(resultArea);

        calculateButton = new JButton("Calculate Grade");
        resetButton = new JButton("Reset");
        exitButton = new JButton("Exit");
    }

    private void layoutComponents() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(calculateButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(exitButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addActionListeners() {
        calculateButton.addActionListener(e -> calculateGrade());
        resetButton.addActionListener(e -> resetFields());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void calculateGrade() {
        int total = 0;
        boolean error = false;
        StringBuilder details = new StringBuilder("Subject-wise Marks:\n");

        for (int i = 0; i < numSubjects; i++) {
            String input = markFields[i].getText().trim();
            try {
                int mark = Integer.parseInt(input);
                if (mark < 0 || mark > 100) {
                    throw new IllegalArgumentException();
                }
                total += mark;
                details.append("Subject ").append(i + 1).append(": ").append(mark).append("\n");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number in Subject " + (i + 1),
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                error = true;
                break;
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this,
                        "Marks for Subject " + (i + 1) + " should be between 0 and 100.",
                        "Range Error",
                        JOptionPane.WARNING_MESSAGE);
                error = true;
                break;
            }
        }

        if (!error) {
            double average = (double) total / numSubjects;
            String grade = getGrade(average);
            details.append("\nTotal Marks: ").append(total);
            details.append("\nAverage Percentage: ").append(String.format("%.2f", average)).append("%");
            details.append("\nGrade: ").append(grade);
            details.append("\nStatus: ").append(grade.equals("F") ? "Fail" : "Pass");

            resultArea.setText(details.toString());
        }
    }

    private String getGrade(double average) {
        if (average >= 90) return "A+";
        else if (average >= 80) return "A";
        else if (average >= 70) return "B";
        else if (average >= 60) return "C";
        else if (average >= 50) return "D";
        else return "F";
    }

    private void resetFields() {
        for (JTextField field : markFields) {
            field.setText("");
        }
        resultArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeCalculatorGUI gui = new StudentGradeCalculatorGUI();
            gui.setVisible(true);
        });
    }
}