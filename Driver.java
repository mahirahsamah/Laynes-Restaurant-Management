/*

CSCE 315-900 Team 2
Project 2: Database Interface

Compile and run with the following command (only works in windows cmd):
make

Access the psql server directly:
psql -h csce-315-db.engr.tamu.edu -U csce315900_2user csce315900_2db


Josh's directory path:
cd C:\Users\jwood\AppData\Local\Packages\CanonicalGroupLimited.UbuntuonWindows_79rhkp1fndgsc\LocalState\rootfs\home\jswood\github\CSCE_315_Lab\CSCE_315_DBScript

*/

import java.util.*; 
import java.io.*;
import java.lang.Thread.State;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.stream.*;

import javax.naming.spi.DirStateFactory.Result;

public class Driver extends JFrame implements ActionListener{

    public static Connection conn = null;

    public static int findCharLimit (ResultSet result) {

        // this helper function takes a result set that will be printed in a table and finds the char limit to use to make the table look pretty
        int charMax = 1;

        try {
            ResultSetMetaData rsmd = result.getMetaData();
            int numColumns = rsmd.getColumnCount();

            while (result.next()) {
                for (int i = 1; i <= numColumns; i++) {

                    String thisWord = result.getString(i);

                    if (result.wasNull()) {
                        System.out.println("Encountered a null value.");
                        continue;
                    }

                    int thisCharNum = thisWord.length();

                    if (thisCharNum > charMax) {
                        charMax = thisCharNum;
                    }
                }
            }

            result.beforeFirst();

        } catch (Exception e) {
            System.out.println("Error finding char limit:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return charMax + 2;
    }

    public static ArrayList<String> toStringAL (String line, String delimiter) {

        // this helper function takes a string separated by a delimiter (such as a line from a csv file with commas) and turns it into an arraylist of strings
        ArrayList<String> stringAL = new ArrayList<String>();
        Boolean isInText = false;
        String thisWord = "";

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\"') {
                if (isInText) {
                    isInText = false;
                } else {
                    isInText = true;
                }
            }

            if (line.charAt(i) == ',' && !isInText && i < line.length() - 1) {
                stringAL.add(thisWord);
                thisWord = "";
                continue;
            }

            if (line.charAt(i) != '\"') {
                if (line.charAt(i) == '\'') {
                    thisWord = thisWord + '\'';
                }

                thisWord = thisWord + line.charAt(i);
            }

            if (i == line.length() - 1) {
                if (thisWord.charAt(thisWord.length() - 1) == ',') {
                    thisWord = thisWord.substring(0, thisWord.length() - 1);
                }
                stringAL.add(thisWord);
            }
        }

        return stringAL;
    }

    public static void login (String userPassword) {
        //login information for database
        String teamNumber = "2";
        String sectionNumber = "900";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";


        try {
            //create a connection to database
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
    }

    public static void logout () {
        //Close connection with database
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
            e.printStackTrace();
        }//end try catch
    }

