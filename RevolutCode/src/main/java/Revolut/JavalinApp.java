package Revolut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.javalin.Javalin;

public class JavalinApp {
    public static void main(String[] args) {
    	 
    	 Javalin app = Javalin.create().start(8080);
         app.get("/", ctx -> ctx.result("Hello World"));
         
         try {
             Class.forName("org.hsqldb.jdbc.JDBCDriver" );
         } catch (Exception e) {
             System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
             e.printStackTrace();
             return;
         }
         
         try {
        	Connection databaseConnection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "", "");
			Statement stmt = databaseConnection.createStatement();
            ResultSet createTable =  stmt.executeQuery("CREATE TABLE Account (UserID int, UserName varchar(50), AccountBalance int)");
            System.out.println("Table Created");
            databaseConnection.commit();
            Statement stmt2 = databaseConnection.createStatement();
            ResultSet populateDataOne = stmt2.executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (1, John, 100)");
            System.out.println("Data one inserted");
            Statement stmt3 = databaseConnection.createStatement();
            ResultSet populateDataTwo = stmt3.executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (2,Jane,500)");
            System.out.println("Data two inserted");
            Statement stmt4 = databaseConnection.createStatement();
            ResultSet loopData = stmt4.executeQuery("SELECT * FROM Account");
            // Loop through the data and print all data
            // Loop through the data and print all data
            while(loopData.next()) {
                System.out.println("User ID" + loopData.getString("UserID") + " User Name" + loopData.getString("UserName") + " Account Balance" + loopData.getString("AccountBalance"));
            }
             
            // Clean up
            createTable.close();
            populateDataOne.close();
            populateDataTwo.close();
            loopData.close();
            stmt.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
			e.printStackTrace();
        }
    }
}