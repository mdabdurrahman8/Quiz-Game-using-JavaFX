package model;

import java.sql.*;
import java.util.ArrayList;

public class Student {
    private static String name;
    private static String email;
    private static String gender;
    private static String password;
    private int id;

    public Student(String name, String email, String gender, String password) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }
    public Student(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Student() {
    }

    int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class constant {
        public static final String TABLE_NAME = "Students";

        public static final String Name = "name";
        public static final String EMAIL = "email";
        public static final String GENDER = "gender";
        public static final String PASSWORD = "password";
        public static final String ID = "ID";

    }

    public static void createTable() {
        try {
            String raw = "CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s VARCHAR(100)," +
                    "%s VARCHAR(100)," +
                    "%s VARCHAR(100)," +
                    "%s VARCHAR(100))";
            String query = String.format(raw, constant.TABLE_NAME,
                    constant.Name,
                    constant.EMAIL,
                    constant.GENDER,
                    constant.PASSWORD);
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

    public boolean isExists() {
        try {
            String query = "SELECT * FROM students WHERE email = ?";
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, this.email);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    public boolean save() {
        try {
            String query;
            if (isExists()) {
                // Update existing record
                query = "UPDATE Students SET name=?, gender=?, password=? WHERE email=?";
            } else {
                // Create new record
                query = "INSERT INTO Students (name, email, gender, password) VALUES (?, ?, ?, ?)";
            }
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, this.name);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.gender);
            preparedStatement.setString(4, this.password);
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }


    public static ArrayList<Student> getAll() {
        ArrayList<Student> students = new ArrayList<>();

        String query = String.format("select %s, %s, %s, %s from %s;",
                constant.Name,
                constant.EMAIL,
                constant.GENDER,
                constant.PASSWORD,
                constant.TABLE_NAME
        );
        String connectionUrl = "jdbc:sqlite:quizzess.db";
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);

                ResultSet result = ps.executeQuery();
                while (result.next()) {
                    Student st = new Student();
                    st.setName(result.getString(1));
                    st.setEmail(result.getString(2));
                    st.setGender(result.getString(3));
                    st.setPassword(result.getString(4));
                    students.add(st);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
    }

    public boolean update(String name, String email, String gender, String password) throws Exception {
        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement("UPDATE students SET name=?, email=?, gender=?, password=?");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, gender);
            ps.setString(4, password);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                this.setName(name);
                this.setEmail(email);
                this.setGender(gender);
                this.setPassword(password);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        try {
            String connectionUrl = "jdbc:sqlite:quizzess.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            String sql = "DELETE FROM students WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, this.name);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error deleting student: ");
            e.printStackTrace();
            return false;
        }

    }
}