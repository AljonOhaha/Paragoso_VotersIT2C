package votersp;

import java.sql.*;

public class PurokListing {

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

    // Method to validate the voter's Purok
   public void validatePuroks(int voterId) {
    String[] validPuroks = {
        "tabay dos", "tabay uno", "kalingawa", "kauswagan", 
        "tangason", "tower", "tambis uno", "tambis dos", 
        "sakaon", "skina gamay"
    };

    String sql = "SELECT RPUROK FROM registration WHERE RID = ?";
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, voterId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String purok = rs.getString("RPUROK");
            // Check if the entered purok is valid
            boolean isValidPurok = false;
            for (String validPurok : validPuroks) {
                if (validPurok.equalsIgnoreCase(purok)) {
                    isValidPurok = true;
                    break;
                }
            }
            if (isValidPurok) {
                System.out.println("Voter belongs to a valid Purok: " + purok);
            } else {
                System.out.println("Voter's Purok is invalid: " + purok);
            }
        } else {
            System.out.println("No voter found with ID: " + voterId);
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    // Method to view Puroks and voters assigned to each Purok
  public void viewPuroks() {
    String[] validPuroks = {
        "tabay dos", "tabay uno", "kalingawan", "kauswagan", 
        "tangason", "tower", "tambis uno", "tambis dos", 
        "sakaon", "skina gamay"
    };

    String sql = "SELECT RID, RFNAME, RLNAME, RPUROK FROM registration";
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        System.out.println("\n===== Registered Voters with Purok Validation =====");
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| ID   | Name                   | Purok        | Status     |");
        System.out.println("+------------------------------------------------------------+");

        while (rs.next()) {
            int voterId = rs.getInt("RID");
            String firstName = rs.getString("RFNAME");
            String lastName = rs.getString("RLNAME");
            String purok = rs.getString("RPUROK");

            // Check if the voter's Purok is valid
            boolean isValidPurok = false;
            for (String validPurok : validPuroks) {
                if (validPurok.equalsIgnoreCase(purok)) {
                    isValidPurok = true;
                    break;
                }
            }

            // Display the voter's information with Purok validation result
            String status = isValidPurok ? "Valid" : "Invalid";
            System.out.printf("| %-4d | %-21s | %-12s | %-10s |\n", voterId, firstName + " " + lastName, purok, status);
        }

        System.out.println("+------------------------------------------------------------+");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


}