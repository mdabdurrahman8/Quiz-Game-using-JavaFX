package Controller;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class adminHomeScreenController implements Initializable {

    @FXML
    private TextField nameStudent;
    @FXML
    private TextField emailStudent;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton otherRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private Button studentAddButton;
    @FXML
    private Button studentDeleteButton;
    @FXML
    private Button updateFormButton;
    @FXML
    private Button clearFormButton;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> emailCol;
    @FXML
    private TableColumn<Student, String> passwordCol;
    @FXML
    private TableColumn<Student, String> genderCol;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private PasswordField password;


    private void radioButtonSetUp() {
        radioGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(radioGroup);
        femaleRadioButton.setToggleGroup(radioGroup);
        otherRadioButton.setToggleGroup(radioGroup);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioButtonSetUp();
        renderTable();
        saveStudentsToFile();
    }


    private void renderTable() {
        List<Student> students = Student.getAll();
        studentTable.getItems().clear();

        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        studentTable.getItems().addAll(students);
    }

    @FXML
    private void studentAdd(ActionEvent event) {
        String name = this.nameStudent.getText().trim();
        String email = this.emailStudent.getText().trim();
        String password = this.password.getText().trim();
        String gender = "";
        if (maleRadioButton.isSelected()) {
            gender = "Male";
        } else if (femaleRadioButton.isSelected()) {
            gender = "Female";
        } else if (otherRadioButton.isSelected()) {
            gender = "Other";
        }
        if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        } else {
            Student st = new Student(name, email, gender, password);
            if (st.save()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!!! ");
                alert.setHeaderText(null);
                alert.setContentText("Student is added successfully");
                studentTable.getItems().add(st);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!!!");
                alert.setHeaderText(null);
                alert.setContentText("Student could not be added");
                alert.showAndWait();
            }
        }
    }


    public void saveStudentsToFile() {
        String connectionUrl = "jdbc:sqlite:quizzess.db";
        String query = "SELECT * FROM Students";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                File file = new File("student.txt");
                FileWriter writer = new FileWriter(file);
                while (rs.next()) {
                    String name = rs.getString("Name");
                    String email = rs.getString("Email");
                    String gender = rs.getString("Gender");
                    String password = rs.getString("Password");
                    String studentData = email + "," + password + "," + name + "," + gender + "," + "\n";
                    writer.write(studentData);
                }
                writer.close();
                if (file.exists() && file.length() > 0) {
                    System.out.println("Data saved to students.txt");
                } else {
                    System.out.println("Error saving data to students.txt");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void studentDelete(ActionEvent event) {
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No row selected");
            alert.setHeaderText("Please select a row to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deletion");
            alert.setHeaderText("Are you sure you want to delete this record?");
            alert.setContentText("Student: " + Student.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Student.getAll();
                studentTable.getItems().remove(student);
            }
        }
    }

    @FXML
    private void updateForm(ActionEvent event) {
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No row selected");
            alert.setHeaderText("Please select a row to update");
            alert.showAndWait();
        } else {
            nameStudent.setText(Student.getName());
            emailStudent.setText(Student.getEmail());
            password.setText(student.getPassword());
            if (Student.getGender().equals("Male")) {
                maleRadioButton.setSelected(true);
            } else if (Student.getGender().equals("Female")) {
                femaleRadioButton.setSelected(true);
            } else {
                otherRadioButton.setSelected(true);
            }
        }
    }


    @FXML
    private void clearForm(ActionEvent event) {
        nameStudent.clear();
        emailStudent.clear();
        password.clear();
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        otherRadioButton.setSelected(false);
    }


}