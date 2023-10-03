package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class SignUpTeacherController implements Initializable {


    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField nameField;

    @FXML
    private RadioButton otherRadioButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button teacherSignUp;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioButtonSetUp();
    }

    private void radioButtonSetUp() {
        ToggleGroup radioGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(radioGroup);
        femaleRadioButton.setToggleGroup(radioGroup);
        otherRadioButton.setToggleGroup(radioGroup);
    }

    @FXML
    private void teacherSignUpButton(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String gender = "";
        if (maleRadioButton.isSelected()) {
            gender = "Male";
        } else if (femaleRadioButton.isSelected()) {
            gender = "Female";
        } else if (otherRadioButton.isSelected()) {
            gender = "Other";
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || gender.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
            return;
        }

        if (userExists(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("User with this email already exists!");
            alert.showAndWait();
            return;
        }

        // Save user data to file
        saveUserDataToFile( email, password,name, gender);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("User registered successfully!");
        alert.showAndWait();

        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
    }

    private boolean userExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("teacher.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUserDataToFile( String email, String password,String name, String gender) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("teacher.txt", true))) {
            writer.printf("%s,%s,%s,%s\n", email, password, name, gender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
