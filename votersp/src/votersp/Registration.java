package votersp;

import java.sql.*;
import java.util.Scanner;

public class Registration {
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

    public void addVoter() {
        Scanner sc = new Scanner(System.in);
        String rfname;
        String rlname;
        int age;
        String rpurok;

        // Validate First Name
        while (true) {
            System.out.print("Voter's First Name: ");
            rfname = sc.nextLine().trim();
            if (!rfname.isEmpty()) {
                break;
            } else {
                System.out.println("First name cannot be empty. Please enter a valid first name.");
            }
        }

        // Validate Last Name
        while (true) {
            System.out.print("Voter's Last Name: ");
            rlname = sc.nextLine().trim();
            if (!rlname.isEmpty()) {
                break;
            } else {
                System.out.println("Last name cannot be empty. Please enter a valid last name.");
            }
        }

        // Validate Age
        {
            System.out.print("Voter's Age: ");
            if (sc.hasNextInt()) {
                age = sc.nextInt();
                sc.nextLine(); // Clear the buffer
                if (!(age >= 15 && age <= 30)) {
                   System.out.println("Age must be between 15 and 30.");
                } else{
                    // Validate Purok
                    while (true) {
                        System.out.print("Voter's Purok: ");
                        rpurok = sc.nextLine().trim();
                        if (!rpurok.isEmpty()) {
                            break;
                        } else {
                            System.out.println("Purok cannot be empty. Please enter a valid purok.");
                        }
                    }

                    // SQL to Insert Voter Data
                    String sql = "INSERT INTO registration (RFNAME, RLNAME, AGE, RPUROK) VALUES (?, ?, ?, ?)";
                    try (Connection conn = connectDB();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, rfname);
                        pstmt.setString(2, rlname);
                        pstmt.setInt(3, age);
                        pstmt.setString(4, rpurok);
                        pstmt.executeUpdate();
                        System.out.println("Voter added successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error adding voter: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer for age.");
                sc.next(); // Clear the invalid input
            }
        }
    }

    public void viewVoters() {
        String sql = "SELECT * FROM registration";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.printf("%-5s %-15s %-15s %-5s %-10s%n", "ID", "First Name", "Last Name", "Age", "Purok");
            System.out.println("----------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-15s %-5d %-10s%n",
                        rs.getInt("RID"),
                        rs.getString("RFNAME"),
                        rs.getString("RLNAME"),
                        rs.getInt("AGE"),
                        rs.getString("RPUROK"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving voters: " + e.getMessage());
        }
    }

    public void updateVoter() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Voter ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine(); // Clear the buffer

        String rfname;
        String rlname;
        int age;
        String rpurok;

        // Validate First Name
        while (true) {
            System.out.print("New First Name: ");
            rfname = sc.nextLine().trim();
            if (!rfname.isEmpty()) {
                break;
            } else {
                System.out.println("First name cannot be empty. Please enter a valid first name.");
            }
        }

        // Validate Last Name
        while (true) {
            System.out.print("New Last Name: ");
            rlname = sc.nextLine().trim();
            if (!rlname.isEmpty()) {
                break;
            } else {
                System.out.println("Last name cannot be empty. Please enter a valid last name.");
            }
        }

        // Validate Age
        
            System.out.print("New Age: ");
            if (sc.hasNextInt()) {
                age = sc.nextInt();
                sc.nextLine(); // Clear the buffer
                if (!(age >= 15 && age <= 30)) {
                    System.out.println("Age must be between 15 and 30. Please try again.");
                } else {
                    // Validate Purok
                    while (true) {
                        System.out.print("New Purok: ");
                        rpurok = sc.nextLine().trim();
                        if (!rpurok.isEmpty()) {
                            break;
                        } else {
                            System.out.println("Purok cannot be empty. Please enter a valid purok.");
                        }
                    }

                    // SQL to Update Voter Data
                    String sql = "UPDATE registration SET RFNAME = ?, RLNAME = ?, AGE = ?, RPUROK = ? WHERE RID = ?";
                    try (Connection conn = connectDB();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, rfname);
                        pstmt.setString(2, rlname);
                        pstmt.setInt(3, age);
                        pstmt.setString(4, rpurok);
                        pstmt.setInt(5, id);
                        pstmt.executeUpdate();
                        System.out.println("Voter updated successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error updating voter: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer for age.");
                sc.next(); // Clear the invalid input
            }

        
    }

    public void deleteVoter() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Voter ID to Delete: ");
        int id = sc.nextInt();

        // SQL to Delete Voter
        String sql = "DELETE FROM registration WHERE RID = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Voter deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting voter: " + e.getMessage());
        }
    }



    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String another = null;
        
        
        Registration registration = new Registration();
        Eligibility eligibility = new Eligibility();
        Programs program = new Programs();
        
        do {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Voter Registration");
            System.out.println("2. Eligibility");
            System.out.println("3. Program");
            System.out.println("4. Exit");
            System.out.print("Enter your action: ");
            int action = in.nextInt();

            switch (action) {
                case 1:
                    voterRegistrationMenu(in, registration);
                    break;
                case 2:
                    eligibilityMenu(in, eligibility);
                    break;
                case 3:
                    programMenu(in, program);
                    break;
                case 4:
                    System.out.println("Exiting the system...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }

            System.out.println("Continue (yes|no)?");
            another = in.next();

        } while (another.equalsIgnoreCase("yes"));

        System.out.println("Thank you for using the system!");
    }

    
    private static void voterRegistrationMenu(Scanner in, Registration registration) {
        config conf = new config();
        
        int action;
        do {
            System.out.println("\n===== Voter Registration Menu =====");
            System.out.println("1. Add Voter");
            System.out.println("2. View Voters");
            System.out.println("3. Update Voter");
            System.out.println("4. Delete Voter");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your action: ");
            action = conf.integerValidate();

            switch (action) {
                case 1:
                    registration.addVoter();
                    break;
                case 2:
                    registration.viewVoters();
                    break;
                case 3:
                    registration.updateVoter();
                    break;
                case 4:
                    registration.deleteVoter();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (action != 5);
    }

   
    private static void eligibilityMenu(Scanner in, Eligibility eligibility) {
        int action;
        do {
            System.out.println("\n===== Eligibility Menu =====");
            System.out.println("1. Add Eligibility");
            System.out.println("2. View Eligibility");
            System.out.println("3. Update Eligibility");
            System.out.println("4. Delete Eligibility");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your action: ");
            action = in.nextInt();

            switch (action) {
                case 1:
                    eligibility.addEligibility();
                    break;
                case 2:
                    eligibility.viewEligibility();
                    break;
                case 3:
                    eligibility.updateEligibility();
                    break;
                case 4:
                    eligibility.deleteEligibility();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (action != 5);
    }

    
    private static void programMenu(Scanner in, Programs program) {
        int action;
        do {
            System.out.println("\n===== Program Menu =====");
            System.out.println("1. Enroll in Program");
            System.out.println("2. View Programs");
           System.out.println("3. Back to Main Menu");
             System.out.print("Enter your action: ");
            action = in.nextInt();

            switch (action) {
                case 1:
                  program.enrollInProgram();
                    break;
                case 2:
                    program.viewPrograms();
                    break;
                case 3:
                   System.out.println("Returning to Main Menu...");
                    break;
                case 4:
                    
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (action != 3);
    }
}
