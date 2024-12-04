package votersp;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Registration rs = new Registration();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Programs programs = new Programs();
        String another;

        do {
            try {
                // Main Menu
                System.out.println("\n===== Main Menu =====");
                System.out.println("1. Voter Registration");
                System.out.println("2. Programs");
                System.out.println("3. Exit");
                System.out.print("Enter your action: ");
                
                int action = getValidIntInput(in);  // Validated input method

                switch (action) {
                    case 1:
                        voterRegistrationMenu(in, rs);
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

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                in.nextLine(); // Clear the invalid input
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
            try {
                System.out.println("\n===== Voter Registration Menu =====");
                System.out.println("1. Register Voter");
                System.out.println("2. View Voter Details");
                System.out.println("3. Back to Main Menu");
                System.out.print("Enter your action: ");
                
                action = getValidIntInput(in);  // Validated input method

                switch (action) {
                    case 1:
                        registration.registerVoter(in);
                        break;
                    case 2:
                        rs.viewAllVoters();
                        System.out.print("Enter Voter ID to view details: ");
                        int voterId = getValidIntInput(in);  // Validated input method
                        registration.viewVoterDetails(voterId);
                        break;
                    case 3:
                        System.out.println("Returning to Main Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                in.nextLine(); // Clear the invalid input
                action = -1;  // Reset action to continue the loop
            }
        } while (action != 3);
    }

    // Programs Menu
    private static void programMenu(Scanner in, Programs programs) {
        int action;
        do {
            try {
                System.out.println("\n===== Programs Menu =====");
                System.out.println("1. Enroll in Program");
                System.out.println("2. View Registered Program");
                System.out.println("3. Back to Main Menu");
                System.out.print("Choose an action: ");
                
                action = getValidIntInput(in);  // Validated input method

                switch (action) {
                    case 1:
                        rs.viewAllVoters();
                        System.out.print("Enter your Voter ID to enroll: ");
                        int voterId = getValidIntInput(in);  // Validated input method
                        programs.validateAndEnrolls(voterId, in);
                        break;
                    case 2:
                        programs.viewProgramOptions(in);
                        break;
                    case 3:
                        System.out.println("Returning to Main Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                in.nextLine(); // Clear the invalid input
                action = -1;  // Reset action to continue the loop
            }
        } while (action != 3);
    }

    // Helper method for validated integer input
    private static int getValidIntInput(Scanner in) {
        while (true) {
            try {
                return in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                in.nextLine(); // Clear the invalid input
                System.out.print("Enter again: ");
            }
        }
    }
}
