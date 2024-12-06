/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package votersp;


import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class config {
    Scanner sc = new Scanner(System.in);
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:votersp.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
       
    
    
    public void AddRecords(String sql, Object... values) {
    Connection conn = this.connectDB(); // Use the connectDB method
    if (conn == null) {
        System.out.println("Database connection failed.");
        return; // Exit if connection is not established
    }
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}

   public void viewRecords(String sqlQuery, String[] headers, String[] columns) {
    viewRecordsWithParams(sqlQuery, headers, columns);
}

// Method to handle both parameterized and simple queries
public void viewRecordsWithParams(String sqlQuery, String[] headers, String[] columns, Object... params) {
    if (headers.length != columns.length) {
        System.out.println("Error: Mismatch between column headers and column names.");
        return;
    }

    try (Connection conn = connectDB();  // Establish connection
         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

        // Set values if any parameters exist
        setPreparedStatementValues(pstmt, params);

        try (ResultSet rs = pstmt.executeQuery()) {
            // Print headers
            printTableHeaders(headers);

            // Print each row
            while (rs.next()) {
                printTableRow(rs, columns);
            }
            System.out.println("---------------------------------------------------------------------------------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}

// Helper method to print table headers
private void printTableHeaders(String[] headers) {
    StringBuilder headerLine = new StringBuilder("\n---------------------------------------------------------------------------------------------------\n| ");
    for (String header : headers) {
        headerLine.append(String.format("%-20s | ", header));
    }
    headerLine.append("\n---------------------------------------------------------------------------------------------------");
    System.out.println(headerLine);
}

// Helper method to print rows dynamically
private void printTableRow(ResultSet rs, String[] columns) throws SQLException {
    StringBuilder row = new StringBuilder("| ");
    for (String column : columns) {
        String value = rs.getString(column);
        row.append(String.format("%-20s | ", value != null ? value : "N/A"));
    }
    System.out.println(row);
}

// Helper method to set PreparedStatement values dynamically
private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
    for (int i = 0; i < values.length; i++) {
        Object value = values[i];
        if (value instanceof Integer) {
            pstmt.setInt(i + 1, (Integer) value);
        } else if (value instanceof Double) {
            pstmt.setDouble(i + 1, (Double) value);
        } else if (value instanceof Boolean) {
            pstmt.setBoolean(i + 1, (Boolean) value);
        } else {
            pstmt.setString(i + 1, value.toString());
        }
    }
}


      public void updateRecords(String sql, Object... values) {
        try (Connection conn = this.connectDB(); // Use the connectDB method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }
   
     
     
      public void deleteRecords(String sql, Object... values) {
        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); 
                } else {
                    pstmt.setString(i + 1, values[i].toString()); 
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
      
      public int integerValidate(){
          int getNum;
          
          while(true){
                try{
                getNum = sc.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.print("Invalid input: Input must only be numbers, try again: ");
                sc.next();
            }
        }
        return getNum;
    }
}
