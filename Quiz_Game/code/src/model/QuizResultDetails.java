package model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Set;

public class QuizResultDetails {

    private Integer id;
    private quizQuestion question;
    private String userAnswer;
    private QuizResult quizResult;

    public static class constant {
        public static final String TABLE_NAME = "QUIZ_RESULT_DETAILS";
        public static final String ID = "ID";
        public static final String USER_ANSWER = "USER_ANSWER";
        public static final String QUESTION_ID = "QUESTION_ID";
        public static final String QUIZ_RESULT_ID = "QUIZ_RESULT_ID";

    }


    public QuizResultDetails(Integer id, quizQuestion question, String userAnswer, QuizResult quizResult) {
        this.id = id;
        this.question = question;
        this.userAnswer = userAnswer;
        this.quizResult = quizResult;
    }

    public QuizResultDetails(quizQuestion question, String userAnswer, QuizResult quizResult) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.quizResult = quizResult;
    }

    public QuizResultDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public quizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(quizQuestion question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public QuizResult getQuizResult() {
        return quizResult;
    }

    public void setQuizResult(QuizResult quizResult) {
        this.quizResult = quizResult;
    }


    public static void createTable() {

        String raw = "   CREATE table %s(\n" +
                "        %s INTEGER NOT null PRIMARY key  AUTOINCREMENT,\n" +
                "        %s int not null ,\n" +
                "        %s int NOT NULL ,\n" +
                "        %s varchar(200) not null ,\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "        FOREIGN KEY (%s) REFERENCES questions(id)\n" +
                "        )";
        String query = String.format(raw,
                constant.TABLE_NAME,
                constant.ID,
                constant.QUIZ_RESULT_ID,
                constant.QUESTION_ID,
                constant.USER_ANSWER,
                constant.QUIZ_RESULT_ID,
                quizQuestion.constant.TABLE_NAME,
                quizQuestion.constant.QUIZ_ID,
                constant.QUESTION_ID,
                quizQuestion.constant.TABLE_NAME,
                quizQuestion.constant.QUESTION_ID
        );
        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.QuizResults.createTable()");
            System.out.println(b);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    static boolean saveQuizResultDetails(QuizResult quizResult, Map<quizQuestion, String> userAnswers){
        String raw = "INSERT INTO QUIZ_RESULT_DETAILS (QUIZ_RESULT_ID,QUESTION_ID,USER_ANSWER) VALUES " +
                "(\n" + "? , ?  , ? )";
        String query  = String.format(raw,
                constant.TABLE_NAME ,
                constant.QUIZ_RESULT_ID ,
                constant.QUESTION_ID ,
                constant.USER_ANSWER

        );

        try{
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);

            Set<quizQuestion> questions  = userAnswers.keySet();
            for(quizQuestion question : questions){
                ps.setInt(1 , quizResult.getId());
                ps.setInt(2 , question.getQuestionId());
                ps.setString(3 , userAnswers.get(question));
                ps.addBatch();
            }
            int[] result  =  ps.executeBatch();
            if(result.length > 0){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return false;
    }
}