    public static void printTableResults (ResultSet result, int charLimit) {
        int maxLimit = 25, minLimit = 5; // the maximum and minimum width of each table entry
        if (charLimit > maxLimit) {
            charLimit = maxLimit;
        } else if (charLimit < minLimit) {
            charLimit = minLimit;
        }

        try {
            ResultSetMetaData rsmd = result.getMetaData();
            int numColumns = rsmd.getColumnCount();

            System.out.println("--------------------Query Results--------------------");
            
            // print column titles
            for (int i = 1; i <= numColumns; i++) {
                String word = rsmd.getColumnName(i);
                if (word.length() > charLimit) {
                    word = word.substring(0, charLimit - 2) + "..";
                }
                System.out.printf("%" + (charLimit + 1) + "s", word);
            }
            System.out.println();

            // print results
            while (result.next()) {
                for (int i = 1; i <= numColumns; i++) {

                    String word = result.getString(i);

                    if (result.wasNull()) { //no entry
                        word = "-null-";
                    }

                    if (word.length() > charLimit) {
                        word = word.substring(0, charLimit - 2) + "..";
                    }

                    System.out.printf("%" + (charLimit + 1) + "s", word);
                }

                System.out.println();
            }
        } catch (Exception e){
            System.out.println("Error printing result:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    
    public static void printTable (String tableName) {

        //Sending SQL query to select all information to print table
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM " + tableName + ";";
            
            ResultSet result = stmt.executeQuery(query);
            printTableResults(result, findCharLimit(result));
        } catch (Exception e) {
            System.out.println("Error querying table:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public static ArrayList<String> readCSV (String fileName) {
        
        //Reading csv as input for database
        ArrayList<String> fileLines = new ArrayList<String>();
        try (Scanner scanner = new Scanner(new File(fileName));) {
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + fileName + ":");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Successfully read file " + fileName);
        return fileLines;
    }

    public static void updateMenuFromCSV (String fileName) {
        try {
            Statement stmt = conn.createStatement(); // initialize statement to use for updating
            String delimiter = ",";
            String tableName = "menu_key"; // the name of the table we are modifying
            ArrayList<String> csvData = readCSV(fileName); // the data we are using to modify the table
            String[] columnTypes = {"int", "text", "text", "text"}; // the data types for each column
            ArrayList<String> columns = toStringAL(csvData.get(1), delimiter); // the name of each column
            ArrayList<Integer> columnsNotToUse = new ArrayList<Integer>(Arrays.asList(0, 5, 6)); // columns to skip

            for (int i = 2; i < csvData.size(); i++) {
                ArrayList<String> thisRow = toStringAL(csvData.get(i), delimiter); // current line we're looking at
                System.out.println("thisRow has " + thisRow.size() + " entries");
                String query = "INSERT INTO " + tableName + " VALUES (";
                int counter = 0;
                for (int j = 1; j < thisRow.size(); j++) {
                    if (columnsNotToUse.contains(j)) { continue; }
                    String nextVal = thisRow.get(j);
                    if (columnTypes[counter].equals("text")) {
                        nextVal = "\'" + nextVal + "\'"; // if it's a string, put it in single quotations
                    }
                    System.out.println(nextVal);
                    query += nextVal;
                    if (counter < columns.size() - columnsNotToUse.size()) {
                        query += ", "; // add a comma afterwards unless it's the last item
                    }
                    counter ++;
                }
                query += ");";
                System.out.println(query);
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            System.out.println("Error updating menu:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public static void updateInventoryFromCSV (String fileName) {

        //Update inventory data table from csv input
        try {
            String delimiter = ",";
            ArrayList<String> csvData = readCSV(fileName);

            Boolean initialOrder = false;
            String isInitial = toStringAL(csvData.get(0), ",").get(1).trim();

            if (isInitial.equals("Initial order")) {
                initialOrder = true;
                System.out.println("Initial Order detected.");
            }

            // make column
            String[] columnTypes = {"text", "text", "text", "text", "text", "text", "text", "text", "int", "text", "int"};
            int[] columnsNotToUse = {4, 9}; // the integer values of the columns we are skipping
            //String[] datalabels = csvData.get(1).split(delimiter); // all of the column titles
            String[] datalabels = Stream.concat(Arrays.stream(csvData.get(1).split(delimiter)), Arrays.stream(new String[] {"fill_level"})).toArray(String[]::new);
            ArrayList<String> columns = new ArrayList<String>();

            for (int i = 0; i < datalabels.length; i++) {
                Boolean skipThis = false; // if this is true, skip this data label
                for (int j = 0; j < columnsNotToUse.length; j++) {
                    if (i == columnsNotToUse[j]) {
                        // skip this data label
                        skipThis = true;
                        break;
                    }
                }

                if (!skipThis) {
                    columns.add(datalabels[i]); // add to the columns we are using
                }
            }
            
            Statement stmt = conn.createStatement(); // initialize statement to use for querying/updating

            // loop through all of the remaining data in the file
            String tableName = ""; // the name of the current table we're working on
            int elementStoppedOn = -1; // if the loop stops prematurely, this is the line it stopped on

            for (int i = 2; i < csvData.size(); i++) {
                //System.out.println(csvData.get(i));
                ArrayList<String> thisRow = toStringAL(csvData.get(i), delimiter); // current line we're looking at

                if (thisRow.size() == 0) {
                    // if this row is empty then stop
                    elementStoppedOn = i;
                    System.out.println("Loop stopped on line " + elementStoppedOn);
                    break;
                }

                if (thisRow.get(0) != "" && thisRow.get(0).length() > 1) {
                    tableName = thisRow.get(0).toLowerCase(); // change the current table we're working on
                    System.out.println("Now looking at table " + tableName); // let the user know the current table has changed
                    
                    ArrayList<String> tableList = printAll();
                    if (!tableList.contains(tableName)) {
                        // throw exception for nonexistant table
                        throw new java.lang.NullPointerException("The requested table does not exist.");
                    }
                } else {
                    if (thisRow.get(1) == "" || thisRow.get(1).length() <= 1) {
                        // if this row is empty then stop
                        elementStoppedOn = i;
                        System.out.println("Loop stopped on line " + elementStoppedOn);
                        break;
                    }

                    if (initialOrder) {
                        String query = "INSERT INTO " + tableName + " VALUES (";
                        int counter = 0;

                        for (int j = 1; j < thisRow.size(); j++) {

                            Boolean skipThis = false; // check if this is one of the columns we're not using
                            for (int k = 0; k < columnsNotToUse.length; k++) {
                                if (j == columnsNotToUse[k]) {
                                    skipThis = true;
                                    break;
                                }
                            }

                            if (!skipThis && datalabels[counter].equals("fill_level")) {

                                String nextVal = "";

                                if (columnTypes[counter].equals("text")) {
                                    nextVal = "\'" + thisRow.get(j) + "\'";
                                } else if (columnTypes[counter].equals("int")) {
                                    nextVal = thisRow.get(j);
                                }

                                query += nextVal;

                                if (j < thisRow.size() - 1) {
                                    query += ", ";
                                }
                                counter ++;
                            }
                        }
                        query += (int) (Integer.parseInt(thisRow.get(2)) * 0.75) + ");";
                        System.out.println(query);
                        stmt.executeUpdate(query);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error updating table:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public static void deleteTable (String tableName) {
        //Delete table from database
        try {
            String query = "DROP TABLE " + tableName + ";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("The table "  + tableName + " has been deleted FOREVER.");
        } catch (Exception e) {
            System.out.println("Error deleting table:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public static ArrayList<String> printAll () {
        //Print a list of all current tables in database
        ArrayList<String> tableList = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                tableList.add(tables.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            System.out.println("Error listing tables:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return tableList;
    }

    public static ArrayList<String> readQuery(String fileName) {
        //reading from a text file of sql queries
        ArrayList<String> fileLines = new ArrayList<String>();
        try (Scanner scanner = new Scanner(new File(fileName));) {
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error reading query file " + fileName + ":");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Successfully read file " + fileName);
        return fileLines;
    }

    public static void addConversionData(String fileName) {
        try {
            Statement stmt = conn.createStatement(); // initialize statement to use for updating
            String delimiter = ",";
            ArrayList<String> csvData = readCSV(fileName); // the data we are using to modify the table
            ArrayList<String> columns = toStringAL(csvData.get(1), delimiter); // the name of each column
            String tableName = toStringAL(csvData.get(0), delimiter).get(0); // the name of the table we are modifying

            // create new table
            String tableQuery = "CREATE TABLE " + tableName + " (";
            for (int i = 0; i < columns.size(); i++) {
                tableQuery += columns.get(i);
                if (i < columns.size() - 1) {
                    tableQuery += ", ";
                }
            }
            tableQuery += ");";
            stmt.executeUpdate(tableQuery);

            for (int i = 2; i < csvData.size(); i++) {
                ArrayList<String> thisRow = toStringAL(csvData.get(i), delimiter); // current line we're looking at
                System.out.println("thisRow has " + thisRow.size() + " entries");
                String query = "INSERT INTO " + tableName + " VALUES (";
                for (int j = 0; j < thisRow.size(); j++) {
                    String nextVal = thisRow.get(j);
                    if (j == 1) {
                        nextVal = "\'" + nextVal + "\'"; // if it's a string, put it in single quotations
                    }
                    query += nextVal;
                    if (j < columns.size() - 1) {
                        query += ", "; // add a comma afterwards unless it's the last item
                    }
                }
                query += ");";
                System.out.println(query);
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            System.out.println("Error adding conversion data:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    
    public static void addSalesEntries(String fileName) {
        try {
            Statement stmt = conn.createStatement(); // initialize statement to use for updating
            String delimiter = ",";
            String tableName = "weekly_sales"; // the name of the table we are modifying
            ArrayList<String> csvData = readCSV(fileName); // the data we are using to modify the table
            String[] columnTypes = {"int", "int", "text"}; // the data types for each column
            ArrayList<String> columns = toStringAL(csvData.get(1), delimiter); // the name of each column
            ArrayList<Integer> columnsNotToUse = new ArrayList<Integer>(Arrays.asList(0, 4)); // columns to skip

            String weekOf = toStringAL(csvData.get(0), delimiter).get(2);
            String currentDay = "";

            for (int i = 1; i < csvData.size(); i++) {
                ArrayList<String> thisRow = toStringAL(csvData.get(i), delimiter); // current line we're looking at

                if (thisRow.get(0).length() <= 1 && thisRow.get(1).length() <= 1) {
                    continue; // empty row
                }

                if (thisRow.get(0).length() > 1) {
                    // this is a lable row and not a data row
                    currentDay = thisRow.get(0); // change the current day
                    continue;
                }

                String query = "INSERT INTO " + tableName + " VALUES (";

                String sales_id = "\'" + weekOf + " " + currentDay + " " + thisRow.get(1) + "\'";
                query += sales_id + ", \'" + weekOf + "\', \'" + currentDay + "\', ";

                int counter = 0;
                for (int j = 1; j < thisRow.size(); j++) {
                    if (columnsNotToUse.contains(j)) { continue; }
                    String nextVal = thisRow.get(j);
                    if (columnTypes[counter].equals("text")) {
                        nextVal = "\'" + nextVal + "\'"; // if it's a string, put it in single quotations
                    }
                    query += nextVal;
                    if (counter < columns.size() - columnsNotToUse.size() - 1) {
                        query += ", "; // add a comma afterwards unless it's the last item
                    }
                    counter ++;
                }
                query += ");";
                System.out.println(query);
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            System.out.println("Error adding sales data:");
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public static void runQuery(String str) {
        //send query to be executed
    	try {
    		Statement stmt = conn.createStatement();
	    	ResultSet queryResult = stmt.executeQuery(str);
	        
	        printTableResults (queryResult, 100);
    	}
    	catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void runInterface() throws FileNotFoundException {
        //Interface created to help with seeing current information in database
        while (true) {
            Scanner ioScanner = new Scanner(System.in);
            String prompt = "What would you like to do? Options:\n";
            prompt += "0: quit\n1: list all tables\n2: print table\n3: update inventory\n"; // options 1-3
            prompt += "4: update menu\n5: delete table\n6: make a query\n"; // options 4-6
            prompt += "7: add sales entries\n8: manually enter query\n9: update unit conversion data"; // options 7-9
            System.out.println(prompt);
            System.out.print("--$");
            int option = ioScanner.nextInt();
            ioScanner.nextLine();

            switch (option) {
                case 0:
                    System.out.println("Quitting...");
                    System.exit(0);
                    break;
                case 1:
                    System.out.println("Tables currently in the database:");
                    ArrayList<String> tableList = printAll();
                    
                    for (int i = 0; i < tableList.size(); i++) {
                        System.out.println(tableList.get(i));
                    }
                    break;
                case 2:
                    System.out.print("Which table do you want to look at? ");
                    String tableName = ioScanner.nextLine();
                    System.out.println();
                    printTable(tableName);
                    break;
                case 3:
                    System.out.print("Please enter csv file to use: ");
                    String csvfile = "UserInput/" + ioScanner.nextLine();
                    System.out.println();
                    updateInventoryFromCSV(csvfile);
                    break;
                case 4:
                    System.out.print("Please enter csv file to use: ");
                    String csvfile2 = "UserInput/" + ioScanner.nextLine();
                    System.out.println();
                    updateMenuFromCSV(csvfile2);
                    break;
                case 5:
                    System.out.println("Which table do you want to delete? (Warning: this cannot be undone)");
                    String tableDeleted = ioScanner.nextLine();
                    deleteTable(tableDeleted);
                    break;
                case 6:
                    //new variable for the queries file
                    String queryFile = "";
                    //prompts user for query file name
                    queryFile = "UserInput/queries.text";
                    //System.out.println("Please enter the name of the file containing the queries you would like to run.");
                    //queryFile = ioScanner.nextLine();
        
                    //creates a scanner for the new file from the query file's name
                    Scanner file = new Scanner(new File(queryFile));
                    //linked list of strings to hold the queries
                    LinkedList<String> queries = new LinkedList<String>();

                    //adds the queries to the linked list
                    while(file.hasNextLine()) {
                        queries.add(file.nextLine());
                    }

                    //displays all the queries from the file
                    System.out.println("Queries in file: ");
                    for(int i = 0; i < queries.size(); i++) {
                        System.out.println((i + 1) + ": " + queries.get(i)); //individual queries
                    }
                    System.out.println();

                    //asks user which query they want to run
                    System.out.println("Which query do you want to run? (Enter the number)");
                    int queryNum = ioScanner.nextInt(); //reads in the number of the query
                    System.out.println();
                    String query = queries.get(queryNum - 1); //reads in the query
                    System.out.println("Running query: " + query); //prints out the query ran
                    runQuery(query); //runs the query
                    break;
                case 7:
                    System.out.print("Please enter csv file to use: ");
                    String salesCSV = "UserInput/" + ioScanner.nextLine();
                    System.out.println();
                    addSalesEntries(salesCSV);
                    break;
                case 8:
                    System.out.println("Enter the query below: ");
                    String manualQuery = ioScanner.nextLine();
                    System.out.println();
                    runQuery(manualQuery);
                    break;
                case 9:
                    System.out.print("Please enter csv file to use: ");
                    String conversionCSV = "UserInput/" + ioScanner.nextLine();
                    System.out.println();
                    addConversionData(conversionCSV);
                    break;
                
                default:
                    System.out.println("Invalid input: Command not recognized.");
                    break;
                }

            //ioScanner.close();
            System.out.println();
        }
    }
    static JFrame f;
    public static void main (String args[]) throws FileNotFoundException {
        Boolean useGUI = true;

        if (useGUI) {
            LaynesGUI lgui = new LaynesGUI();
            lgui.StartGUI();
        } else {
            // ask for password and setup connection
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter password: ");
            String userPassword = sc.nextLine();
            login(userPassword);
    
            // run interface loop
            runInterface();
    
            // log out and end program
            logout();
        }
        
        //lgui.guiLogout();
    }
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}

