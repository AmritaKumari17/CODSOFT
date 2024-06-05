import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Course {
    String code, title, description, schedule;
    int capacity, enrolled;

    Course(String code, String title, String description, int capacity, String schedule) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolled = 0;
    }

    boolean isAvailable() {
        return enrolled < capacity;
    }

    void registerStudent() {
        enrolled++;
    }

    void dropStudent() {
        enrolled--;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%d/%d)", code, title, enrolled, capacity);
    }
}

class Student {
    String id, name;
    ArrayList<Course> registeredCourses;

    Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    void registerCourse(Course course) {
        registeredCourses.add(course);
        course.registerStudent();
    }

    void dropCourse(Course course) {
        registeredCourses.remove(course);
        course.dropStudent();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", id, name);
    }
}

public class CourseRegistrationSystem extends JFrame {
    private HashMap<String, Course> courseDatabase = new HashMap<>();
    private HashMap<String, Student> studentDatabase = new HashMap<>();
    private JList<Course> courseList;
    private JList<Student> studentList;
    private DefaultListModel<Course> courseListModel;
    private DefaultListModel<Student> studentListModel;
    private JTextArea courseDetails;

    public CourseRegistrationSystem() {
        setTitle("Student Course Registration System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sample data
        initializeData();

        // Components
        courseListModel = new DefaultListModel<>();
        studentListModel = new DefaultListModel<>();
        courseList = new JList<>(courseListModel);
        studentList = new JList<>(studentListModel);
        courseDetails = new JTextArea();
        courseDetails.setEditable(false);

        for (Course course : courseDatabase.values()) {
            courseListModel.addElement(course);
        }
        for (Student student : studentDatabase.values()) {
            studentListModel.addElement(student);
        }

        JScrollPane courseScrollPane = new JScrollPane(courseList);
        JScrollPane studentScrollPane = new JScrollPane(studentList);
        JScrollPane courseDetailsScrollPane = new JScrollPane(courseDetails);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(courseScrollPane);
        centerPanel.add(courseDetailsScrollPane);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Students"), BorderLayout.NORTH);
        rightPanel.add(studentScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        JButton dropButton = new JButton("Drop");

        registerButton.addActionListener(new RegisterActionListener());
        dropButton.addActionListener(new DropActionListener());

        bottomPanel.add(registerButton);
        bottomPanel.add(dropButton);

        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        courseList.addListSelectionListener(e -> updateCourseDetails());
    }

    private void initializeData() {
        courseDatabase.put("CS101", new Course("CS101", "Intro to CS", "Basic concepts of computer science", 30, "MWF 9-10AM"));
        courseDatabase.put("CS102", new Course("CS102", "Data Structures", "In-depth study of data structures", 25, "TTh 11-12:30PM"));
        courseDatabase.put("CS103", new Course("CS103", "Operating System", "Concepts of Operating System", 30, "MWF 10-11AM"));
        courseDatabase.put("CS104", new Course("CS104", "DataBase And Management System ", "About DBMS and Implementaion of MySQL", 25, "TTh 01-2:30PM"));
        courseDatabase.put("CS105", new Course("CS105", "Intro to AFL", "Study of Automata Theory", 30, "MWF 5-6PM"));
        courseDatabase.put("CS106", new Course("CS106", "Data Analysis and Algorithm", "In-depth description of data analysis", 25, "TTh 08-09:00PM"));
        studentDatabase.put("S001", new Student("S001", "Aditya"));
        studentDatabase.put("S002", new Student("S002", "Jayesmita"));
        studentDatabase.put("S003", new Student("S003", "Rahul"));
        studentDatabase.put("S004", new Student("S004", "Shivam"));
        studentDatabase.put("S005", new Student("S005", "Shubham"));
        studentDatabase.put("S006", new Student("S006", "Kriti"));
        studentDatabase.put("S007", new Student("S007", "Khushi"));
        studentDatabase.put("S008", new Student("S008", "Shreya"));
        studentDatabase.put("S009", new Student("S009", "Ayush"));
        studentDatabase.put("S010", new Student("S010", "Gayetri"));
        studentDatabase.put("S011", new Student("S011", "Om"));
        studentDatabase.put("S012", new Student("S012", "Amitya"));
    }


    private void updateCourseDetails() {
        Course selectedCourse = courseList.getSelectedValue();
        if (selectedCourse != null) {
            courseDetails.setText(String.format("Code: %s\nTitle: %s\nDescription: %s\nSchedule: %s\nCapacity: %d\nEnrolled: %d",
                    selectedCourse.code, selectedCourse.title, selectedCourse.description, selectedCourse.schedule, selectedCourse.capacity, selectedCourse.enrolled));
        }
    }

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Course selectedCourse = courseList.getSelectedValue();
            Student selectedStudent = studentList.getSelectedValue();
            if (selectedCourse != null && selectedStudent != null && selectedCourse.isAvailable()) {
                selectedStudent.registerCourse(selectedCourse);
                courseList.repaint();
                updateCourseDetails();
                JOptionPane.showMessageDialog(CourseRegistrationSystem.this, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(CourseRegistrationSystem.this, "Registration failed. Course may be full or not selected.");
            }
        }
    }

    private class DropActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Course selectedCourse = courseList.getSelectedValue();
            Student selectedStudent = studentList.getSelectedValue();
            if (selectedCourse != null && selectedStudent != null && selectedStudent.registeredCourses.contains(selectedCourse)) {
                selectedStudent.dropCourse(selectedCourse);
                courseList.repaint();
                updateCourseDetails();
                JOptionPane.showMessageDialog(CourseRegistrationSystem.this, "Course dropped successfully!");
            } else {
                JOptionPane.showMessageDialog(CourseRegistrationSystem.this, "Drop failed. Course may not be registered or not selected.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CourseRegistrationSystem frame = new CourseRegistrationSystem();
            frame.setVisible(true);
        });
    }
}
