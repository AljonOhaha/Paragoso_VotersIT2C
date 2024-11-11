package votersp;

import java.sql.*;
import java.util.Scanner;

public class Eligibility {
private static final int MIN_ELIGIBLE_AGE = 15;
    private static final int MAX_ELIGIBLE_AGE = 30;
    private static final String DEFAULT_PUROK = "Unknown";
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

       
        System.out.print("Enter Voter First Name: ");
        String fname = sc.nextLine();

       
        System.out.print("Enter Voter Last Name: ");
        String lname = sc.nextLine();

        
        System.out.print("Enter Purok Name: ");
        String purok = sc.nextLine();
        if (purok.isEmpty()) {
            purok = DEFAULT_PUROK; 
        }

       
        int age = 0;
        boolean validAge = false;
        while (!validAge) {
            System.out.print("Enter Voter Age: ");
            try {
                age = Integer.parseInt(sc.nextLine());
               
                if (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE) {
                    validAge = true;
                } else {
                    System.out.println("Age must be between 15 and 30 for SK eligibility.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for age. Please enter a valid integer.");
            }
        }

       
        String eligibilityStatus = (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE) 
            ? "Eligible for SK programs" 
            : "Not eligible for SK programs";

       
        String sql = "INSERT INTO eligibility (fname, lname, Purok, Age, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, purok);
            pstmt.setInt(4, age);
            pstmt.setString(5, eligibilityStatus); // Add eligibility status to the database

            pstmt.executeUpdate(); // Execute the insert query
            System.out.println("Voter information added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding voter information: " + e.getMessage());
        }
    }

    
    public void viewEligibility() {
        String sql = "SELECT * FROM eligibility"; 
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

          
            System.out.println("-----------------------------------------------------------");
            System.out.printf("%-5s %-20s %-15s %-5s%n", "ID", "Name", "Age", "Status");
            System.out.println("-----------------------------------------------------------");

           
            while (rs.next()) {
                int id = rs.getInt("VID");           
                String firstName = rs.getString("fname");   
                String lastName = rs.getString("lname");   
                int age = rs.getInt("Age");         
                String eligibilityStatus = (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE)
                    ? "Eligible for SK programs" : "Not eligible for SK programs"; 

                System.out.printf("%-5d %-20s %-15d %-5s%n", id, firstName + " " + lastName, age, eligibilityStatus);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving eligibility: " + e.getMessage());
        }
    }

   
    public void updateEligibility() {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Enter Eligibility ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();  

        
        System.out.print("Enter New Voter First Name: ");
        String fname = sc.nextLine();

       
        System.out.print("Enter New Voter Last Name: ");
        String lname = sc.nextLine();

     
        int age = 0;
        boolean validAge = false;
        while (!validAge) {
            System.out.print("Enter New Voter Age: ");
            try {
                age = Integer.parseInt(sc.nextLine());
              
                if (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE) {
                    validAge = true;
                } else {
                    System.out.println("Age must be between 15 and 30 for SK eligibility.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for age. Please enter a valid integer.");
            }
        }

        
        String eligibilityStatus = (age >= MIN_ELIGIBLE_AGE && age <= MAX_ELIGIBLE_AGE)
            ? "Eligible for SK programs"
            : "Not eligible for SK programs";

      
        String sql = "UPDATE eligibility SET fname = ?, lname = ?, Age = ?, status = ? WHERE VID = ?";

        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

           
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setInt(3, age);
            pstmt.setString(4, eligibilityStatus);
            pstmt.setInt(5, id);  

            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Eligibility updated successfully!");
            } else {
                System.out.println("No record found with the specified ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating eligibility: " + e.getMessage());
        }
    }


    
    public void deleteEligibility() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Eligibility ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM eligibility WHERE VID = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Eligibility deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting eligibility: " + e.getMessage());
        }
    }
}
