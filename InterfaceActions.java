import java.util.*;
import java.io.*;
import java.lang.Thread.State;
import java.sql.*;
import java.time.LocalDate;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
//import java.awt.List;
import java.util.List;
import java.util.Map.*;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JOptionPane;

import org.jfree.data.category.DefaultCategoryDataset;

public class InterfaceActions {
    public static Connection conn = null;
    public static Hashtable<Integer, Vector<Float>> uConversion;
    public static ArrayList<String> ingredientSKUs;
    public static ArrayList<String> itemIDs;

    public static Map<String, Integer> itemsOrderedHash; // hashtable to store item ordered and the amount it was ordered
    public static Map<String, String> itemsIngredientsHash; // hashtable to store item ordered and its ingredients
    public static Map<String, Integer> ingredientsAmountHash; // hashtable to store how nuch of one ingredient is used in those orders
    public static Map<String, Integer> ingredientsTotalAmountHash; // hashtable to store how much 
    public static Map<String, String> ingredientsMultipliedHash;
    public static ArrayList<String> itemIngredientsList;
    public static Map<String, Float> kkMap;
    public static Map<String, Entry<ArrayList<String>, ArrayList<Integer>>> threeMap;
    public static Map<String, Float> newMap;

    /////////////////////////////////// ////////////////// Helper Functions ////////////////////////// ////////////////////////////////////

    public static Boolean checkDate(String date) {
        if (date.length() != 10) {
            return false;
        }

        if (date.charAt(4) != '-' || date.charAt(7) != '-') {
            return false;
        }

        if (!(Character.isDigit(date.charAt(0)) && Character.isDigit(date.charAt(1)) && Character.isDigit(date.charAt(2)) && Character.isDigit(date.charAt(3)) && Character.isDigit(date.charAt(5)) && Character.isDigit(date.charAt(6)) && Character.isDigit(date.charAt(8)) && Character.isDigit(date.charAt(9)))) {
            return false;
        }

        return true;
    }

