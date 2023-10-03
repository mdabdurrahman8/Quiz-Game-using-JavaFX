package model;

import javafx.scene.control.TextField;

import java.sql.*;
import java.util.*;

public class Quiz {
    private Integer quizId;
    private String title;

    public class constant {
        public static final String TABLE_NAME = "quizess";
        public static final String QUIZ_ID = "id";
        public static final String TITLE = "title";

    }

    public Quiz() {
    }

    public Quiz(String title) {
        this.title = title;
    }

    public static void createTable() {
        try {
            String raw = "CREATE TABLE %s( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(50))";
            String query = String.format(raw, constant.TABLE_NAME,
                    constant.QUIZ_ID,
                    constant.TITLE);
            System.out.println(query);
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("Database is connected");
            System.out.println(b);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int save() {
        try {

            String raw = "INSERT INTO %s (%s) VALUES (?);";
            String query = String.format(raw, constant.TABLE_NAME, constant.TITLE);
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, this.title);
            int i = ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;

    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public boolean save(List<quizQuestion> questions) {
        boolean flag = true;
        this.quizId = this.save();

        for (quizQuestion q : questions) {
            flag = flag && q.save();
            System.out.println(flag);
        }
        return flag;
    }

    public static Map<Quiz, List<quizQuestion>> getAll() {
        Map<Quiz, List<quizQuestion>> quizzess = new HashMap<>();
        Quiz key = null;

            String query = String.
                    format("SELECT %s.%s , %s  ," +
                                    "%s.%s, %s , " +
                                    "%s , %s ," +
                                    " %s , %s , \n" +
                                    "%s\n" +
                                    "FROM %s join %s on %s.%s = %s.%s",
                            constant.TABLE_NAME,
                            constant.QUIZ_ID,
                            constant.TITLE,
                            quizQuestion.constant.TABLE_NAME,
                            quizQuestion.constant.QUESTION_ID,
                            quizQuestion.constant.QUESTION,
                            quizQuestion.constant.OPTION1,
                            quizQuestion.constant.OPTION2,
                            quizQuestion.constant.OPTION3,
                            quizQuestion.constant.OPTION4,
                            quizQuestion.constant.ANSWER,
                            constant.TABLE_NAME,
                            quizQuestion.constant.TABLE_NAME,
                            quizQuestion.constant.TABLE_NAME,
                            quizQuestion.constant.QUIZ_ID,
                            constant.TABLE_NAME,
                            constant.QUIZ_ID
                    );
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            System.out.println(query);
            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet result = ps.executeQuery();
                    while (result.next()) {
                        Quiz temp = new Quiz();
                        temp.setQuizId(result.getInt(1));
                        temp.setTitle(result.getString(2));

                        quizQuestion tempQuestion = new quizQuestion();
                        tempQuestion.setQuestionId(result.getInt(3));
                        tempQuestion.setQuestion(result.getString(4));
                        tempQuestion.setOption1(result.getString(5));
                        tempQuestion.setOption2(result.getString(6));
                        tempQuestion.setOption3(result.getString(7));
                        tempQuestion.setOption4(result.getString(8));
                        tempQuestion.setAnswer(result.getString(9));

                        if (key != null && key.equals(temp)) {
                            quizzess.get(key).add(tempQuestion);
                        } else {
                            ArrayList<quizQuestion> value = new ArrayList<>();
                            value.add(tempQuestion);
                            quizzess.put(temp, value);
                        }

                        key = temp;
                    }
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    return quizzess;
        }

        //get Questions using quiz
        public  List<quizQuestion> getQuestions() {
            List<quizQuestion> quizzess = new ArrayList<>();

            String query = String.
                    format("SELECT " +
                                    "%s, %s , " +
                                    "%s , %s ," +
                                    " %s , %s , \n" +
                                    "%s\n" +
                                    "FROM %s where %s=?",

                            quizQuestion.constant.QUESTION_ID,
                            quizQuestion.constant.QUESTION,
                            quizQuestion.constant.OPTION1,
                            quizQuestion.constant.OPTION2,
                            quizQuestion.constant.OPTION3,
                            quizQuestion.constant.OPTION4,
                            quizQuestion.constant.ANSWER,

                            quizQuestion.constant.TABLE_NAME,
                            quizQuestion.constant.QUIZ_ID

                    );
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            System.out.println(query);
            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection connection = DriverManager.getConnection(connectionUrl)) {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1,this.quizId);
                    ResultSet result = ps.executeQuery();
                    while (result.next()) {
                        quizQuestion tempQuestion = new quizQuestion();
                        tempQuestion.setQuestionId(result.getInt(1));
                        tempQuestion.setQuestion(result.getString(2));
                        tempQuestion.setOption1(result.getString(3));
                        tempQuestion.setOption2(result.getString(4));
                        tempQuestion.setOption3(result.getString(5));
                        tempQuestion.setOption4(result.getString(6));
                        tempQuestion.setAnswer(result.getString(7));

                        tempQuestion.setQuiz(this);
                        quizzess.add(tempQuestion);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return quizzess;
        }
    public static Map<Quiz, Integer> getAllWithQuestionCount() {
        Map<Quiz, Integer> quizes = new HashMap<>();
        Quiz key = null;

        String query = String.
                format("SELECT %s.%s,%s, COUNT(*) AS question_count\n" +
                                "FROM %s\n" +
                                " INNER JOIN %s " +
                                " ON %s.%s = %s.%s" +
                                " GROUP BY %s.%s",
                        constant.TABLE_NAME,
                        constant.QUIZ_ID,
                        constant.TITLE,
                        constant.TABLE_NAME,
                        quizQuestion.constant.TABLE_NAME,
                        constant.TABLE_NAME,
                        constant.QUIZ_ID,
                        quizQuestion.constant.TABLE_NAME,
                        quizQuestion.constant.QUIZ_ID,
                        constant.TABLE_NAME,
                        constant.QUIZ_ID
                );
        String connectionUrl = "jdbc:sqlite:quizzess.db";
        System.out.println(query);
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setQuizId(result.getInt(1));
                    temp.setTitle(result.getString(2));
                    int count = result.getInt(3);
                    quizes.put(temp, count);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizes;
    }

    public List<quizQuestion> getquizQuestions() {
        List<quizQuestion> quizes = new ArrayList<>();

        String query = String.
                format("SELECT " +
                                " %s , %s , " +
                                "%s , %s ," +
                                " %s , %s , \n" +
                                "%s\n" +
                                "FROM %s  where %s = ?",

                        quizQuestion.constant.QUESTION_ID,
                        quizQuestion.constant.QUESTION,
                        quizQuestion.constant.OPTION1,
                        quizQuestion.constant.OPTION2,
                        quizQuestion.constant.OPTION3,
                        quizQuestion.constant.OPTION4,
                       quizQuestion.constant.ANSWER,

                        quizQuestion.constant.TABLE_NAME,
                        quizQuestion.constant.QUIZ_ID

                );
        String connectionUrl = "jdbc:sqlite:quizzess.db";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, this.quizId);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    quizQuestion tempQuestion = new quizQuestion();
                    tempQuestion.setQuestionId(result.getInt(1));
                    tempQuestion.setQuestion(result.getString(2));
                    tempQuestion.setOption1(result.getString(3));
                    tempQuestion.setOption2(result.getString(4));
                    tempQuestion.setOption3(result.getString(5));
                    tempQuestion.setOption4(result.getString(6));
                    tempQuestion.setAnswer(result.getString(7));
                    tempQuestion.setQuiz(this);
                    quizes.add(tempQuestion);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return quizes;
    }


    public Integer getNumberOfQuestions() {
        String raw = "SELECT count(*) from %s WHERE %s = ?";
        String query = String.format(raw, quizQuestion.constant.TABLE_NAME
                , quizQuestion.constant.QUIZ_ID
        );


        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.quizId);
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



    public static Map<Quiz , Integer> getStudentCount() {
        Map<Quiz , Integer> data = new HashMap<>();
        String raw = "SELECT QUIZZES.ID , \n" +
                "QUIZZES.title  , \n" +
                "count(*) ,\n" +
                "QUIZ_RESULTS.id\n" +
                "FROM QUIZZES Left Join \n" +
                "QUIZ_RESULTS on \n" +
                "QUIZ_RESULTS.quiz_id = QUIZZES.ID \n" +
                "GROUP BY QUIZZES.id";
        String query = String.format(
                raw,
                constant.TABLE_NAME,
                constant.QUIZ_ID,

                constant.TABLE_NAME,
                constant.TITLE,

                QuizResult.constant.TABLE_NAME ,
                QuizResult.constant.ID,

                constant.TABLE_NAME,
                constant.TABLE_NAME,

                QuizResult.constant.TABLE_NAME,
                QuizResult.constant.QUIZ_ID,

                constant.TABLE_NAME,
                constant.QUIZ_ID,

                constant.TABLE_NAME,
                constant.QUIZ_ID

        );
        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                Quiz  quiz = new Quiz();
                quiz.setQuizId(result.getInt(1));
                quiz.setTitle(result.getString(2));
                int count = 0 ;
                Integer resultID = result.getInt(4);
                if(resultID > 0){
                    count = result.getInt(3);
                }
                data.put(quiz , count);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!(obj instanceof Quiz))
            return  false;
        Quiz t = (Quiz)obj;

        if(this.quizId == t.quizId){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, title);
    }
}

