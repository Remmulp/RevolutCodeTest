import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.Javalin;

public class JavalinApp {
	static Map<String, String> reservations = new HashMap<String, String>() {{
        put("saturday", "No reservation");
        put("sunday", "No reservation");
    }};
    
    public static void main(String[] args) {
    	//Driver connection
    	try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
    	
    	//Connecting to in memory DB, does not exist create.
       	Connection databaseConnection = null;
		try {
			databaseConnection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "", "");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		//Test data set up
        try {
           Statement stmt = databaseConnection.createStatement();
           ResultSet createTable =  stmt.executeQuery("CREATE TABLE Account (UserID int, UserName varchar(50), AccountBalance int)");
           System.out.println("Table Created");
           databaseConnection.commit();
           
           Statement stmt2 = databaseConnection.createStatement();
           ResultSet populateDataOne = stmt2.executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (1, 'John', 100)");
           System.out.println("Data one inserted");
           
           Statement stmt3 = databaseConnection.createStatement();
           ResultSet populateDataTwo = stmt3.executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (2, 'Jane',500)");
           System.out.println("Data two inserted");
           
           Statement stmt4 = databaseConnection.createStatement();
           ResultSet loopData = stmt4.executeQuery("SELECT * FROM Account");
           
           // Loop through the data and print all data to show insertions
           while(loopData.next()) {
               System.out.println("User ID: " + loopData.getString("UserID") + " User Name: " + loopData.getString("UserName") + " Account Balance: " + loopData.getString("AccountBalance"));
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
        
        //Get the Balance of each account
		try {
			Statement getBalance = databaseConnection.createStatement();
			ResultSet getBalanceResult = getBalance.executeQuery("SELECT AccountBalance FROM Account");
			
			List rowValues = new ArrayList();
			while (getBalanceResult.next()) {
			    rowValues.add(getBalanceResult.getString(1));
			}   
			for(int i = 0; i < rowValues.size(); i++ )
			{
				System.out.println("Balance Amuonts are: " + rowValues.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	 Javalin app = Javalin.create(config -> {config.addStaticFiles("/public");}).start(8080);
    	 
    	 Javalin amountToTransfer = app.get("/make-transfer", ctx -> {ctx.html(reservations.get(ctx.queryParam("Amount")));});
    	 
         app.post("/make-transfer", ctx -> {
             reservations.put(ctx.formParam("Amount"), ctx.formParam("Account"));
             ctx.html("Your transfer has been complete");
         });
         
         app.get("/check-balance", ctx -> {
             ctx.html(reservations.get(ctx.queryParam("Amount")));
         });
         
         /*app.post("/upload-example", ctx -> {
        	    ctx.uploadedFiles("files").forEach(file -> {
        	        FileUtil.streamToFile(file.getContent(), "upload/" + file.getFilename());
        	    });
        	});*/
    }
}