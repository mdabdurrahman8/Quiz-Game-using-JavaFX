package StartApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

public class startApp extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createTables();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/LogIn.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    private void createTables(){
        Quiz.createTable();
        quizQuestion.createTable();
        Student.createTable();
        QuizResult.createTable();
        QuizResultDetails.createTable();
    }

}
