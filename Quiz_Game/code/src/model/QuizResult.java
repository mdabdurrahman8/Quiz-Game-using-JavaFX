package model;


import java.sql.*;
import java.util.*;
import java.util.Date;

import static model.QuizResultDetails.saveQuizResultDetails;

public class QuizResult {
    private Integer id ;
    private  Quiz quiz ;
    private Student student ;
    private Integer rightAnswers ;
    private Timestamp timestamp;
    public static  class constant{
        public static final String TABLE_NAME ="QUIZ_RESULTS";
        public static final String ID ="id";
        public static final String QUIZ_ID ="QUIZ_ID";
        public static final String STUDENT_ID ="STUDENT_ID";
        public static final String RIGHT_ANSWERS ="RIGHT_ANSWERS";
        public static final String TIMESTAMP ="date_time";
    }

    {
        timestamp = new Timestamp(new Date().getTime());
    }

    public QuizResult() {

    }

    public QuizResult(Integer id, Quiz quiz, Student student, Integer rightAnswers) {
        this.id = id;
        this.quiz = quiz;
        this.student = student;
        this.rightAnswers = rightAnswers;
    }

    public QuizResult(Quiz quiz, Student student, Integer rightAnswers) {
        this.quiz = quiz;
        this.student = student;
        this.rightAnswers = rightAnswers;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(Integer rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public static void createTable(){

        String raw = "CREATE table %s (\n" +
                "        %s Integer not null PRIMARY key AUTOINCREMENT,\n" +
                "        %s int not null,\n" +
                "        %s int not null ,\n" +
                "        %s int not null ,\n" +
                "        %s timestamp NOT null ,\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s)\n" +
                "        )";
        String query  = String.format(raw,
                constant.TABLE_NAME ,
                constant.ID,
                constant.STUDENT_ID,
                constant.QUIZ_ID,
                constant.RIGHT_ANSWERS,
                constant.TIMESTAMP,
                constant.STUDENT_ID,
                Quiz.constant.TABLE_NAME ,
                Quiz.constant.QUIZ_ID ,
                constant.STUDENT_ID ,
                Student.constant.TABLE_NAME,
                Student.constant.ID
        );

        System.err.println(query);
        try{
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.QuizResults.createTable()");
            System.out.println(b);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    public boolean save(Map<quizQuestion , String> userAnswers){
        String raw = "INSERT INTO %s (%s , %s , %s , %s ) values \n" +
                "( ?, ? , ?  , CURRENT_TIMESTAMP)";
        String query  = String.format(raw,
                constant.TABLE_NAME ,
                constant.STUDENT_ID,
                constant.QUIZ_ID,
                constant.RIGHT_ANSWERS,
                constant.TIMESTAMP);

        try{
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1 , this.getStudent().getId());
            ps.setInt(2 , this.getQuiz().getQuizId());
            ps.setInt(3 , this.getRightAnswers());
            int result =  ps.executeUpdate();
            if(result > 0){
                ResultSet keys = ps.getGeneratedKeys();
                if(keys.next()){
                    this.setId(keys.getInt(1));
                    // save details
                    System.out.println(this);
                    return saveQuizResultDetails(userAnswers);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        return false;
    }
    public static Map<QuizResult , Quiz> getQuizzes(Student student){

        Map<QuizResult ,  Quiz > data = new HashMap<>();
        String raw = "SELECT %s.%s ,  " +
                "%s.%s ," +
                " %s.%s as quiz_id , " +
                "%s.%s FROM %s " +
                "JOIN %s on " +
                "%s.%s = %s.%s WHERE %s = ?";

        String query = String.format(raw ,
                constant.TABLE_NAME,
                constant.ID,
                constant.TABLE_NAME,
                constant.RIGHT_ANSWERS,
                Quiz.constant.TABLE_NAME,
                Quiz.constant.QUIZ_ID,
                Quiz.constant.TABLE_NAME,
                Quiz.constant.TITLE,
                constant.TABLE_NAME,
                Quiz.constant.TABLE_NAME,
                constant.TABLE_NAME,
                constant.QUIZ_ID ,
                Quiz.constant.TABLE_NAME,
                Quiz.constant.QUIZ_ID ,
                constant.STUDENT_ID
        );

        try{
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , student.getId());
            ResultSet result  =  ps.executeQuery();

            while (result.next()){
                QuizResult quizResult = new QuizResult();
                quizResult.setId(result.getInt(1));
                quizResult.setRightAnswers(result.getInt(2));

                Quiz quiz = new Quiz();
                quiz.setQuizId(result.getInt(3));
                quiz.setTitle(result.getString(4));

                data.put(quizResult , quiz);
            }


        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return data;
    }

    public static List<Student> getStudents(Quiz quiz){
        List<Student> students = new ArrayList();
        String raw = "SELECT st.%s , st.%s ,\n" +
                "st.%s , st.%s ,\n" +
                "st.%s , st.%s \n" +
                "FROM %s  As qr\n" +
                "join %s as st\n" +
                "on st.%s = qr.%s\n" +
                "WHERE %s = ? GROUP by %s";
        String query = String.format(raw ,
                Student.constant.ID,
                Student.constant.Name,
                Student.constant.EMAIL,
                Student.constant.GENDER,
                constant.TABLE_NAME,
                Student.constant.TABLE_NAME,
                Student.constant.ID,
                constant.STUDENT_ID,
                constant.QUIZ_ID,
                constant.STUDENT_ID
        );

        try{
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , quiz.getQuizId());
            ResultSet result  =  ps.executeQuery();

            while (result.next()){
                Student student = new Student();
                student.setId(result.getInt(1));
                student.setName(result.getString(2));
                student.setEmail(result.getString(3));
                student.setGender(result.getString(4));
                students.add(student);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return students;

    }
    public Integer getNumberOfAttemptedQuestions() {
        String raw = "SELECT COUNT(*) FROM %s  WHERE  %s = ?";
        String query = String.format(raw, QuizResultDetails.constant.TABLE_NAME
                , QuizResultDetails.constant.QUIZ_RESULT_ID
        );


        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.getId());
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                return result.getInt(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private boolean  saveQuizResultDetails(Map<quizQuestion , String> userAnswers){
        return QuizResultDetails.saveQuizResultDetails(this , userAnswers);
    }


    public static List<QuizResult> getResult(Student student){
        Map<QuizResult , Quiz > quizResultQuizMap = getQuizzes(student);
        return  new ArrayList(quizResultQuizMap.keySet());
    }

}
