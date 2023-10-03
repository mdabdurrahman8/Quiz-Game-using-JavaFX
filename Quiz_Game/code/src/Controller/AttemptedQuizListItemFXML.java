package Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import listener.AttemptedQuizItemClickListener;
import model.Quiz;
import model.QuizResult;

import java.net.URL;
import java.util.ResourceBundle;

public class AttemptedQuizListItemFXML implements Initializable {
    public Label title;
    public VBox item;
    private Quiz quiz;
    private QuizResult quizResult;
    private AttemptedQuizItemClickListener itemClickListener;

    public void setData(Quiz quiz , QuizResult quizResult) {
        this.quiz = quiz;
        this.quizResult = quizResult;
        this.title.setText(this.quiz.getTitle());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setItemClickListener(AttemptedQuizItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void loadData(MouseEvent mouseEvent) {
        System.out.println("Item Clicked...");
        Integer numberOfAttemptedQuestions = quizResult.getNumberOfAttemptedQuestions();
        Integer numberOfQuestions = quiz.getNumberOfQuestions();
        System.out.println(numberOfAttemptedQuestions);
        System.out.println(numberOfQuestions);
        itemClickListener.itemClicked(numberOfQuestions , numberOfAttemptedQuestions);
    }
}
