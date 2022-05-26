import java.util.*;

public class OrderItem {
    private String item_id;
    private ArrayList<Strings> item_ingredients;
    private ArrayList<Strings> item_ingredients_unit_conversions; // the unit conversions of the item's ingredients in order

    public String getID() { return item_id; }

    // list item_ingredients
    public static ArrayList<String> itemsIngredientsList(String item_id){ // function to create a hashmap of items ordered and string (list) of ingredients that the items use

        ArrayList<String> itemIngredientsList = new ArrayList<String>();

        try {
            Statement stmt = conn.createStatement();
            Iterator mapIterator = itemsAmount.entrySet().iterator();

            String query = "SELECT item_ingredients FROM menu_items WHERE item_id = " + OrderItem.getID() + ";";
            ResultSet result = stmt.executeQuery(query);
            String name = "";

            while (result.next()) {
                name += result.getString("item_ingredients")+"\n";
            }
            itemIngredientsList.add(name);

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

    public OrderItem (String id) {
        item_id = id;
        item_ingredients = ingredients;
        item_ingredients_unit_conversions = uc;
    }

    
    public int getOrdered() { return amountOrdered; }

    public Map<String, String> getItemIngredients() {

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
    }
}
