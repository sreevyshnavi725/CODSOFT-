import java.io.*;
import java.util.*;

class Student {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll No: " + rollNumber + ", Grade: " + grade;
    }

    public String toFileFormat() {
        return name + "," + rollNumber + "," + grade;
    }

    public static Student fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Student(parts[0], parts[1], parts[2]);
    }
}

class StudentManagementSystem {
    private List<Student> students = new ArrayList<>();
    private final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        loadFromFile();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    public boolean removeStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) {
                students.remove(s);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public Student searchStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.write(s.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file.");
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(Student.fromFileFormat(line));
            }
        } catch (IOException e) {
            // File may not exist on first run
        }
    }
}

public class StudentManagementSystemApp {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\nSTUDENT MANAGEMENT SYSTEM");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1 -> addStudentUI();
                case 2 -> removeStudentUI();
                case 3 -> searchStudentUI();
                case 4 -> sms.displayAllStudents();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    private static void addStudentUI() {
        System.out.print("Enter name: ");
        String name = getNonEmptyString();
        System.out.print("Enter roll number: ");
        String roll = getNonEmptyString();
        System.out.print("Enter grade: ");
        String grade = getNonEmptyString();
        sms.addStudent(new Student(name, roll, grade));
        System.out.println("Student added.");
    }

    private static void removeStudentUI() {
        System.out.print("Enter roll number to remove: ");
        String roll = getNonEmptyString();
        if (sms.removeStudent(roll)) {
            System.out.println("Student removed.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void searchStudentUI() {
        System.out.print("Enter roll number to search: ");
        String roll = getNonEmptyString();
        Student s = sms.searchStudent(roll);
        if (s != null) {
            System.out.println("Student found: " + s);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static String getNonEmptyString() {
        String input;
        while ((input = scanner.nextLine()).trim().isEmpty()) {
            System.out.print("Input cannot be empty. Try again: ");
        }
        return input;
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}