package votersp;
import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
          Scanner in = new Scanner(System.in);
        Programs programs = new Programs();
       
        Registration registration = new Registration();
            String another;
        do {
            // Main Menu
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Voter Registration");
            System.out.println("2. Programs");
            System.out.println("3. Exit");
            System.out.print("Enter your action: ");
            int action = in.nextInt();

            switch (action) {
                case 1:
                    voterRegistrationMenu(in, registration);
                    break;
                case 2:
                    programMenu(in, programs);
                    break;
                case 3:
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
            System.out.println("1. Register Voter");
            System.out.println("2. View Voter Details");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your action: ");
            action = in.nextInt();

            switch (action) {
                case 1:
                    registration.registerVoter(in);
                    break;
                case 2:
                    System.out.print("Enter Voter ID to view details: ");
                    int voterId = in.nextInt();
                    registration.viewVoterDetails(voterId);
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
        System.out.println("\n===== Programs Menu =====");
        System.out.println("1. Enroll in Program");
        System.out.println("2. View Registered Program");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an action: ");
        action = in.nextInt();

        switch (action) {
            case 1:
                // Prompt user for their Voter ID before enrolling
                System.out.print("Enter your Voter ID to enroll: ");
                int voterId = in.nextInt();
                // Call the validateAndEnrolls method with voterId and in (Scanner)
                programs.validateAndEnrolls(voterId, in);
                break;

            case 2:
                // Prompt user for their Voter ID to view their program details
                System.out.print("Enter Voter ID to view program details: ");
                int programVoterId = in.nextInt();
                programs.viewProgramDetails(programVoterId);
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
