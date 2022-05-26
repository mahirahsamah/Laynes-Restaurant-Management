# CSCE_315_DBScript

To run in linux, enter these commands:\
javac *.java\
java -cp ".:postgresql-42.2.8.jar" Driver

To run in windows command prompt, enter these commands:\
javac *.java\
java -cp ".;postgresql-42.2.8.jar" Driver


Driver.java allows login for a specific class section (900) and team (team 2) in order to access the correct databases. Also in this file are functions printTableResults() and printTable() which prints a specific data table, readCSV() and createTableFromCSV() to read in data from CSV files and add that data to a new data table, with function deleteTable() to delete specific tables as well. printAll() prints out all the stored data tables in the database. The function runInterface() prompts the user with a menu to select from the different options of listing all tables, printing a specific table, create a new table, delete a certain table, and quit out of the interface.