    public static MenuItem getItem(ArrayList<MenuItem> items, String id) {
        MenuItem empty = new MenuItem("0", "empty", (float) 0.0);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getID().equals(id)) {
                return items.get(i);
            }
        }
        return empty;
    }

    public static Boolean hasItem(ArrayList<MenuItem> items, String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////// ////////////////// On Login ////////////////////////// ////////////////////////////////////

    /*public static void setFillLevels() {
        fillLevels = new Vector<Float>();

        fillLevels.add((float) 100.0); // chicken breast
        fillLevels.add((float) 100.0); // flour
        fillLevels.add((float) 100.0); // salt
        fillLevels.add((float) 100.0); // black pepper
        fillLevels.add((float) 100.0); // fries
        fillLevels.add((float) 100.0); // thick bread
        fillLevels.add((float) 100.0); // potato salad
        fillLevels.add((float) 100.0); // liquid margerine
        fillLevels.add((float) 100.0); // garlic powder
        fillLevels.add((float) 100.0); // ranch
        fillLevels.add((float) 100.0); // ketchup container
        fillLevels.add((float) 100.0); // ketchup packets
        fillLevels.add((float) 100.0); // mayo
        fillLevels.add((float) 100.0); // tea bags
        fillLevels.add((float) 100.0); // sugar for tea
        fillLevels.add((float) 100.0); // worcestershire sauce
        fillLevels.add((float) 100.0); // sliced cheese
        fillLevels.add((float) 100.0); // bacon
        fillLevels.add((float) 100.0); // fryer oil

        uConversion.put(1, fillLevels);
    }*/

    public static void populateUnitConversion () {
        try {
            String getDataQuery = "SELECT * FROM unit_conversion;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(getDataQuery);

            // get ingredient SKUs and add them to an arraylist
            ResultSetMetaData rsmd = result.getMetaData();
            int ingredientCount = rsmd.getColumnCount() - 2; // subtract 2 to account for menu item name and id
            ingredientSKUs = new ArrayList<String>();
            for (int i = 3; i < ingredientCount + 3; i++) {
                ingredientSKUs.add(rsmd.getColumnName(i));
            }

            uConversion = new Hashtable<Integer, Vector<Float>>(); // initialize unit conversion hashtable
            itemIDs = new ArrayList<String>(); // initialize item id list

            // parse the table line by line and use the data to populate the hashtable
            while (result.next()) {
                int thisItem = result.getInt("item_id"); // get the menu item id (e.g. 501, 502, etc)
                itemIDs.add(String.valueOf(thisItem)); // add to the arraylist of menu item IDs
                Vector<Float> vals = new Vector<Float>();
                for (int i = 0; i < ingredientSKUs.size(); i++) {
                    vals.add(result.getFloat(ingredientSKUs.get(i))); // get the next ingredient SKU and use it to request the unit conversion float and add it to vals
                }
                
                uConversion.put(thisItem, vals); // add new entry to hashtable with the menu item id and the ingredient values
            }


        } catch (Exception e) {
            System.out.println("Error populating unit conversion data:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            
        }
    }
    
    /////////////////////////////////// ////////////////// GUI Login / Logout ////////////////////////// ////////////////////////////////////

    public static void login (String userPassword) {
        //login information for database
        String teamNumber = "2";
        String sectionNumber = "900";
        String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";


        try {
            //create a connection to database
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbConnectionString, userName, userPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        populateUnitConversion();
        //setFillLevels();
        //JOptionPane.showMessageDialog(null,"Opened database successfully");
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
    
    ////////////////////////////////////////// //////////// SERVER INTERFACE /////////////// /////////////////////////////////////////
   
    public static void addToOrder (String itemName, String item_num, ArrayList<String> OrderList, ArrayList<String> QueryList) {
        try {
            Statement stmt = conn.createStatement();
            String priceQuery = "SELECT item_price, item_description FROM menu_items WHERE item_id = " + item_num + ";";
            ResultSet priceResult = stmt.executeQuery(priceQuery);
           
            String price = "";
            String description = "";

            while (priceResult.next()) {
                price = priceResult.getString("item_price");
                description = priceResult.getString("item_description");
            }
            System.out.println(price);
                
            String orderLine = itemName + "@" + description + "@" + price + "@" + item_num;
            OrderList.add(orderLine);

            System.out.println("Added to orderList:");
            System.out.println(orderLine);
            System.out.println();

            System.out.println("Items in order:");
            // For Each Loop for iterating ArrayList
            for (String item : OrderList ){
                // Printing the elements of ArrayList
                System.out.println(item);
            }
            System.out.println();

            //System.out.println("Added to queryList:");
            //System.out.println(queryLine);
            
        } catch (Exception e) {
            System.out.println("Error adding to order:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static Double calculateTotal(Double total, ArrayList<String> OrderList) {
        for (String items : OrderList) {
            System.out.println(items);
            String[] arr = new String[4];
            arr = items.split("@");
            String price = arr[2];
            System.out.println(price);
            total += Double.parseDouble(price.substring(1));
            System.out.println(total);
        }
        return total;

    }

    public static void deleteCurrentOrder(ArrayList<String> OrderList) {
        OrderList.clear();
        System.out.println("Order has been deleted. There are " + OrderList.size() + " items");
        JOptionPane.showMessageDialog(null, "Current Order was deleted.", "Order Deleted",JOptionPane.INFORMATION_MESSAGE);
    
    }

    public static void updateIngredientsFromOrder(String orderItems) {
        try {
            Statement stmt = conn.createStatement();
            String[] items = orderItems.split(" ");
            
            String tableName = "inventory";
            String columnName = "inventory_quantity";

            for (int i = 0; i < items.length; i++) {
                String itemID = items[i];
                Vector<Float> conversionList = uConversion.get(Integer.valueOf(itemID)); // list pulled from uConversion hashtable

                Vector<String> SKUs = new Vector<String>(); // SKUs of the ingredients we are changing in the inventory
                Vector<Float> conversionVals = new Vector<Float>(); // amount (per ingredient) to subtract from the inventory

                for (int j = 0; j < conversionList.size(); j++) {
                    // add SKU and conversion value if the current menu item uses this ingredient
                    if (conversionList.get(j) > 0) {
                        SKUs.add(ingredientSKUs.get(j));
                        conversionVals.add(conversionList.get(j));
                    }
                }

                for (int j = 0; j < SKUs.size(); j++) {
                    String getRowQuery = "SELECT * FROM " + tableName + " WHERE inventory_sku LIKE '%" + SKUs.get(j) + "%';"; // get the row of the ingredient we're updating
                    ResultSet result = stmt.executeQuery(getRowQuery);
                    result.next();
                    Float existingAmount = Float.valueOf(result.getString(columnName)); // get the amount of this ingredient that is in the inventory

                    existingAmount -= conversionVals.get(j); // update the value

                    String updateAmount = "UPDATE " + tableName + " SET " + columnName + "='" + String.valueOf(existingAmount) + "' WHERE inventory_sku LIKE '%" + SKUs.get(j) + "%';";
                    System.out.println(updateAmount);
                    stmt.executeUpdate(updateAmount);
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating ingredients from order:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);   
        }
    }

    public static void createOrder(int orderNum, String orderItems, String orderTotal, String payment, LocalDate date) {
        try{
            String thisDate = date.toString();

            Statement stmt = conn.createStatement();
            String orderQuery = "INSERT INTO Orders (order_id, order_items, order_total, order_payment, order_date) VALUES (" + orderNum + ", \'" + orderItems + "\', \'" + orderTotal + "\', \'" + payment + "\', \'" + thisDate + "\');";
            System.out.println(orderQuery);
            stmt.execute(orderQuery);
            updateIngredientsFromOrder(orderItems);
            System.out.println("Order was created successfully.");
            JOptionPane.showMessageDialog(null, "Order was successfully placed.", "Order Completed",JOptionPane.INFORMATION_MESSAGE);
    
            
        } catch (Exception e) {
            System.out.println("Error creating order:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            
        }
    }

    public static int orderNum() {
        try{
            Statement stmt = conn.createStatement();
            String rowQuery = "SELECT ROW_NUMBER() OVER() AS rowNum FROM Orders;";
            ResultSet rowResult = stmt.executeQuery(rowQuery);
            int orderNum = 0;
            while(rowResult.next()) {
                orderNum = Integer.parseInt(rowResult.getString("rowNum"));
            }
            orderNum += 1;
            return orderNum;
        } catch (Exception e) {
            System.out.println("Error getting order id");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return 0;
        }
    }

    /////////////////////////////////// ////////////////// MANAGER INTERFACE ////////////////////////// ////////////////////////////////////

        
    public static String charRepeat(int x) { // to format the display of menu items
        String space = " ";
        return (space.repeat(x));
    }

    public static void viewEntireMenu() {
        String name = "item_id   item_name               item_price             item_description " + "\n";
        int x = 0;
        int lengthOfString = 0;
        try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM menu_items;";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                //lengthOfString = length(result.getString("item_name"));
                lengthOfString = result.getString("item_name").length();
                x = 4 + (27 - lengthOfString);
                //System.out.println("x is: " + x);
                name += result.getString("item_id") + "          " + result.getString("item_name") + charRepeat(x) + result.getString("item_price") + "             " + result.getString("item_description") +"\n";
            }

            JOptionPane.showMessageDialog(null, name);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
    }
    public static void getInventoryInfo(ArrayList<String> ingrSKU, String[] ingrName) {
         try {
            Statement stmt = conn.createStatement();
            String inventoryQuery = "SELECT inventory_sku, inventory_item FROM inventory;";
            
            ResultSet inventoryResult = stmt.executeQuery(inventoryQuery);
           
            String sku = "";
            String name = "";

            while (inventoryResult.next()) {
                sku += inventoryResult.getString("inventory_sku") + "@";
                name += inventoryResult.getString("inventory_item") + "@";
            }
            

            String[] arrSku = sku.split("@");
            String[] arrName = name.split("@");

            int i = 0;
            for (String temp : ingrSKU) {
                for(int j = 0; j < arrSku.length; j++) {
                    if (temp.contains(arrSku[j])) {
                        ingrName[i] = arrName[j];
                    }
                }
                i++;
            }
            
        } catch (Exception e) {
            System.out.println("Error adding to order:");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void viewMenuFromInput() { // view menu item when user inputs item id
        String name = "item_id         item_name            item_price                   item_description " + "\n";

        try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM menu_items WHERE item_id = '" + LaynesGUI.itemIdFromTextField + "';";
            System.out.println("hahaha: " + LaynesGUI.itemIdFromTextField);
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                name += result.getString("item_id") + "                 " + result.getString("item_name") + "             " + result.getString("item_price") + "                       " + result.getString("item_description") +"\n";
            }

            JOptionPane.showMessageDialog(null, name);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
    }

    public static void addMenuItemFromInput() { // view menu item when user inputs item id
        //String name = "item_id  item_name  item_price  item_description " + "\n";

        try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "INSERT INTO menu_items (item_id, item_name, item_description, item_price, item_ingredients) VALUES(" + LaynesGUI.addMenuItemsItemId + ",'" + LaynesGUI.addMenuItemsItemName + "','" + LaynesGUI.addMenuItemsItemDesc + "','" + LaynesGUI.addMenuItemsItemPrice + "','" + LaynesGUI.addMenuItemsItemIngre + "'" + ");";
            //System.out.println("hahaha: " + LaynesGUI.itemIdFromTextField);
            System.out.println("sql statement: " + sqlStatement);
            //ResultSet result = 
            stmt.execute(sqlStatement);

            /*if(result.next()) {
                JOptionPane.showMessageDialog(null, "Successfully added menu item!");
            }*/
            JOptionPane.showMessageDialog(null, "Successfully added menu item!");
            
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Could not add this item.");
        }
    }

    public static void updateMenuItemFromInput() { // update menu item. if user does not change text field prompt, then that value does not get updated
 
        
        ArrayList<String> attributeNames = new ArrayList<String>();
        ArrayList<String> attributeValues = new ArrayList<String>();
        
        attributeNames.add("item_name");
        attributeValues.add(LaynesGUI.updateMenuItemsItemName);

        attributeNames.add("item_description");
        attributeValues.add(LaynesGUI.updateMenuItemsItemDesc);

        attributeNames.add("item_price");
        attributeValues.add(LaynesGUI.updateMenuItemsItemPrice);

        attributeNames.add("item_ingredients");
        attributeValues.add(LaynesGUI.updateMenuItemsItemIngre);

        try{
            Statement stmt = conn.createStatement();

            for(int i = 0; i < attributeNames.size(); i++){

                if(attributeValues.get(i).contains("Enter")){
                    // do nothing

                }
                else {
                    String sqlStatement = "UPDATE menu_items SET " + attributeNames.get(i) + " = '" + attributeValues.get(i) + "' WHERE item_id = " + LaynesGUI.updateMenuItemsItemId + ";";
                    System.out.println("sql statement: " + sqlStatement);
                    stmt.executeUpdate(sqlStatement);
                }
                
            }

            JOptionPane.showMessageDialog(null, "Successfully updated menu item!");
            
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Could not add this item.");
        }
    }

    // FOR ORDER TRENDS AND ORDER POPULARITY
    public static DefaultCategoryDataset createDiffDataset(ArrayList<MenuItem> items1, ArrayList<MenuItem> items2, String startDate1, String startDate2, String endDate1, String endDate2) { // returns a dataset that can be displayed as a chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 2 lines representing the integer amount of each item
        /*String series1 = startDate1.replace("-", "/") + " - " + endDate1.replace("-", "/");
        String series2 = startDate2.replace("-", "/") + " - " + endDate2.replace("-", "/");

        for (int i = 0; i < items1.size(); i++) {
            String xval = items1.get(i).getID();
            int yval = items1.get(i).getOrdered();
            dataset.addValue(yval, series1, xval);
        }

        for (int i = 0; i < items2.size(); i++) {
            String xval = items2.get(i).getID();
            int yval = items2.get(i).getOrdered();
            dataset.addValue(yval, series2, xval);
        }*/

        
        float totalRevenue1 = 0;
        for (int i = 0; i < items1.size(); i++) {
            totalRevenue1 += items1.get(i).getTotalCost();
        }

        Vector<String> itemID1 = new Vector<String>();
        Vector<Integer> itemPerc1 = new Vector<Integer>();
        for (int i = 0; i < items1.size(); i++) {
            String x = items1.get(i).getID();
            Integer y = (int) Math.round(items1.get(i).getTotalCost() / totalRevenue1 * 100);
            itemID1.add(x);
            itemPerc1.add(y);
        }
        
        float totalRevenue2 = 0;
        for (int i = 0; i < items2.size(); i++) {
            totalRevenue2 += items2.get(i).getTotalCost();
        }

        Vector<String> itemID2 = new Vector<String>();
        Vector<Integer> itemPerc2 = new Vector<Integer>();
        for (int i = 0; i < items2.size(); i++) {
            String x = items2.get(i).getID();
            Integer y = (int) Math.round(items2.get(i).getTotalCost() / totalRevenue2 * 100);
            itemID2.add(x);
            itemPerc2.add(y);
        }

        Map<String, Integer> trends = new HashMap<String, Integer>();
        for (int i = 0; i < itemID1.size(); i++) {
            String id = itemID1.get(i);
            int percentageDiff = 0;
            if (itemID2.contains(id)) {
                percentageDiff = itemPerc2.get(itemID2.indexOf(id)) - itemPerc1.get(i);
            } else {
                percentageDiff = -1 * itemPerc1.get(i);
            }
            trends.put(id, -1 * percentageDiff);
        }

        for (int i = 0; i < itemID2.size(); i++) {
            String id = itemID2.get(i);
            int percentageDiff = 0;
            if (!itemID1.contains(id)) {
                percentageDiff = -itemPerc2.get(i);
                trends.put(id, -1 * percentageDiff);
            }
        }

        String trendSeries = "Revenue Percentage Trends in Menu Items";
        List<Entry<String, Integer>> trendList = new LinkedList<Entry<String, Integer>>(trends.entrySet());
        Collections.sort(trendList, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        ListIterator mapIterator = trendList.listIterator();

        while (mapIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) mapIterator.next();
            String itemID = (String) mapElement.getKey();
            Integer itemTrend = -1 * (Integer) mapElement.getValue();
            dataset.addValue(itemTrend, trendSeries, itemID);
        }


        return dataset;
    }

    public static ArrayList<String> findDifferences(ArrayList<MenuItem> items1, ArrayList<MenuItem> items2) { // list differences between timeframes
        
        ArrayList<String> entries = new ArrayList<String>();
        String title = "Differences between the two timeframes:";
        entries.add(title);

        ArrayList<String> alreadyDone = new ArrayList<String>(); // items we've already checked
        for (int i = 0; i < items1.size(); i++) {
            MenuItem thisItem = items1.get(i);
            alreadyDone.add(thisItem.getID());

            String difference = "difference";
            if (hasItem(items2, thisItem.getID())) {
                MenuItem thatItem = getItem(items2, thisItem.getID());
                int diff = Math.abs(thisItem.getOrdered() - thatItem.getOrdered());
                if (thisItem.getOrdered() > thatItem.getOrdered()) {
                    difference = thisItem.getName() + ": Timeframe 1 has " + Integer.toString(diff) + " more orders than Timeframe 2.";
                } else {
                    difference = thisItem.getName() + ": Timeframe 2 has " + Integer.toString(diff) + " more orders than Timeframe 1.";
                }
            } else {
                difference = thisItem.getName() + ": Timeframe 1 has " + thisItem.getOrdered() + " orders and Timeframe 2 has none.";
            }
            entries.add(difference);
        }

        for (int i = 0; i < items2.size(); i++) {
            MenuItem thisItem = items2.get(i);
            if (!hasItem(items1, thisItem.getID())) {
                String difference = thisItem.getName() + ": Timeframe 2 has " + thisItem.getOrdered() + " orders and Timeframe 1 has none.";
                entries.add(difference);
            }
        }

        return entries;
    }
    
    public static ArrayList<String> findItemsOrdered(String startDate, String endDate, ArrayList<MenuItem> items) {
        // list the amount 
       ArrayList<String> itemList = new ArrayList<String>();
       String title = "All orders between " + startDate + " and " + endDate + ":";
       itemList.add(title);

       try {
           Statement stmt = conn.createStatement();
           String query = "SELECT * FROM orders WHERE order_date BETWEEN '" + startDate + "' AND '" + endDate + "';";
           ResultSet result = stmt.executeQuery(query);

           ArrayList<String> itemsOrdered = new ArrayList<String>();
           while (result.next()) {
               String[] theseItems = result.getString("order_items").split(" ");
               for (int i = 0; i < theseItems.length; i++) {
                   itemsOrdered.add(theseItems[i].strip());
               }
           }

           for (int i = 0; i < itemsOrdered.size(); i++) {
               String thisItem = itemsOrdered.get(i);

               // check to see if item is already in list
               Boolean exists = false;
               for (int j = 0; j < items.size(); j++) {
                   //System.out.println(items.get(j).getID() + ", " + thisItem + ", " + (items.get(j).getID().equals(thisItem)));
                   if (items.get(j).getID().equals(thisItem)) {
                       exists = true;
                       items.get(j).addToAmount(); // just add one to the item amount and move on
                       break;
                   }
               }
               if (exists) { continue; }

               // if item does not already exist
               String query2 = "SELECT * FROM menu_items WHERE item_id = " + thisItem + ";";
               ResultSet result2 = stmt.executeQuery(query2);
               result2.next();
               String itemName = result2.getString("item_name");
               String priceStr = result2.getString("item_price");
               Float price = Float.parseFloat(priceStr.substring(1));

               items.add(new MenuItem(thisItem, itemName, price));
           }

           Collections.sort(items);

           float totalRevenue = 0;
           for (int i = 0; i < items.size(); i++) {
               totalRevenue += items.get(i).getTotalCost();
           }

           for (int i = 0; i < items.size(); i++) {
               MenuItem thisItem = items.get(i);
               int percentage = (int) Math.round(thisItem.getTotalCost() / totalRevenue * 100);
               /*String orders = "orders";
               if (thisItem.getOrdered() == 1) { orders = "order"; }
               String entry = thisItem.getID() + ": " + thisItem.getOrdered() + " " + orders + ". (" + thisItem.getName() + ")";*/
               String entry =  String.format("%s: %-28s%-16s", thisItem.getID(), thisItem.getName(), " Total: " + thisItem.getOrdered() + " (" + thisItem.getTotalValue() + ", " + percentage + "%)");
               itemList.add(entry);
           }

       } catch (Exception e) {
           System.out.println("Error querying orders between " + startDate + " and " + endDate + ":");
           e.printStackTrace();
           System.err.println(e.getClass().getName() + ": " + e.getMessage());
           System.exit(0);
       }

       return itemList;
   }

    public static ArrayList<String> computeItemsOrdered(String startDate, String endDate, ArrayList<MenuItem> items) {
         // list the amount 
        ArrayList<String> itemList = new ArrayList<String>();
        String title = "All orders between " + startDate + " and " + endDate + ":";
        itemList.add(title);

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM orders WHERE order_date BETWEEN '" + startDate + "' AND '" + endDate + "';";
            ResultSet result = stmt.executeQuery(query);

            ArrayList<String> itemsOrdered = new ArrayList<String>();
            while (result.next()) {
                String[] theseItems = result.getString("order_items").split(" ");
                for (int i = 0; i < theseItems.length; i++) {
                    itemsOrdered.add(theseItems[i].strip());
                }
            }

            for (int i = 0; i < itemsOrdered.size(); i++) {
                String thisItem = itemsOrdered.get(i);

                // check to see if item is already in list
                Boolean exists = false;
                for (int j = 0; j < items.size(); j++) {
                    //System.out.println(items.get(j).getID() + ", " + thisItem + ", " + (items.get(j).getID().equals(thisItem)));
                    if (items.get(j).getID().equals(thisItem)) {
                        exists = true;
                        items.get(j).addToAmount(); // just add one to the item amount and move on
                        break;
                    }
                }
                if (exists) { continue; }

                // if item does not already exist
                String query2 = "SELECT * FROM menu_items WHERE item_id = " + thisItem + ";";
                ResultSet result2 = stmt.executeQuery(query2);
                result2.next();
                String itemName = result2.getString("item_name");
                Float price = Float.valueOf(result2.getString("item_price").substring(1));
                MenuItem mItem = new MenuItem(thisItem, itemName, price);
                mItem.setByAmount();
                items.add(mItem);
            }

            Collections.sort(items);

            for (int i = 0; i < items.size(); i++) {
                MenuItem thisItem = items.get(i);
                //String entry = thisItem.getID() + ", " + thisItem.getName() + ", "  + thisItem.getOrdered();
                String orders = "orders";
                if (thisItem.getOrdered() == 1) { orders = "order"; }
                String entry = thisItem.getID() + ": " + thisItem.getOrdered() + " " + orders + ". (" + thisItem.getName() + ")";
                itemList.add(entry);
            }

        } catch (Exception e) {
            System.out.println("Error querying orders between " + startDate + " and " + endDate + ":");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return itemList;
    }

    // FOR INVENTORY USAGE PANEL

    public static Map<String, Integer> itemsAmountMap(String startDate, String endDate) { // function to create a hashmap of items ordered and their individual amounts in a date range
        ArrayList<String> itemsOrdered = new ArrayList<String>();
        itemsOrderedHash = new HashMap<String, Integer>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM orders WHERE order_date BETWEEN '" + startDate + "' AND '" + endDate + "';";
            ResultSet result = stmt.executeQuery(query);

            //ArrayList<String> itemsOrdered = new ArrayList<String>();
            while (result.next()) {
                String[] theseItems = result.getString("order_items").split(" ");
                for (int i = 0; i < theseItems.length; i++) {
                    itemsOrdered.add(theseItems[i].strip());
                }
            }


            for (String i : itemsOrdered) {
                Integer j = itemsOrderedHash.get(i);
                itemsOrderedHash.put(i, (j == null) ? 1 : j + 1);
            }

            // printing the hashmap
            
            /*for (Map.Entry<String, Integer> val : itemsOrderedHash.entrySet()) {
                System.out.println("Item " + val.getKey() + " " + "is ordered" + ": " + val.getValue() + " times");
            }*/

        } catch (Exception e) {
            System.out.println("Error querying orders between " + startDate + " and " + endDate + ":");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return itemsOrderedHash;
    }

    public static Map<String, String> itemsIngredientsMap(Map<String, Integer> itemsAmount){ // function to create a hashmap of items ordered and string (list) of ingredients that the items use

        //itemsIngredientsHash = new HashMap<>()

        itemsIngredientsHash = new HashMap<String, String>(); // <items_id, item_ingredients>
        
        ArrayList<Integer> itemIdsList = new ArrayList<Integer>();
        itemIngredientsList = new ArrayList<String>();

        try {
            String temp = null;
            Integer itemID =0;

            Statement stmt = conn.createStatement();
            Iterator mapIterator = itemsAmount.entrySet().iterator();

            // adds the key of the hashmap itemsAmount into an arraylist
            while (mapIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)mapIterator.next();
                temp = (String)mapElement.getKey(); // typecasted string itemID
                itemID=Integer.parseInt(temp);  
                itemIdsList.add(itemID);
            }

            // looping through the itemsID list and querying to find each menu item's ingredients list
            for(int k = 0; k < itemIdsList.size(); k++){

                String query = "SELECT item_ingredients FROM menu_items WHERE item_id = " + itemIdsList.get(k) + ";";
                ResultSet result = stmt.executeQuery(query);
                String name = "";

                while (result.next()) {
                    name += result.getString("item_ingredients")+"\n";
                }
                itemIngredientsList.add(name);
            }

            // putting item_id and each of their ingredients in a hash map
            for(int i = 0; i < itemIdsList.size(); i++){
                String tempID = String.valueOf(itemIdsList.get(i));
                itemsIngredientsHash.put(tempID, itemIngredientsList.get(i));
            }

            // printing the hashmap
            /*for (Map.Entry<String, String> val : itemsIngredientsHash.entrySet()) {
                //System.out.println("herrrr");
                System.out.println("ITEM " + val.getKey() + " " + "HAS INGREDIENTS" + ": " + val.getValue());
            }*/


        } catch (Exception e) {
            //System.out.println("Error querying orders between " + startDate + " and " + endDate + ":");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return itemsIngredientsHash;
    }

    public static Map<String, Float> ingredientsAmountMap2(Map<String, Integer> itemsAmount) {

        Map<String, Float> newMap = new HashMap<String, Float>();


        Vector<String> listOfIngredients = new Vector<String>();
        listOfIngredients.add("c1001");
        listOfIngredients.add("d2001");
        listOfIngredients.add("d2002");
        listOfIngredients.add("d2003");
        listOfIngredients.add("f3002");
        listOfIngredients.add("d2004");
        listOfIngredients.add("c1002");
        listOfIngredients.add("c1003");
        listOfIngredients.add("d2005");
        listOfIngredients.add("c1004");
        listOfIngredients.add("d2006");
        listOfIngredients.add("d2007");
        listOfIngredients.add("d2008");
        listOfIngredients.add("d2009");
        listOfIngredients.add("d2010");
        listOfIngredients.add("d2011");
        listOfIngredients.add("c1005");
        listOfIngredients.add("c1006");
        listOfIngredients.add("d2012");
        listOfIngredients.add("i1001");



        //Statement stmt = conn.createStatement();
        
        Iterator mapIterator = itemsAmount.entrySet().iterator();

        Map<String, Float> ingredientAmounts = new HashMap<String, Float>(); // contains ingredient sku & (the conversionData * itemsAmount.getValue())

        // adds the key of the hashmap itemsAmount into an arraylist
        while (mapIterator.hasNext()) {

            Map.Entry mapElement = (Map.Entry)mapIterator.next();
            Integer itemID = Integer.parseInt((String)mapElement.getKey()); // typecasted string itemID
            //System.out.println(itemID);
            //itemID=Integer.parseInt(temp);
            Vector<Float> conversionData = uConversion.get(itemID); // conversionData = <0.423, 0.124, 0, 0.642, ...>
            //System.out.print(conversionData.size());

            //break;

            //Integer multiplyer = mapElement.getValue();
            Integer multiplyer = (Integer)mapElement.getValue();

            Float num = (Float) 0.0f;

            for (int i = 0; i < conversionData.size(); i++) {

                //conversionData.set(i, conversionData.get(i) * multiplyer);
                num = conversionData.get(i) * multiplyer;
                
                
                newMap.put(listOfIngredients.get(i), conversionData.get(i) + num);

            }

        }
        
        for (Map.Entry<String, Float> val : newMap.entrySet()) {
            System.out.println(val.getKey() + " = " + val.getValue());
        }

        newMap.values().removeIf(f -> f == 0f);

        return newMap;
    }

    /*public static Map<String, Integer> ingredientsAmountMap(Map<String, Integer> itemsAmount, Map<String, String> itemsIngredients) { // function to create a hashmap of ingredient_sku's and the total amount of each that was ordered

        // multiplying the number of times an item was ordered to the ingredients that item has = total amount of that ingredients used
        ingredientsMultipliedHash = new HashMap<String, String>();

        ArrayList<Integer> itemsAmountNumbers = new ArrayList<Integer>();

        Iterator amountIterator = itemsAmount.entrySet().iterator();

        while(amountIterator.hasNext()){ // populating the amounts arraylist
            Map.Entry mapElement = (Map.Entry)amountIterator.next();
            Integer temp = (Integer)mapElement.getValue(); // typecasted string itemID
            itemsAmountNumbers.add(temp);
        }

        ArrayList<String> ingredientsMultiplied = new ArrayList<String>();

        for(int i = 0; i < itemsAmountNumbers.size(); i++){
            //itemIngredientsList.get(i) = itemIngredientsList.get(i).repeat(itemsAmountNumbers.get(i));
            itemIngredientsList.set(i, itemIngredientsList.get(i).repeat(itemsAmountNumbers.get(i)));
        }

        ingredientsAmountHash = new HashMap<String, Integer>();

        //ArrayList<String> allIngredients = new ArrayList<String>();
        ArrayList<String> ingredientsParsed = new ArrayList<String>();

        Iterator mapIterator = itemsIngredients.entrySet().iterator();
        String ingr;

        ArrayList<String> rip = new ArrayList<String>();

        for(int i = 0; i < itemIngredientsList.size(); i++){

            ArrayList<String> elephantList = new ArrayList<>(Arrays.asList(itemIngredientsList.get(i).split(", ")));

            for(int j = 0; j < elephantList.size(); j++){
                String sku = elephantList.get(j);
                ingredientsParsed.add(sku);
            }
        }

        for (int i = 0; ingredientsParsed.size() > i; i++) {
            String item = ingredientsParsed.get(i);
            //item.replaceAll("\n", "");
            ingredientsParsed.set(i,item.replaceAll("\n", ""));
        }

        for(int i = 0; i < ingredientsParsed.size(); i++){
            String toSplit  = "";
            //ArrayList<String> elephantList = new ArrayList<>(Arrays.asList(ingredientsParsed.get(i).split(", ")));
            if(ingredientsParsed.get(i).length() > 5){
                //System.out.println("here");
                for(int j = 0; j < ingredientsParsed.get(i).length(); j += 5) {
                    toSplit = ingredientsParsed.get(i).substring(j, Math.min(ingredientsParsed.get(i).length(), j + 5));
                    
                }
                ingredientsParsed.set(i, ingredientsParsed.get(i).substring(0, 5));
                ingredientsParsed.add(toSplit);
                //System.out.println(toSplit);
                
            }
        }

        // ingredientsParsed contains all the individual ingredients used in the order in that time frame, with repeating ingredients



        // now make hashmap that contains each sku as key and their # of occurrences as value
        for (String i : ingredientsParsed) {
            //System.out.println(i);
            Integer j = ingredientsAmountHash.get(i);
            ingredientsAmountHash.put(i, (j == null) ? 1 : j + 1);
        }

        return ingredientsAmountHash;
    }*/

    public static ArrayList<String> ingredientsList(Map<String, Float> ingrHash) {

        ArrayList<String> retList = new ArrayList<String>();

        Iterator ingrIterator = ingrHash.entrySet().iterator();

        while(ingrIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)ingrIterator.next();
            String toAdd = (String)mapElement.getKey();
            retList.add(toAdd);
        }

        return retList;
    }

    public static ArrayList<Float> amountList(Map<String, Float> ingrHash) {
        ArrayList<Float> retList = new ArrayList<Float>();

        Iterator amountIterator = ingrHash.entrySet().iterator();

        while(amountIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)amountIterator.next();
            Float toAdd = (Float)mapElement.getValue();
            retList.add(toAdd);
        }

        return retList;
    }

    // FOR INVENTORY POPULARITY

    public static Map<String, Float> sortMap(Map<String, Float> map) {

        List<Entry<String, Float>> list = new LinkedList<Entry<String, Float>>(map.entrySet());
        
        Collections.sort(list, new Comparator<Entry<String, Float>>() {
            @Override
            public int compare(Entry<String, Float> o1, Entry<String, Float> o2){
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        kkMap = new LinkedHashMap<String, Float>();

        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<Float> vals = new ArrayList<Float>();

        for(Entry<String, Float> o: list){
            keys.add(o.getKey());
            vals.add(o.getValue());
        }


        for(int i = keys.size()-1; i >= 0; i--){ // reversing the order to make ingredients most used -> least used
            kkMap.put(keys.get(i), vals.get(i));
        }
        
        /*for (Map.Entry<String, Integer> val : kkMap.entrySet()) {
            System.out.println("ingredient SKU " + val.getKey() + " " + "occurs" + ": " + val.getValue() + " times");
        }*/

        return kkMap;
    }

}

