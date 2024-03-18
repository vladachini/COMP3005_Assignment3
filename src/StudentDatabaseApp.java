package src;
import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class StudentDatabaseApp {
    //URL used to connect to the Postgres DB
    private static final String url = "jdbc:postgresql://localhost:5432/Assignment3";
    //Username used to login to Postgres DB
    private static final String user = "postgres";
    //Password for logging in Postgres DB
    private static final String password = "kovacina2002";

    public static void main(String[] args) {
        getAllStudents();
        addStudent("Vladimir","Kovacina","vladimirkovacina@cmail.carleton.ca","2020-09-30");
        System.out.println("After adding new Student: ");
        getAllStudents();
        System.out.println("Changing email for Vladimir:");
        updateStudentEmail(4,"vlad.kovacina@gmail.com");
        getAllStudents();
        System.out.println("Deleting Vladimir:");
        deleteStudent(4);
        getAllStudents();



    }

    //Method used to connect to the Postgres DB, returns the connection
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    /**
     * Retrieves and displays all records from the students table
     */
    public static void getAllStudents() {
        try{
            //Class.forName("org.postgres.Driver");
            //SQL statement to execute
            String sql = "SELECT * FROM students";
            //Try establishing connection to the DB
            Connection conn = connect();
            if(conn != null){ //If we were able to connect to DB then proceed
                Statement stmt = conn.createStatement(); //create a statement on the established connection
                ResultSet rs = stmt.executeQuery(sql); //Get the set of results retrieved from executing the SQL statement
                while (rs.next()){//Iterate through the result set and print out the tuples
                    System.out.println(rs.getInt("student_id") + " " +
                            rs.getString("first_name") + " " +
                            rs.getString("last_name") + " " +
                            rs.getString("email") + " " +
                            rs.getDate("enrollment_date"));
                }
            }else{ //Couldn't connect to the DB so don't execute query
                System.out.println("Failed to establish connection");
            }
            conn.close(); //Close the connection when finished
        }catch (SQLException e){ //catch any SQL error
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new student record into the students table
     * @param firstName: first name of student
     * @param lastName: last name of student
     * @param email: email of student
     * @param enrollmentDate: enrollment Date (YYYY-MM-DD)
     */
    public static void addStudent(String firstName, String lastName, String email, String enrollmentDate){
        //Create SQL statement to use for query
        String SQL = "INSERT INTO students(first_name, last_name, email, enrollment_date) VALUES (?,?,?,?)";
        try{
            //Class.forName("org.postgres.Driver");
            Connection conn = connect();//establish connection to DB
            PreparedStatement pstmt = conn.prepareStatement(SQL); //create a prepare statement, so that we can dynamically add parameters
            if(conn != null){ //Make sure connection was properly established
                //Set the data in the SQL string to the parameters passed in
                pstmt.setString(1,firstName);
                pstmt.setString(2,lastName);
                pstmt.setString(3,email);
                pstmt.setDate(4, java.sql.Date.valueOf(enrollmentDate));
                pstmt.executeUpdate(); //Execute the query
            }else{
                System.out.println("Failed to establish connection");
            }
            conn.close(); //close connection
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the email address for a student with the specified student_id
     * @param studentId The specified student id to update
     * @param newEmail the new email address to change to
     */
    public static void updateStudentEmail(int studentId, String newEmail){
        String SQL = "UPDATE students SET email = ? WHERE student_id = ?"; //Create SQL segment to execute
        try{
            //Class.forName("org.postgres.Driver");
            Connection conn = connect(); //Establish Connection to Postgres DB
            PreparedStatement pstmt = conn.prepareStatement(SQL);//Prepare a statement, the SQL string, to execute in the connection
            if(conn != null){ //Check connection is established
                //Dynamically set the values in the SQL statement
                pstmt.setString(1,newEmail);
                pstmt.setInt(2,studentId);
                pstmt.executeUpdate(); //Execute the prepared statement
            }else{
                System.out.println("Failed to establish connection");
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Delete the record of the student with the specified student_id
     * @param studentId student_id to delete
     */
    public static void deleteStudent(int studentId){
        String SQL = "DELETE FROM students WHERE student_id = ?"; //SQL statement to prepare and execute
        try{
            //Class.forName("org.postgres.Driver");
            Connection conn = connect(); //Establish Connection to the DB
            PreparedStatement pstmt = conn.prepareStatement(SQL); //Prepare statement to dynamically modify and execute
            if(conn != null){ //Check if connection is established
                pstmt.setInt(1,studentId); //Update SQL statement with parameter passed in
                pstmt.executeUpdate();//Execute the prepared statement
            }else{
                System.out.println("Failed to establish connection");
            }
            conn.close();//close established connection
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
