import java.io.*;
import java.util.Scanner;

import Controller.studentMainScreenController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import model.Quiz;
import model.Student;

public class Main extends Application {

    @FXML
    private TextField studentEmail;
    @FXML
    private PasswordField studentPassword;

    @FXML
    private Button studentLogIn;
    @FXML
    private TextField adminEmail;
    @FXML
    private PasswordField adminPassword;
    @FXML
    private TextField teacherEmail;
    @FXML
    private PasswordField teacherPassword;
    @FXML
    private Button adminLogin;
    @FXML
    private Button teacherLogin;
    @FXML
    private Button SignUpStudent;
    @FXML
    private Button SignUpTeacher;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/LogIn.fxml"));
        Scene scene = new Scene(root,850,500);
        primaryStage.setTitle("Seeker's Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private boolean authenticate(String email, String password) {
        try {
            Scanner scanner = new Scanner(new File("student.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].trim().equals(email.trim()) && parts[1].trim().equals(password.trim())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void studentLoginButton(ActionEvent event) throws IOException {
        String email = studentEmail.getText().trim();
        String password = studentPassword.getText().trim();
        if (authenticate(email,password)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/studentMainScreen.fxml"));
           Parent root = fxmlLoader.load();
           studentMainScreenController controller = fxmlLoader.getController();
           controller.setStudent(new Student(this.studentEmail.getText().trim(),this.studentPassword.getText().trim()));
            Stage stage = (Stage) studentEmail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Email or Passwords do not match!");
            alert.showAndWait();
        }
    }
    @FXML
    private void adminLoginButton(ActionEvent event) throws IOException {
        String email = adminEmail.getText();
        String password = adminPassword.getText();
        if (email.equals("admin") && password.equals("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/adminHomeScreen.fxml"));
            Stage stage = (Stage) adminEmail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Email or Passwords do not match!");
            alert.showAndWait();
        }

    }
    private boolean authentication(String email, String password) {
        try {
            Scanner scanner = new Scanner(new File("teacher.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].trim().equals(email.trim()) && parts[1].trim().equals(password.trim())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    @FXML
    private void teacherLoginButton(ActionEvent event) throws IOException {
        if (authentication(teacherEmail.getText().trim(), teacherPassword.getText().trim())) {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/addQuestion.fxml"));
            Stage stage = (Stage) adminEmail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Email or Passwords do not match!");
            alert.showAndWait();
        }

    }

    @FXML
    private void SignUpStudentButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/signUpStudentInfo.fxml"));
        Stage stage = (Stage) studentEmail.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void SignUpTeacherButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXML/signUpTeacherInfo.fxml"));
        Stage stage = (Stage) teacherEmail.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }
}

