package votersp;

import java.sql.*;
import java.util.Scanner;

public class Programs {
     private static final String SPORTS_LEAGUE_PROGRAM = "Sports League";
    private static final String SCHOLARSHIP_PROGRAM = "Scholarship";

    private Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:votersp.db");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

    public void enrollInProgram() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Voter ID: ");
        int id = sc.nextInt();
        System.out.print("Choose Program (1 - Scholarship, 2 - Sports League): ");
        int program = sc.nextInt();

        String programName = program == 1 ? "Scholarship" : "Sports League";
        String sql = "INSERT INTO programs (PVID, programs) VALUES (?, ?)";
        
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, programName);
            pstmt.executeUpdate();
            System.out.println("Enrolled in " + programName);
        } catch (SQLException e) {
            System.out.println("Error enrolling in program: " + e.getMessage());
        }
    }

    public void viewPrograms() {
         String sql = "SELECT PID , programs, fname, lname  " +
                     "FROM programs  " +
                     "JOIN eligibility e ON programs.PVID = e.VID";

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.printf("%-10s %-20s%n", "Voter ID", "Program");
            System.out.println("---------------------------");

            while (rs.next()) {
                int voterID = rs.getInt("PID");
                String programName = rs.getString("programs");
                String voterName = rs.getString("fname") + " " + rs.getString("lname");

                System.out.printf("%-10d %-20s %-25s%n", voterID, programName, voterName);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving programs: " + e.getMessage());
        }
    }
}


