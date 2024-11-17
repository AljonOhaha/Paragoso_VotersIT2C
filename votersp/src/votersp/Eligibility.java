package votersp;

import java.sql.*;
import java.util.Scanner;

public class Eligibility {
    private static final int MIN_ELIGIBLE_AGE = 15;
    private static final int MAX_ELIGIBLE_AGE = 30;

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

  public void addEligibility() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter Registration ID: ");
    int regId = sc.nextInt();
    sc.nextLine(); 

    String sqlCheckRegistration = "SELECT COUNT(*) FROM registration WHERE RID = ?";
    String sqlCheckEligibility = "SELECT COUNT(*) FROM eligibility WHERE RRID = ?";
    String sqlSelect = "SELECT RFNAME, RLNAME, AGE, RPUROK FROM registration WHERE RID = ?";
    String sqlInsert = "INSERT INTO eligibility (RRID, fname, lname, purok, eage, status) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = connectDB();
         PreparedStatement pstmtCheckRegistration = conn.prepareStatement(sqlCheckRegistration);
         PreparedStatement pstmtCheckEligibility = conn.prepareStatement(sqlCheckEligibility);
         PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
         PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

        
        pstmtCheckRegistration.setInt(1, regId);
        ResultSet rsCheckRegistration = pstmtCheckRegistration.executeQuery();
        if (rsCheckRegistration.next() && rsCheckRegistration.getInt(1) == 0) {
            System.out.println("Error: Registration ID does not exist. Please provide a valid ID.");
            return; 
        }

        
        pstmtCheckEligibility.setInt(1, regId);
        ResultSet rsCheckEligibility = pstmtCheckEligibility.executeQuery();
        if (rsCheckEligibility.next() && rsCheckEligibility.getInt(1) > 0) {
            System.out.println("Error: Eligibility record already exists for this Registration ID.");
            return; 
        }

        
        pstmtSelect.setInt(1, regId);
        ResultSet rsSelect = pstmtSelect.executeQuery();
        if (rsSelect.next()) {
            String fname = rsSelect.getString("RFNAME");
            String lname = rsSelect.getString("RLNAME");
            int age = rsSelect.getInt("AGE");
            String purok = rsSelect.getString("RPUROK");

            
            String status = (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE)
                    ? "Eligible for SK programs"
                    : "Not eligible for SK programs";

            
            pstmtInsert.setInt(1, regId);
            pstmtInsert.setString(2, fname);
            pstmtInsert.setString(3, lname);
            pstmtInsert.setString(4, purok);
            pstmtInsert.setInt(5, age);
            pstmtInsert.setString(6, status);
            pstmtInsert.executeUpdate();

            System.out.println("Eligibility record added successfully!");
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

   public void viewEligibility() {
    String sql = "SELECT VID, fname, lname, eage, purok, status FROM eligibility";

    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        System.out.printf("%-5s %-15s %-15s %-5s %-10s %-25s%n", 
                          "ID", "First Name", "Last Name", "Age", "Purok", "Status");
        System.out.println("-----------------------------------------------------------------------");

        while (rs.next()) {
            int vid = rs.getInt("VID");
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            int age = rs.getInt("eage");
            String purok = rs.getString("purok");
            String status = rs.getString("status");

            System.out.printf("%-5d %-15s %-15s %-5d %-10s %-25s%n", 
                              vid, fname, lname, age, purok, status);
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving eligibility data: " + e.getMessage());
    }
}

   public void updateEligibility() {
    Scanner sc = new Scanner(System.in);
    
    // Prompt for Eligibility ID to update
    System.out.print("Enter Eligibility ID to Update: ");
    int vid = sc.nextInt();
    sc.nextLine(); // Clear the buffer

    // Prompt for new details to update
    System.out.print("Enter New First Name: ");
    String newFname = sc.nextLine();

    System.out.print("Enter New Last Name: ");
    String newLname = sc.nextLine();

    System.out.print("Enter New Age: ");
    int newAge = sc.nextInt();
    sc.nextLine(); // Clear the buffer

    System.out.print("Enter New Purok: ");
    String newPurok = sc.nextLine();

    // Determine eligibility status based on the new age
    String newStatus = (newAge >= MIN_ELIGIBLE_AGE && newAge <= MAX_ELIGIBLE_AGE)
            ? "Eligible for SK programs"
            : "Not eligible for SK programs";

    // SQL to update only fname, lname, purok, age, and status
    String sqlUpdate = "UPDATE eligibility SET fname = ?, lname = ?, purok = ?, eage = ?, status = ? WHERE VID = ?";
    String sqlSelectUpdated = "SELECT VID, fname, lname, eage, purok, status FROM eligibility WHERE VID = ?";

    try (Connection conn = connectDB();
         PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
         PreparedStatement pstmtSelectUpdated = conn.prepareStatement(sqlSelectUpdated)) {

        // Update the eligibility record with new values
        pstmtUpdate.setString(1, newFname);
        pstmtUpdate.setString(2, newLname);
        pstmtUpdate.setString(3, newPurok);
        pstmtUpdate.setInt(4, newAge);
        pstmtUpdate.setString(5, newStatus);
        pstmtUpdate.setInt(6, vid);

        int rowsAffected = pstmtUpdate.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Eligibility record updated successfully!");

            // Retrieve and display the updated record
            pstmtSelectUpdated.setInt(1, vid);
            ResultSet rsUpdated = pstmtSelectUpdated.executeQuery();

            if (rsUpdated.next()) {
                System.out.println("\nUpdated Eligibility Record:");
                System.out.printf("%-5s %-15s %-15s %-5s %-10s %-25s%n", 
                                  "ID", "First Name", "Last Name", "Age", "Purok", "Status");
                System.out.println("-----------------------------------------------------------------------");
                
                int updatedVid = rsUpdated.getInt("VID");
                String updatedFname = rsUpdated.getString("fname");
                String updatedLname = rsUpdated.getString("lname");
                int updatedAge = rsUpdated.getInt("eage");
                String updatedPurok = rsUpdated.getString("purok");
                String updatedStatus = rsUpdated.getString("status");

                System.out.printf("%-5d %-15s %-15s %-5d %-10s %-25s%n", 
                                  updatedVid, updatedFname, updatedLname, updatedAge, updatedPurok, updatedStatus);
            }
        } else {
            System.out.println("No eligibility record found with ID: " + vid);
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    public void deleteEligibility() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Eligibility ID to Delete: ");
        int vid = sc.nextInt();

        String sql = "DELETE FROM eligibility WHERE VID = ?";

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vid);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Eligibility record deleted successfully!");
            } else {
                System.out.println("No eligibility record found with ID: " + vid);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting eligibility record: " + e.getMessage());
        }
    }
}