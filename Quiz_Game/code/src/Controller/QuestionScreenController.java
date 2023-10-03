package Controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import listener.NewScreenListener;
import model.Quiz;
import model.QuizResult;
import model.Student;
import model.quizQuestion;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuestionScreenController implements Initializable {
    private final Map<quizQuestion, String> studentAnswers = new HashMap<>();
    int currentIndex = 0;
    @FXML
    private FlowPane progressPane;
    @FXML
    private Label title;
    @FXML
    private Label time;
    @FXML
    private Label question;
    @FXML
    private RadioButton option1;
    @FXML
    private ToggleGroup options;
    @FXML
    private RadioButton option2;
    @FXML
    private RadioButton option3;
    @FXML
    private RadioButton option4;
    @FXML
    private Button next;
    @FXML
    private Button submit;
    private Quiz quiz;
    private List<quizQuestion> questionList;
    private quizQuestion currentQuestion;
    private QuestionObservable questionObservable;
    private Integer numberOfRightAnswers = 0;
    private NewScreenListener screenListener;
    private Student student;

    public void setScreenListener(NewScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
        this.getData();
    }

    private long min, sec, hour, totalSec = 0;
    private Timer timer;

    private String format(long value) {
        if (value < 10) {
            return 0 + "" + value;
        }

        return value + "";
    }

    public void convertTime() {

        min = TimeUnit.SECONDS.toMinutes(totalSec);
        sec = totalSec - (min * 60);
        hour = TimeUnit.MINUTES.toHours(min);
        min = min - (hour * 60);
        time.setText(format(hour) + ":" + format(min) + ":" + format(sec));

        totalSec--;
    }



    private void setTimer() {

        totalSec = this.questionList.size() * 10;
        this.timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        convertTime();
                        if (totalSec <= 0) {
                            timer.cancel();
                            time.setText("00:00:00");

                            // saving data to database
                            submitQuizButton(null);
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning!!!");
                            alert.setHeaderText(null);
                            alert.setContentText("Error!!Time up");
                            alert.showAndWait();
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    private void getData() {
        if (quiz != null) {
            this.questionList = quiz.getquizQuestions();
            Collections.shuffle(this.questionList);
            renderProgress();
            setNextQuestion();
            setTimer();
        }
    }

    private void renderProgress() {
        for (int i = 0; i < this.questionList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass()
                            .getResource("/FXML/ProgressCircle.fxml"));
            try {
                Node node = fxmlLoader.load();
                ProgressCircle progressCircle = fxmlLoader.getController();
                progressCircle.setNumber(i + 1);
                progressCircle.setDefaultColor();
                progressPane.getChildren().add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.showNextQuestionButton();
        this.hideSubmitQuizButton();
        this.questionObservable = new QuestionObservable();
        bindFields();

    }

    private void bindFields() {
        this.question.textProperty().bind(this.questionObservable.question);
        this.option4.textProperty().bind(this.questionObservable.option4);
        this.option3.textProperty().bind(this.questionObservable.option3);
        this.option2.textProperty().bind(this.questionObservable.option2);
        this.option1.textProperty().bind(this.questionObservable.option1);
    }

    @FXML
    private void nextQuestionButton(ActionEvent event) {
        boolean isRight = false;
        {
            //checking answers
            RadioButton selectedButton = (RadioButton) options.getSelectedToggle();
            String userAnswer = selectedButton.getText();
            String rightAnswer = this.currentQuestion.getAnswer();
            if (userAnswer.trim().equalsIgnoreCase(rightAnswer.trim())) {
                isRight = true;
                this.numberOfRightAnswers++;
            }
            //saving answers to map
            studentAnswers.put(this.currentQuestion, userAnswer);
        }

        // Clear selected toggle
        options.getSelectedToggle().setSelected(false);

        Node circleNode = this.progressPane.getChildren().get(currentIndex - 1);
        ProgressCircle controller = (ProgressCircle) circleNode.getUserData();
        if (isRight) {
            controller.setRightAnsweredColor();
        } else {
            controller.setWrongAnsweredColor();
        }
        this.setNextQuestion();
    }

    private void setNextQuestion() {
        if (!(currentIndex >= questionList.size())) {
            {
                //changing the color
                Node circleNode = this.progressPane.getChildren().get(currentIndex);
                ProgressCircle controller = (ProgressCircle) circleNode.getUserData();
                controller.setCurrentQuestionColor();
            }

            this.currentQuestion = this.questionList.get(currentIndex);
            List<String> options = new ArrayList<>();

            options.add(this.currentQuestion.getOption1());
            options.add(this.currentQuestion.getOption2());
            options.add(this.currentQuestion.getOption3());
            options.add(this.currentQuestion.getOption4());
            Collections.shuffle(options);

            this.currentQuestion.setOption1(options.get(0));
            this.currentQuestion.setOption2(options.get(1));
            this.currentQuestion.setOption3(options.get(2));
            this.currentQuestion.setOption4(options.get(3));

            this.questionObservable.setQuestion(this.currentQuestion);
            currentIndex++;

        } else {
            hideNextQuestionButton();
            showSubmitQuizButton();
        }
    }

    private void showNextQuestionButton() {
        this.next.setVisible(true);
    }

    private void showSubmitQuizButton() {
        this.submit.setVisible(true);
    }

    private void hideNextQuestionButton() {
        this.next.setVisible(false);
    }

    private void hideSubmitQuizButton() {
        this.submit.setVisible(false);
    }

    @FXML
    private void submitQuizButton(ActionEvent event) {
        Student student = new Student();
        student.setId(1);
        QuizResult quizResult = new QuizResult(this.quiz, student, numberOfRightAnswers);
        quizResult.save(this.studentAnswers);
        boolean result = quizResult.save(this.studentAnswers);
        if (result) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("Successfully attempted quiz");
            alert.showAndWait();
            timer.cancel();
            openResultScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!!!");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    private class QuestionObservable {
        Property<String> question = new SimpleStringProperty();
        Property<String> option1 = new SimpleStringProperty();
        Property<String> option2 = new SimpleStringProperty();
        Property<String> option3 = new SimpleStringProperty();
        Property<String> option4 = new SimpleStringProperty();
        Property<String> answer = new SimpleStringProperty();

        private void setQuestion(quizQuestion question) {
            this.question.setValue(question.getQuestion());
            this.option1.setValue(question.getOption1());
            this.option2.setValue(question.getOption2());
            this.option3.setValue(question.getOption3());
            this.option4.setValue(question.getOption4());
            this.answer.setValue(question.getAnswer());
        }
    }
    

    private void openResultScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                getResource("/FXML/QuizResultFXML.fxml"));

        try {
            Node node = fxmlLoader.load();
            QuizResultFXMLController controller = fxmlLoader.getController();
            controller.setValues(this.studentAnswers, numberOfRightAnswers, quiz, questionList);
            this.screenListener.removeTopScreen();
            this.screenListener.ChangeScreen(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



