import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import io.javalin.Javalin;

public class JavalinApp {
    static Connection databaseConnection = null;

    static ArrayList<String> BankAccount = new ArrayList<String>() {
    };

    public static void doTransfer() throws SQLException {
        int Amount = Integer.parseInt(BankAccount.get(0));
        String Account = BankAccount.get(1);

        // If account to transfer is John (else statment is vice versa)
        System.out.println(Account.equals("John"));
        if (Account.equals("John")) {
            // Add the balance to Jane
            PreparedStatement subtractBalanceFromJane = databaseConnection
                    .prepareStatement("UPDATE Account SET AccountBalance = AccountBalance + ? WHERE UserID = 1");
            subtractBalanceFromJane.setObject(1, Amount);

            // Subtract the balance from John
            PreparedStatement addBalanceToJohn = databaseConnection
                    .prepareStatement("UPDATE Account SET AccountBalance = AccountBalance - ? WHERE UserID = 2");
            addBalanceToJohn.setObject(1, Amount);

            subtractBalanceFromJane.executeUpdate();
            addBalanceToJohn.executeUpdate();
        } else {
            PreparedStatement subtractBalanceFromJohn = databaseConnection
                    .prepareStatement("UPDATE Account SET AccountBalance = AccountBalance - ? WHERE UserID = 1");
            subtractBalanceFromJohn.setObject(1, Amount);

            PreparedStatement addBalanceToJane = databaseConnection
                    .prepareStatement("UPDATE Account SET AccountBalance = AccountBalance + ? WHERE UserID = 2");
            addBalanceToJane.setObject(1, Amount);

            subtractBalanceFromJohn.executeUpdate();
            addBalanceToJane.executeUpdate();
        }

        Statement checkTransfer = databaseConnection.createStatement();
        ResultSet loopData = checkTransfer.executeQuery("SELECT * FROM Account");

        // Loop through the data and print all data to show insertions to show transfer
        // done correctly in console.
        while (loopData.next()) {
            System.out.println("User ID: " + loopData.getString("UserID") + " User Name: "
                    + loopData.getString("UserName") + " Account Balance: " + loopData.getString("AccountBalance"));
        }
    }

    public static void main(String[] args) {
        // Driver connection
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        // Connecting to in memory DB, does not exist create.
        try {
            databaseConnection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "", "");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        // Test data set up
        try {
            Statement stmt = databaseConnection.createStatement();
            ResultSet createTable = stmt
                    .executeQuery("CREATE TABLE Account (UserID int, UserName varchar(50), AccountBalance int)");
            System.out.println("Table Created");
            databaseConnection.commit();

            Statement stmt2 = databaseConnection.createStatement();
            ResultSet populateDataOne = stmt2
                    .executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (1, 'John', 100)");
            System.out.println("Data one inserted");

            Statement stmt3 = databaseConnection.createStatement();
            ResultSet populateDataTwo = stmt3
                    .executeQuery("INSERT INTO Account (UserID, UserName, AccountBalance) VALUES (2, 'Jane',500)");
            System.out.println("Data two inserted");

            Statement stmt4 = databaseConnection.createStatement();
            ResultSet loopData = stmt4.executeQuery("SELECT * FROM Account");

            // Loop through the data and print all data to show insertions
            while (loopData.next()) {
                System.out.println("User ID: " + loopData.getString("UserID") + " User Name: "
                        + loopData.getString("UserName") + " Account Balance: " + loopData.getString("AccountBalance"));
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
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(8080);

        // Javalin amountToTransfer = app.get("/make-transfer", ctx -> {
        // ctx.html(BankAccount.get(ctx.queryParam("Amount")));
        // });

        // Add amount to transfer and account to transfer to, to the ArrayList
        app.post("/make-transfer", ctx -> {
            BankAccount.add(0, ctx.formParam("Amount"));
            BankAccount.add(1, ctx.formParam("Account"));
            ctx.html("Your transfer has been complete");
            doTransfer();
        });

        //On Exit, stop the Javalin session
        app.post("/exit", ctx -> {
            ctx.html("You have exited the session");
            app.stop();
        });
        
        // app.get("/check-balance", ctx -> {
        // ctx.html(BankAccount.get(ctx.queryParam("Balance")));
        // });

        /*
         * app.post("/upload-example", ctx -> { ctx.uploadedFiles("files").forEach(file
         * -> { FileUtil.streamToFile(file.getContent(), "upload/" +
         * file.getFilename()); }); });
         */
    }
}