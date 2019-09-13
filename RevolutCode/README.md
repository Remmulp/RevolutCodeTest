# Revolut Code Test
Coding test for Revolut API bank transfer for backend developer.

# To Run
1. Run the JavalinApp.java file which will:
- Create the in memory HSQLDB database, table and populate with data.
- Start the Javalin API on localhost 8080
- Handle post and get requests.

2. Navigate to 'https://localhost:8080'.

3. Use the application for bank transfers and management.

(All can be seen on screen but for more details see the console output.)

# Functionality
1. To make a transfer select the amount and the Account to transfer to. This amount shall then be deducted from the source account and added to the requested destination account.
2. To check the balance of your accounts click the 'Check Balance' button. This shall show you the current balance of your managed accounts.
3. To end the session and terminate the Javalin service click the 'Exit' button.

# Technologies used
## Maven
To handle dependancies and create a self contained project.

## Java
Main lanaguage used.

## Javalin
Lightweight framework used for connection and API functionality.

## HSQLDB
An in memory SQL database system for handling, storing and querying test data.

## HTML 
Used in the creation of the webpage in order to interact with the overall system.
