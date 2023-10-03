package util;

import model.Quiz;
import model.quizQuestion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {
    public static void main(String[] args) throws Exception {
        Quiz.createTable();
        quizQuestion.createTable();
        readAndSaveQuizzesData();
    }

    public static void readAndSaveQuizzesData() throws Exception {
        String folderPath = "D:/copy/code/design/Quiz new2 (1)/Quiz new1/Quiz new/Quiz/src/util/sample_data/quizes";

        File folder = new File(folderPath);

        String[] fileNames = folder.list();

        for (String filename : fileNames) {
            System.out.println(filename);

            File file = new File(folder + "/" + filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                System.out.println(stringBuilder);
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray result = jsonObject.getJSONArray("results");
                Quiz quiz = new Quiz();
                List<quizQuestion> questions = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {
                    String objString = result.get(i).toString();
                    JSONObject object = new JSONObject(objString);
                    quizQuestion question = new quizQuestion();
                    quiz.setTitle(object.getString("category"));
                    question.setQuestion(object.getString("question"));
                    JSONArray incorrectOptions = object.getJSONArray("incorrect_answers");

                    question.setOption1(incorrectOptions.get(0).toString());
                    question.setOption2(incorrectOptions.get(1).toString());
                    question.setOption3(incorrectOptions.get(2).toString());
                    question.setOption4(object.getString("correct_answer"));
                    question.setAnswer(object.getString("correct_answer"));
                    System.out.println(quiz);
                    questions.add(question);
                    question.setQuiz(quiz);
                    System.out.println(question);
                    System.out.println(quiz);
                }
                quiz.save(questions);
            }
        }
    }

}




