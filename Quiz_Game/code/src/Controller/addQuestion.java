package Controller;

import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Quiz;
import model.quizQuestion;

public class addQuestion implements Initializable {

    @FXML
    private TextField BlankTitle;

    @FXML
    private TextField addBlanks;

    @FXML
    private Button addNextBlankQuestion;

    @FXML
    private Button addNextQuestion;

    @FXML
    private TextField answer;

    @FXML
    private TextField option1;

    @FXML
    private RadioButton option1Radio;

    @FXML
    private TextField option2;

    @FXML
    private RadioButton option2Radio;

    @FXML
    private TextField option3;

    @FXML
    private RadioButton option3Radio;

    @FXML
    private TextField option4;

    @FXML
    private RadioButton option4Radio;

    @FXML
    private TextField questionAdd;

    @FXML
    private TextField quizTitle;

    @FXML
    private Button setBlankQuiz;

    @FXML
    private Button setQuizTitle;

    @FXML
    private Button submitBlanks;

    @FXML
    private Button submitQuiz;

    @FXML
    private TextField userAnswer;


    @FXML
    private ToggleGroup radioGroup;

    private Quiz quiz = null;
    private final ArrayList<quizQuestion> questions = new ArrayList<>();
    @FXML
    private TreeView treeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioButtonSetUp();
        renderTreeView();
    }

    private void renderTreeView() {
        Map<Quiz, List<quizQuestion>> data = Quiz.getAll();
        Set<Quiz> quizzs = data.keySet();

        TreeItem root = new TreeItem("Quizzes");
        for (Quiz q : quizzs) {
            TreeItem quizTreeItem = new TreeItem(q);

            List<quizQuestion> questions = data.get(q);
            for (quizQuestion question : questions) {
                TreeItem questionTreeItem = new TreeItem(question);
                questionTreeItem.getChildren().add(new TreeItem("A : " + question.getOption1()));
                questionTreeItem.getChildren().add(new TreeItem("B : " + question.getOption2()));
                questionTreeItem.getChildren().add(new TreeItem("C : " + question.getOption3()));
                questionTreeItem.getChildren().add(new TreeItem("D : " + question.getOption4()));
                questionTreeItem.getChildren().add(new TreeItem("Answer : " + question.getAnswer()));
                quizTreeItem.getChildren().add(questionTreeItem);
            }

            quizTreeItem.setExpanded(true);
            root.getChildren().add(quizTreeItem);
        }

        root.setExpanded(true);
        this.treeView.setRoot(root);
    }

    private void radioButtonSetUp() {
        radioGroup = new ToggleGroup();
        option1Radio.setToggleGroup(radioGroup);
        option2Radio.setToggleGroup(radioGroup);
        option3Radio.setToggleGroup(radioGroup);
        option4Radio.setToggleGroup(radioGroup);
    }

    @FXML
    private void setQuizTitleButton(ActionEvent actionEvent) {
        String title = quizTitle.getText();
        if (title.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Enter valid Quiz Title");
            alert.showAndWait();
        } else {
            quizTitle.setEditable(false);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Title is saved. You can continue");
            alert.showAndWait();
            this.quiz = new Quiz(title);
        }
    }

    @FXML
    private void setBlankQuizButton(ActionEvent actionEvent) {
        String title = BlankTitle.getText();
        if (title.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Enter valid Quiz Title");
            alert.showAndWait();
        } else {
            BlankTitle.setEditable(false);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Title is saved. You can continue");
            alert.showAndWait();
        }
    }

    private boolean validateQuiz() {
        if (quiz == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Quiz Title");
            alert.showAndWait();
        }
        String ques = this.questionAdd.getText();
        String op1 = this.option1.getText();
        String op2 = this.option1.getText();
        String op3 = this.option1.getText();
        String op4 = this.option1.getText();
        Toggle selectedRadio = radioGroup.getSelectedToggle();

        if ((ques.trim().isEmpty()) || (op1.trim().isEmpty()) || (op2.trim().isEmpty()) || (op3.trim().isEmpty()) || (op4.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Fill al the options");
            alert.showAndWait();
            return false;
        } else {
            if (selectedRadio == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Select an answer first");
                alert.showAndWait();
                return false;
            } else {
                return true;

            }
        }
    }


    @FXML
    private void addNextQuestionButton(ActionEvent actionEvent) {
        addQuestionButton();
    }

    @FXML
    private boolean addQuestionButton() {
        boolean valid = validateQuiz();
        quizQuestion question = new quizQuestion();
        if (valid) {
            // Populate question object with data from UI elements
            question.setOption1(option1.getText().trim());
            question.setOption2(option2.getText().trim());
            question.setOption3(option3.getText().trim());
            question.setOption4(option4.getText().trim());
            Toggle selected = radioGroup.getSelectedToggle();
            String ans = null;
            if (selected == option1Radio) {
                ans = option1.getText().trim();
            } else if (selected == option2Radio) {
                ans = option2.getText().trim();
            } else if (selected == option3Radio) {
                ans = option3.getText().trim();
            } else if (selected == option4Radio) {
                ans = option4.getText().trim();
            }
            question.setAnswer(ans);
            question.setQuestion(this.questionAdd.getText().trim());

            this.questionAdd.clear();
            option1.clear();
            option2.clear();
            option3.clear();
            option4.clear();
            questions.add(question);
            question.setQuiz(quiz);
            System.out.println(questions);
            System.out.println(quiz);
        }
        return valid;
    }

    @FXML
    private void submitQuizButton(ActionEvent actionEvent) {
        boolean flag = addQuestionButton();
        if (flag) {
            flag = quiz.save(questions); // Save quiz with new questions
            if (flag) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Quiz successfully saved");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Cannot save the Quiz..Try Again!!!");
                alert.showAndWait();
            }
        }
    }


    @FXML
    private void submitBlanksButton(ActionEvent actionEvent) {
    }

    private boolean validateBlanksQuiz() {
        String ques = this.addBlanks.getText();
        String ans = this.userAnswer.getText();
        String rightAnswer = this.answer.getText();

        if ((ques.trim().isEmpty()) || (ans.trim().isEmpty()) || (rightAnswer.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Fill al the options");
            alert.showAndWait();
            return false;
        } else {

            return true;
            //Questions
        }

    }


}
