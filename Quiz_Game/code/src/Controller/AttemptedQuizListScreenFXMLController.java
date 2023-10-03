package Controller;


import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import listener.AttemptedQuizItemClickListener;
import model.Quiz;
import model.QuizResult;
import model.Student;


import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class AttemptedQuizListScreenFXMLController implements Initializable {
    public VBox list;
    public Label total;
    public Label Aq;
    public Label ra;
    public Label wa;
    public PieChart attemptedChart;
    public PieChart rightWrongChart;
    private Student student;

    public void setStudent(Student student) {
        this.student = student;

        setList();
    }


    private void setList(){
        Map<QuizResult, Quiz> map = QuizResult.getQuizzes(student);
        Set<QuizResult> quizzesData = map.keySet();

        for(QuizResult quizResult : quizzesData){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("/FXML/AttemptedQuizListItemFXML.fxml"));
            try {
                Parent root = fxmlLoader.load();
                AttemptedQuizListItemFXML attemptedQuizListItemFXML = fxmlLoader.getController();
                attemptedQuizListItemFXML.setItemClickListener(new AttemptedQuizItemClickListener() {
                    @Override
                    public void itemClicked(Integer nof, Integer noa) {

                        int attempted = noa;
                        int nonAttempted = nof - attempted ;
                        int right = quizResult.getRightAnswers() ;
                        int wrong = attempted - right ;

                        total.setText(nof.toString());
                        Aq.setText(attempted + "");
                        ra.setText(right+"");
                        wa.setText((noa - quizResult.getRightAnswers()) + "");


                        ObservableList<PieChart.Data> attemptedData  = attemptedChart.getData();
                        attemptedData.clear();
                        attemptedData.add(new PieChart.Data("Attempted Questions ("+attempted+")" , attempted));
                        attemptedData.add(new PieChart.Data("Not Attempted Questions ("+nonAttempted+")" , nonAttempted));


                        ObservableList<PieChart.Data> answerData  = rightWrongChart.getData();
                        answerData.clear();
                        answerData.add(new PieChart.Data("Right Answers ("+right+")" , right));
                        answerData.add(new PieChart.Data("Wrong Answers ("+wrong+")" , wrong));


                    }
                });
                attemptedQuizListItemFXML.setData(map.get(quizResult) , quizResult);
                this.list.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
