package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import listener.NewScreenListener;
import model.Quiz;
import model.Student;

public class quizCardLayoutController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label questionNumber;
    @FXML
    private Button startButton;
    private Quiz quiz;
    private NewScreenListener screenListener;
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
    }

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setQuestionNumber(String value) {
        this.questionNumber.setText(value);
    }

    @FXML
    private void startQuizButton(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/questionScreen.fxml"));
        try {
            Node node = fxmlLoader.load();
            QuestionScreenController questionScreenController = fxmlLoader.getController();
            questionScreenController.setStudent(this.student);
            questionScreenController.setQuiz(this.quiz);
            questionScreenController.setScreenListener(this.screenListener);
            this.screenListener.ChangeScreen(node);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
