package votersp;

import java.sql.*;
import java.util.Scanner;
import votersp.Programs;
public class Registration {

    private static PurokListing PurokListing;
    private static Object programs;

    private Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:votersp.db");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
        return con;
    }

  public int addVoter() {
    Scanner sc = new Scanner(System.in);
    System.out.println("===== Voter Registration =====");
    String rfname, rlname, rpurok;
    int age;

    // Input Validation for First Name and Last Name
    System.out.print("Enter First Name: ");
    rfname = sc.nextLine();
    System.out.print("Enter Last Name: ");
    rlname = sc.nextLine();

    // Age Input Validation (only accepts integers)
    while (true) {
        System.out.print("Enter Age: ");
        if (sc.hasNextInt()) {
            age = sc.nextInt();  // If input is a valid integer, proceed
            sc.nextLine(); // Clear buffer
            break;  // Exit the loop once valid input is received
        } else {
            System.out.println("Invalid input! Please enter a valid integer for age.");
            sc.nextLine(); // Clear the invalid input
        }
    }

    // Purok Input
    System.out.print("Enter Purok: ");
    rpurok = sc.nextLine();

    // SQL query to insert the data
    String sql = "INSERT INTO registration (RFNAME, RLNAME, AGE, RPUROK) VALUES (?, ?, ?, ?)";
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, rfname);
        pstmt.setString(2, rlname);
        pstmt.setInt(3, age);
        pstmt.setString(4, rpurok);
        pstmt.executeUpdate();

        ResultSet keys = pstmt.getGeneratedKeys();
        if (keys.next()) {
            System.out.println("Registration successful!");
            return keys.getInt(1); // Return voter ID
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return -1;  // Return -1 if there is an error
}


    // View all registered voters
  public void viewVoters() {
    // Updated SQL query to include the AGE column
    String sql = "SELECT RID, RFNAME, RLNAME, RPUROK, AGE FROM registration";
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        System.out.println("\n===== Registered Voters =====");
        System.out.println("+----------------------------------------------------+");
        System.out.println("| ID   | Name                   | Purok        | Age |");
        System.out.println("+----------------------------------------------------+");

        while (rs.next()) {
            int voterId = rs.getInt("RID");
            String firstName = rs.getString("RFNAME");
            String lastName = rs.getString("RLNAME");
            String purok = rs.getString("RPUROK");
            int age = rs.getInt("AGE");  // Fetch the Age

            // Print each voter's data in a structured format, including Age
            System.out.printf("| %-4d | %-21s | %-12s | %-3d |\n", voterId, firstName + " " + lastName, purok, age);
        }

        System.out.println("+----------------------------------------------------+");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


    
    
    public void deleteVoter() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Voter ID to delete: ");
        int voterId = sc.nextInt();

        // SQL query to delete the voter
        String sql = "DELETE FROM registration WHERE RID = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, voterId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Voter with ID " + voterId + " has been deleted.");
            } else {
                System.out.println("No voter found with ID " + voterId);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

   





   
     public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String another = null;

    // Initialize objects
    Programs programs = new Programs();
    Registration registration = new Registration();
    PurokListing purokListing = new PurokListing();

    do {
        // Main Menu
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Voter Registration");
        System.out.println("2. Purok Listings");
        System.out.println("3. Programs");
        System.out.println("4. Exit");
        System.out.print("Enter your action: ");
        int action = in.nextInt();

        switch (action) {
            case 1:
                voterRegistrationMenu(in, registration);
                break;
            case 2:
                purokMenu(in, purokListing);
                break;
            case 3:
                programMenu(in, programs);
                break;
            case 4:
                System.out.println("Exiting the system...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }

        // Ask if the user wants to continue
        System.out.println("Continue (yes|no)?");
        another = in.next();

    } while (another.equalsIgnoreCase("yes"));

    System.out.println("Thank you for using the system!");
}

// Voter Registration Menu
private static void voterRegistrationMenu(Scanner in, Registration registration) {
    int action;
    do {
        System.out.println("\n===== Voter Registration Menu =====");
        System.out.println("1. Add Voter");
        System.out.println("2. View Voters");
        System.out.println("3. Delete Voters");
      
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your action: ");
        action = in.nextInt();

        switch (action) {
            case 1:
                registration.addVoter();
                break;
            case 2:
                registration.viewVoters();
                break;
            case 3:
                registration.deleteVoter();
                break;
            case 4:
                System.out.println("Returning to Main Menu...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    } while (action != 4);
}

// Purok Listing Menu
private static void purokMenu(Scanner in, PurokListing purokListing) {
    int action;
    do {
        System.out.println("\n===== Purok Listing Menu =====");
        System.out.println("1. Validate Purok for Voter");
        System.out.println("2. View Purok Listings");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your action: ");

        // Validate numeric input
        while (!in.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print("Enter your action: ");
            in.next();
        }
        action = in.nextInt();
        in.nextLine(); // Clear the input buffer

        switch (action) {
            case 1:
                System.out.print("Enter Voter ID to validate: ");
                while (!in.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid Voter ID.");
                    System.out.print("Enter Voter ID to validate: ");
                    in.next();
                }
                int voterId = in.nextInt();
                in.nextLine(); // Clear the buffer
                purokListing.validatePuroks(voterId);
                break;
            case 2:
                purokListing.viewPuroks();
                break;
            case 3:
                System.out.println("Returning to Main Menu...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    } while (action != 3);
}

// Programs Menu
private static void programMenu(Scanner in, Programs programs) {
    int action;
    do {
        System.out.println("===== Programs Menu =====");
        System.out.println("1. Enroll in Programs");
        System.out.println("2. View Voters in Programs");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an action: ");
        action = in.nextInt();

        switch (action) {
            case 1:
                // Ask the user for the voter ID
                System.out.print("Enter Voter ID to enroll: ");
                int voterId = in.nextInt();  // Get voter ID input from user

                // Enroll the voter in programs
                programs.enrollInPrograms(voterId);
                break;
            case 2:
                // View all voters in programs
                programs.viewVotersInPrograms();
                break;
            case 3:
                System.out.println("Returning to Main Menu...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    } while (action != 3);
}

}