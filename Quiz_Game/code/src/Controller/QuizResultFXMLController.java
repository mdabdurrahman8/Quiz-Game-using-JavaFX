package Controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import model.Quiz;
import model.quizQuestion;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizResultFXMLController implements Initializable {
    public PieChart attemptedChart;
    public PieChart scoreChart;
    public VBox questionsContainer;

    // not fxml variable
    private Map<quizQuestion , String> userAnswers ;
    private Integer numberOfRightAnswers = 0;
    private Quiz quiz ;
    private List<quizQuestion> questionList;
    private Integer notAttempted = 0 ;
    private Integer attempted = 0 ;


    public void  setValues(Map<quizQuestion, String> userAnswers,
                           Integer numberOfRightAnswers, Quiz quiz,
                           List<quizQuestion> questionList) {
        this.userAnswers = userAnswers;
        this.numberOfRightAnswers = numberOfRightAnswers;
        this.quiz = quiz;
        this.questionList = questionList;

        this.attempted = this.userAnswers.keySet().size();
        this.notAttempted = this.questionList.size() - attempted;

        setValuesToChart();
        renderQuestions();
    }


    private void renderQuestions(){
        for(int i = 0 ; i < this.questionList.size() ; i ++){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("/FXML/QuizResultSingleQuestionFXML.fxml"));

            try {
                Node node = fxmlLoader.load();
                QuizResultSingleQuestionFXMLController controller= fxmlLoader.getController();
                controller.setValues(this.questionList.get(i) , this.userAnswers.get(this.questionList.get(i)));
                questionsContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setValuesToChart(){
        ObservableList<PieChart.Data> attemptedData = this.attemptedChart.getData();
        attemptedData.add(new PieChart.Data(String.format("Attempted (%d)", this.attempted) , this.attempted));
        attemptedData.add(new PieChart.Data(String.format("Not Attempted (%d)", this.notAttempted) , this.notAttempted));


        ObservableList<PieChart.Data> scoreChartData = this.scoreChart.getData();
        scoreChartData.add(new PieChart.Data(
                String.format("Right Answers (%d)", this.numberOfRightAnswers) , this.numberOfRightAnswers));
        scoreChartData.add(new PieChart.Data(
                String.format("Wrong Answers (%d)", this.attempted - this.numberOfRightAnswers) , this.attempted-this.numberOfRightAnswers));


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

