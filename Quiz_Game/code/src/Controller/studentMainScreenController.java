package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import listener.NewScreenListener;
import model.Student;

public class studentMainScreenController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private StackPane stackPanel;
    private Student student;

    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }

    private void addScreenToStackPane(Node node) {
        this.stackPanel.getChildren().add(node);
    }

    private void addQuizListScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/quizListFXML.fxml"));
        try {
            Node node = fxmlLoader.load();
            quizListFXMLController quizListController = fxmlLoader.getController();
            quizListController.setScreenListener(new NewScreenListener() {
                @Override
                public void ChangeScreen(Node node) {
                    addScreenToStackPane(node);
                }

                @Override
                public void removeTopScreen() {
                    stackPanel.getChildren().remove(stackPanel.getChildren().size()-1);
                }

                @Override
                public void handle(Event event) {

                }
            });
            quizListController.setCards();
            stackPanel.getChildren().add(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void back(ActionEvent actionEvent) {
        ObservableList<Node> nodes = this.stackPanel.getChildren();
        if (nodes.size() == 1) {
            return;
        }
        this.stackPanel.getChildren().remove(nodes.size() - 1);
    }

    public void setStudent(Student student) {
        this.student = student;
        addQuizListScreen();
    }

}




