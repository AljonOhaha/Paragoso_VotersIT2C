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

    public void enrollInPrograms(int voterId) {
    String[] validPuroks = {
        "tabay dos", "tabay uno", "kalingawan", "kauswagan", 
        "tangason", "tower", "tambis uno", "tambis dos", 
        "sakaon", "skina gamay"
    };

    // First, check if the voter's Purok is valid
    String sqlCheckPurok = "SELECT RPUROK FROM registration WHERE RID = ?";
    try (Connection conn = connectDB();
         PreparedStatement pstmtCheckPurok = conn.prepareStatement(sqlCheckPurok)) {
        pstmtCheckPurok.setInt(1, voterId);
        ResultSet rs = pstmtCheckPurok.executeQuery();
        
        if (rs.next()) {
            String purok = rs.getString("RPUROK");
            boolean isValidPurok = false;

            // Check if the voter's Purok is in the list of valid Puroks
            for (String validPurok : validPuroks) {
                if (validPurok.equalsIgnoreCase(purok)) {
                    isValidPurok = true;
                    break;
                }
            }

            // If the Purok is valid, proceed with program enrollment
            if (isValidPurok) {
                Scanner sc = new Scanner(System.in);
                System.out.println("===== Barangay Programs =====");
                System.out.println("1. Scholarship");
                System.out.println("2. Sports League");
                System.out.print("Choose a program: ");
                int choice = sc.nextInt();

                String program = (choice == 1) ? "Scholarship" : "Sports League";
                String sql = "INSERT INTO programs (PVID, programs) VALUES (?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, voterId);
                    pstmt.setString(2, program);
                    pstmt.executeUpdate();
                    System.out.println("Successfully enrolled in: " + program);
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Voter's Purok is invalid. Enrollment failed.");
            }
        } else {
            System.out.println("No voter found with ID: " + voterId);
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

   public void viewVotersInPrograms() {
    // Updated SQL query to include the program information
    String sql = 
        "SELECT r.RPUROK AS Purok, " +
        "r.RFNAME || ' ' || r.RLNAME AS VoterName, " +
        "p.programs AS Program " +  // Include the program name
        "FROM registration r " +
        "INNER JOIN programs p " +
        "ON r.RID = p.PVID " +
        "ORDER BY r.RPUROK, r.RFNAME, r.RLNAME";

    try (Connection conn = connectDB();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        String currentPurok = "";
        System.out.println("\n===== Voters Enrolled in Programs =====");
        System.out.println("+--------------------------------------------------------------+");
        System.out.println("| Purok        | Voter Name               | Program          |");
        System.out.println("+--------------------------------------------------------------+");

        while (rs.next()) {
            String purok = rs.getString("Purok");
            String voterName = rs.getString("VoterName");
            String program = rs.getString("Program");  // Get the program name

            // Group by Purok
            if (!purok.equals(currentPurok)) {
                currentPurok = purok;
                System.out.println("\nPurok: " + purok);
            }

            // Display voter name and the program they are enrolled in with formatting
            System.out.printf("| %-12s | %-24s | %-16s |\n", purok, voterName, program);
        }

        System.out.println("+--------------------------------------------------------------+");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


   
}