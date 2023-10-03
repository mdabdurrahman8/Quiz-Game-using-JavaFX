package Controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import listener.NewScreenListener;
import model.Quiz;

public class quizListFXMLController implements Initializable {

    Map<Quiz, Integer> quizzes = null;
    @FXML
    private FlowPane quizListContainer;
    private NewScreenListener screenListener;
    private Set<Quiz> keys;

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;

    }

    public void setCards() {
        for (Quiz quiz : keys) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/quizCardLayout.fxml"));
            try {
                Node node = fxmlLoader.load();
                quizCardLayoutController quizCard = fxmlLoader.getController();
                quizCard.setQuiz(quiz);
                quizCard.setQuestionNumber(quizzes.get(quiz) + "");
                quizCard.setScreenListener(this.screenListener);
                quizListContainer.getChildren().add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quizzes = Quiz.getAllWithQuestionCount();
        keys = quizzes.keySet();
    }

}
