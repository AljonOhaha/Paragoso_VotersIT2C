package votersp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Registration {
    
    
      
     public Connection connectDB() {
    try {
        // Update the path to your actual database file
        String url = "jdbc:sqlite:votersp.db";  // Absolute path
        Connection con = DriverManager.getConnection(url);
        System.out.println("Connection Successful");
        return con;
    } catch (SQLException e) {
        System.out.println("Database connection error: " + e.getMessage());
    }
    return null;
}

public void registerVoter(Scanner in) {
    boolean isRegistered = false; // Track registration status
    int currentVoterId = -1;      // Store registered voter ID

    Connection con = connectDB();
    if (con == null) {
        System.out.println("Unable to connect to the database.");
        return;
    }

    while (true) { // Keep showing the menu until the user decides to return to the main menu
        if (!isRegistered) {
            // Registration menu
            System.out.println("\n===== Registration Menu =====");
            System.out.println("1. Register Voter");
            System.out.println("2. Go Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = in.nextInt();
            in.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Start voter registration
                    System.out.print("Enter your Registered Voter ID: ");
                    int voterId = in.nextInt();
                    in.nextLine(); // Consume the newline

                    try {
                        // Validate if the voter ID exists in the database
                        String sql = "SELECT * FROM voters WHERE voter_id = ?";
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, voterId);
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {
                            // Voter ID is valid
                            String name = rs.getString("name");
                            String purok = rs.getString("purok");
                            String status = rs.getString("status");

                            System.out.println("\n===== Voter Found =====");
                            System.out.println("Voter ID: " + voterId);
                            System.out.println("Name: " + name);
                            System.out.println("Purok: " + purok);
                            System.out.println("Status: " + status);
                            System.out.println("=========================");

                            if (status.equals("Not Enrolled")) {
                                // Update the status to "Registered"
                                String updateSql = "UPDATE voters SET status = ? WHERE voter_id = ?";
                                PreparedStatement updatePst = con.prepareStatement(updateSql);
                                updatePst.setString(1, "Registered");
                                updatePst.setInt(2, voterId);
                                updatePst.executeUpdate();
                                System.out.println("Successfully registered as a voter!");

                                currentVoterId = voterId; // Store voter ID
                                isRegistered = true;     // Update registration status
                            } else {
                                System.out.println("You are already registered.");
                                currentVoterId = voterId; // Store voter ID
                                isRegistered = true;     // Update registration status
                            }
                        } else {
                            // Voter ID does not exist in the database
                            System.out.println("Invalid Voter ID. You must be a registered voter in this barangay.");
                        }

                        rs.close();
                        pst.close();
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Returning to main menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } else {
            // View details menu (post-registration)
            System.out.println("\n===== Post-Registration Menu =====");
            System.out.println("1. View Voter Details");
            System.out.println("2. Go Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = in.nextInt();
            in.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // View voter details
                    viewVoterDetails(currentVoterId);
                    break;

                case 2:
                    System.out.println("Returning to main menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}



public void viewVoterDetails(int voterId) {
    Connection con = connectDB();
    if (con == null) {
        System.out.println("Unable to connect to the database.");
        return;
    }

    try {
        // Fetch voter details from the database
        String sql = "SELECT * FROM voters WHERE voter_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, voterId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Retrieve voter details
            String name = rs.getString("name");
            String purok = rs.getString("purok");
            String status = rs.getString("status");

            // Output voter details
            System.out.println("\n===== Voter Details =====");
            System.out.println("Voter ID: " + voterId);
            System.out.println("Name: " + name);
            System.out.println("Purok: " + purok);
            System.out.println("Status: " + status);
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

}
