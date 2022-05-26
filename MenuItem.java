import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

class MenuItem implements Comparable<MenuItem> {
    private String item_id;
    private String item_name;
    private int amountOrdered;
    private float cost;
    private float totalCost;
    private boolean byCost;
    

    public MenuItem (String id, String name, float item_cost) {
        item_id = id;
        item_name = name;
        amountOrdered = 1;
        cost = item_cost;
        totalCost = item_cost;
        byCost = true;
    }

    public String getID() { return item_id; }
    public String getName() { return item_name; }
    public int getOrdered() { return amountOrdered; }
    public float getCost() { return cost; }
    public float getTotalCost() { return totalCost; }
    public void setByAmount() { byCost = false; }
    public void addToAmount() {
        amountOrdered += 1;
        totalCost = (float) Math.round(cost * (float) amountOrdered * 100) / 100;
    }

    public String getTotalValue() {
        String result = "$" + String.valueOf(totalCost);
        if (result.charAt(result.length() - 2) == '.') {
            result += "0";
        }
        return result;
    }

    @Override public int compareTo(MenuItem o) {
        float thisVal = totalCost;
        float thatVal = o.getTotalCost();
        if (!byCost) {
            thisVal = (int) amountOrdered;
            thatVal = (int) o.getOrdered();
        }

        if (thisVal > thatVal) {
            return -1;
        } else if (thisVal < thatVal) {
            return 1;
        } else {
            return 0;
        }
    }
}