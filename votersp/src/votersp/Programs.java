package votersp;


import java.sql.*;
import java.util.Scanner;

public class Programs {

    
    config conf = new config();
    public boolean validateAndEnrolls(int voterId, Scanner in) {
       
        Connection con = connectDB();
        if (con == null) {
            System.out.println("Unable to connect to the database.");
            return false;
        }

        try {
            // Validate Voter ID
            String sql = "SELECT * FROM voters WHERE voter_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, voterId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String status = rs.getString("status");

                System.out.println("\n===== Voter Found =====");
                System.out.println("Voter ID: " + voterId);
                System.out.println("Name: " + name);
                System.out.println("Registration Status: " + status);
                System.out.println("=========================");

                if (!status.equals("Registered")) {
                    System.out.println("You must be registered to enroll in programs.");
                    return false;
                }

                // Check for existing program enrollment
                String programCheckSql = "SELECT program_name FROM programs WHERE voter_id = ?";
                PreparedStatement pstProgramCheck = con.prepareStatement(programCheckSql);
                pstProgramCheck.setInt(1, voterId);
                ResultSet rsProgramCheck = pstProgramCheck.executeQuery();

                if (rsProgramCheck.next()) {
                    System.out.println("You are already enrolled in " + rsProgramCheck.getString("program_name") + ".");
                    return true;
                  
                }
                rsProgramCheck.close();

                // Show program options
                System.out.println("\nAvailable Programs:");
                System.out.println("1. Scholarship");
                System.out.println("2. Sports League");
                System.out.print("Select a program to enroll: ");
                int choice = in.nextInt();
                in.nextLine(); // Consume the newline

                String programName = null;
                switch (choice) {
                    case 1:
                        programName = "Scholarship";
                        break;
                    case 2:
                        programName = "Sports League";
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        return false;
                }

                // Enroll in the selected program
                String enrollSql = "INSERT INTO programs (voter_id, program_name) VALUES (?, ?)";
                PreparedStatement pstEnroll = con.prepareStatement(enrollSql);
                pstEnroll.setInt(1, voterId);
                pstEnroll.setString(2, programName);
                pstEnroll.executeUpdate();
                System.out.println("Successfully enrolled in " + programName + ".");

                rsProgramCheck.close();
                pstProgramCheck.close();
                return true;

            } else {
                System.out.println("Invalid Voter ID. You must be a registered voter.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public void postEnrollmentMenu(int voterId, Scanner in) {
        while (true) {
            System.out.println("\n===== Post-Enrollment Menu =====");
            System.out.println("1. View Program Details");
            System.out.println("2. Go Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    viewProgramOptions( in);
                    break;
                case 2:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void viewProgramOptions(Scanner in) {
        while (true) {
            System.out.println("\n===== Program View Options =====");
            System.out.println("1. View General Program Enrollment");
            System.out.println("2. View Individual Program Details");
            System.out.println("3. Back to Previous Menu");
            System.out.print("Enter your choice: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    viewGeneralProgramDetails();
                    break;
                case 2:
                    System.out.print("Enter Voter ID to view details: ");
                    int voterId = in.nextInt();
                    viewProgramDetails(voterId);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    public void viewGeneralProgramDetails() {
        String query = "SELECT voters.voter_id, voters.name, programs.program_name " +
                       "FROM voters " +
                       "INNER JOIN programs ON voters.voter_id = programs.voter_id " +
                       "ORDER BY voters.name ASC";

        // Define the headers for display
        String[] headers = {"Voter ID", "Name", "Enrolled Program"};
        String[] columns = {"voter_id", "name", "program_name"};

        // Verify and display records using the config method
        try {
            conf.viewRecords(query, headers, columns); // Ensure this method works correctly
        } catch (Exception e) {
            System.out.println("Error displaying general program details: " + e.getMessage());
        }
    }



   
    public void viewProgramDetails(int voterId) {
        Connection con = connectDB();
        if (con == null) {
            System.out.println("Unable to connect to the database.");
            return;
        }

        try {
            String sql = "SELECT * FROM voters WHERE voter_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, voterId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String purok = rs.getString("purok");
                String status = rs.getString("status");

                // Check enrolled program
                String programSql = "SELECT program_name FROM programs WHERE voter_id = ?";
                PreparedStatement pstProgram = con.prepareStatement(programSql);
                pstProgram.setInt(1, voterId);
                ResultSet rsProgram = pstProgram.executeQuery();

                String enrolledProgram = rsProgram.next() ? rsProgram.getString("program_name") : "No program enrolled.";

                // Display details
                System.out.println("\n===== Voter Details =====");
                System.out.println("Voter ID: " + voterId);
                System.out.println("Name: " + name);
                System.out.println("Purok: " + purok);
                System.out.println("Registration Status: " + status);
                System.out.println("Enrolled Program: " + enrolledProgram);
                System.out.println("=========================");
            } else {
                System.out.println("Voter ID not found.");
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:votersp.db");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

   

  
}
