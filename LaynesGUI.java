import java.util.*; 
import java.io.*;
import java.lang.Thread.State;
//import java.rmi.activation.ActivationDesc;
import java.sql.*;
import java.time.LocalDate;
import java.awt.List;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.*;
import java.awt.Graphics;
import javax.swing.border.Border;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.border.Border;
import javax.swing.SwingConstants;
import java.util.Map.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class LaynesGUI {
	static JFrame f;
    ArrayList<String> OrderList;
    ArrayList<String> QueryList;
    public static String itemIdFromTextField;
    public static int invItemIndex;

    //arraylist of inventory items
    ArrayList<JPanel> invItemList = new ArrayList<JPanel>();
    boolean updatingInventory = false;
    boolean rowSelected = false;

    // variables in add menu items panel
    public static int addMenuItemsItemId = 0;
    public static String addMenuItemsItemName;
    public static String addMenuItemsItemDesc;
    public static String addMenuItemsItemPrice;
    public static String addMenuItemsItemIngre;

    // variables in update menu items panel
    public static int updateMenuItemsItemId = 0;
    public static String updateMenuItemsItemName;
    public static String updateMenuItemsItemDesc;
    public static String updateMenuItemsItemPrice;
    public static String updateMenuItemsItemIngre;

    // variables for general menu
    public static Color backgroundColor;
    public static Color backButtonColor;

    // variables in inventory usage chart
    public static Map<String, Integer> itemsAndTheirAmount = new HashMap<String, Integer>();
    public static Map<String, String> itemsAndTheirIngredients = new HashMap<String, String>();
    public static Map<String, Float> ingredientsAndTheirAmount = new HashMap<String, Float>();

    int order_id;
   


    public LaynesGUI  () {
        // class constructor
    	OrderList = new ArrayList<String>();
        QueryList = new ArrayList<String>();
        
        backgroundColor = new Color(240, 247, 255);
        backButtonColor = new Color(198, 227, 243);
    }
    public void Finalize() {
        guiLogout();
    }
    public void chooseInterface() {
        f = new JFrame("Interface");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        //Question to ask what user
        JLabel labelM = new JLabel("Are you a Manager or Server?");
        labelM.setBounds(400, 50, 400, 300);
        labelM.setFont(new Font("Serif", Font.PLAIN, 20));
        f.add(labelM);

        //Create server Button option
        //String serverButtonStyle = "<html><font color=\"#FF0080\"><u>Server</u></font></html>";
        JButton server = new JButton("Server");
        server.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == server) {
                    f.dispose();
        			serverPanel();
        		}
        	}
        } );
        server.setBounds(565, 300, 170, 50);
        server.setBackground(new Color(200, 226, 238));

        //server.border()

        f.add(server);

        
        //Create manager Button option
        JButton manager = new JButton("Manager");
        manager.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == manager) {
                    f.dispose();
        			managerPanel();
        		}
        	}
        } );
        manager.setBounds(265, 300, 170, 50);
        manager.setBackground(new Color(200, 226, 238));
        f.add(manager);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

         //logout button
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == logout){
                    f.dispose();
                   guiLogout();
                   StartGUI();
                }
            }
        } );
        logout.setBounds(850, 500, 100, 30);
        logout.setBackground(new Color(200, 226, 238));
        f.add(logout);

        f.setVisible(true);
        f.show();
    }

    ////////////////////////////////////////// //////////// SERVER INTERFACE /////////////// /////////////////////////////////////////

    public void serverPanel() { 
    	f = new JFrame("Server");
        //set size and location of frame
        f.setSize(1300, 600);
        f.setLocation(100,100);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel

        f.setDefaultLookAndFeelDecorated(true);

        JLabel thislabel = new JLabel("Menu Items");
        //thislabel.setBounds(50, 30, 500, 120);
       // thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        thislabel.setVerticalAlignment(JLabel.TOP);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        thislabel.setBounds(50, 25, 500, 120);
        f.add(thislabel);

        //menu items in order panel
        JPanel order = new JPanel();
        order.setBounds(950, 110, 250, 340);
        order.setBackground(new Color(255,255,255));
        BoxLayout boxLayoutManager = new BoxLayout(order, BoxLayout.Y_AXIS);
		order.setLayout(boxLayoutManager);
        

        JLabel orderlabel = new JLabel("Order Items");
        orderlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderlabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 15));
        order.add(orderlabel);

        JSeparator sep = new JSeparator(); 
        sep.setBounds(50, 120, 280, 5); 
        order.add(sep);
        
        
        JTextArea orderItems = new JTextArea();
        for(String item : OrderList) {
            String[] arr = new String[4];
            arr = item.split("@");
            orderItems.append(arr[0] + System.getProperty("line.separator"));
        
        }

        orderItems.setEditable(false);
        orderItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        //orderItems.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        order.add(orderItems);
        order.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.add(order);

        //Chicken Finger Title
        JLabel chickenLabel = new JLabel("Chicken Fingers");
        chickenLabel.setBounds(50, 75, 250, 50);
        f.add(chickenLabel);

        //5 Finger combo button
        JButton chicken1 = new JButton("5 Finger Original");
        chicken1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == chicken1) {
                    InterfaceActions.addToOrder("5 Finger Original", "501", OrderList, QueryList);
                    orderItems.append("5 Finger Original\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        chicken1.setBounds(50, 130, 160, 30);
        chicken1.setBackground(new Color(198, 227, 243));
        chicken1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        chicken1.setOpaque(true);
        f.add(chicken1);

        //4 Finger combo button
        JButton chicken2 = new JButton("4 Finger Meal");
        chicken2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == chicken2) {
                    InterfaceActions.addToOrder("4 Finger Meal", "502", OrderList, QueryList);
                    orderItems.append("4 Finger Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        chicken2.setBounds(220, 130, 160, 30);
        chicken2.setBackground(new Color(198, 227, 243));
        chicken2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        chicken2.setOpaque(true);
        f.add(chicken2);

        //3 Finger combo button
        JButton chicken3 = new JButton("3 Finger Meal");
        chicken3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == chicken3) {
                    InterfaceActions.addToOrder("3 Finger Meal", "503", OrderList, QueryList);
                    orderItems.append("3 Finger Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        chicken3.setBounds(390, 130, 160, 30);
        chicken3.setBackground(new Color(198, 227, 243));
        chicken3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        chicken3.setOpaque(true);
        f.add(chicken3);

        //Kids combo button
        JButton chicken4 = new JButton("Kids Meal");
        chicken4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == chicken4) {
                    InterfaceActions.addToOrder("Kids Meal", "504", OrderList, QueryList);
                    orderItems.append("Kids Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        chicken4.setBounds(560, 130, 160, 30);
        chicken4.setBackground(new Color(198, 227, 243));
        chicken4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        chicken4.setOpaque(true);
        f.add(chicken4);

        //Sandwiches Title
        JLabel sandwichLabel = new JLabel("Sandwiches");
        sandwichLabel.setBounds(50, 170, 250, 50);
        f.add(sandwichLabel);

        //Club Sandwich Meal button
        JButton sandwich1 = new JButton("Club Sandwich Meal");
        sandwich1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == sandwich1) {
                    InterfaceActions.addToOrder("Club Sandwich Meal", "507", OrderList, QueryList);
                    orderItems.append("Club Sandwich Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        sandwich1.setBounds(50, 220, 160, 30);
        sandwich1.setBackground(new Color(198, 227, 243));
        sandwich1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
        sandwich1.setOpaque(true);
        f.add(sandwich1);

        //Sandwich Meal button
        JButton sandwich2 = new JButton("Sandwich Meal");
        sandwich2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == sandwich2) {
                    InterfaceActions.addToOrder("Sandwich Meal", "509", OrderList, QueryList);
                    orderItems.append("Sandwich Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        sandwich2.setBounds(220, 220, 160, 30);
        sandwich2.setBackground(new Color(198, 227, 243));
        sandwich2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        sandwich2.setOpaque(true);
        f.add(sandwich2);

        //Club Sandwich only button
        JButton sandwich3 = new JButton("Club Sandwich Only");
        sandwich3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == sandwich3) {
                    InterfaceActions.addToOrder("Club Sandwich Only", "508", OrderList, QueryList);
                    orderItems.append("Club Sandwich Only\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        sandwich3.setBounds(390, 220, 160, 30);
        sandwich3.setBackground(new Color(198, 227, 243));
        sandwich3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
        sandwich3.setOpaque(true);
        f.add(sandwich3);

        //Sandwich Only button
        JButton sandwich4 = new JButton("Sandwich Only");
        sandwich4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == sandwich4) {
                    InterfaceActions.addToOrder("Sandwich Only", "510", OrderList, QueryList);
                    orderItems.append("Sandwich Only\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        sandwich4.setBounds(560, 220, 160, 30);
        sandwich4.setBackground(new Color(198, 227, 243));
        sandwich4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        sandwich4.setOpaque(true);
        f.add(sandwich4);

        //Miscellaneous Title
        JLabel otherLabel = new JLabel("Miscellaneous");
        otherLabel.setBounds(50, 260, 250, 50);
        f.add(otherLabel);

        //Grilled Cheese Meal button
        JButton other1 = new JButton("Grilled Cheese Meal");
        other1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == other1) {
                    InterfaceActions.addToOrder("Grilled Cheese Meal", "511", OrderList, QueryList);
                    orderItems.append("Grilled Cheese Meal\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        other1.setBounds(50, 310, 160, 30);
        other1.setBackground(new Color(198, 227, 243));
        other1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        other1.setOpaque(true);
        f.add(other1);

        //Grilled Cheese Only button
        JButton other2 = new JButton("Grilled Cheese Only");
        other2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == other2) {
                    InterfaceActions.addToOrder("Grilled Cheese Only", "512", OrderList, QueryList);
                    orderItems.append("Grilled Cheese Only\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        other2.setBounds(220, 310, 160, 30);
        other2.setBackground(new Color(198, 227, 243));
        other2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        other2.setOpaque(true);
        f.add(other2);

        //Family Pack button
        JButton other3 = new JButton("Family Pack");
        other3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == other3) {
                    InterfaceActions.addToOrder("Family Pack", "506", OrderList, QueryList);
                    orderItems.append("Family Pack\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        other3.setBounds(390, 310, 160, 30);
        other3.setBackground(new Color(198, 227, 243));
        other3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        other3.setOpaque(true);
        f.add(other3);

        //Impossible Chicken button
        JButton other4 = new JButton("Impossible Chicken");
        other4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == other4) {
                    InterfaceActions.addToOrder("Impossible Chicken", "520", OrderList, QueryList);
                    orderItems.append("Impossible Chicken\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        other4.setBounds(560, 310, 160, 30);
        other4.setBackground(new Color(198, 227, 243));
        other4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        other4.setOpaque(true);
        f.add(other4);

        //Sides Title
        JLabel sidesLabel = new JLabel("Sides");
        sidesLabel.setBounds(50, 340, 250, 50);
        f.add(sidesLabel);

        //Texas Toast button
        JButton side1 = new JButton("Texas Toast");
        side1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == side1) {
                    InterfaceActions.addToOrder("Texas Toast", "515", OrderList, QueryList);
                    orderItems.append("Texas Toast\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        side1.setBounds(220, 390, 160, 30);
        side1.setBackground(new Color(198, 227, 243));
        side1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        side1.setOpaque(true);
        f.add(side1);

        //crinkle cut fries button
        JButton side2 = new JButton("Crinkle Cut Fries");
        side2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == side2) {
                    InterfaceActions.addToOrder("Crinkle Cut Fries", "517", OrderList, QueryList);
                    orderItems.append("Crinkle Cut Fries\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        side2.setBounds(390, 390, 160, 30);
        side2.setBackground(new Color(198, 227, 243));
        side2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        side2.setOpaque(true);
        f.add(side2);

        //potato salad button
        JButton side3 = new JButton("Potato Salad");
        side3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == side3) {
                    InterfaceActions.addToOrder("Potato Salad", "516", OrderList, QueryList);
                    orderItems.append("Potato Salad\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        side3.setBounds(560, 390, 160, 30);
        side3.setBackground(new Color(198, 227, 243));
        side3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        side3.setOpaque(true);
        f.add(side3);

        //Layne's Sauce button
        JButton side4 = new JButton("Layne's Sauce");
        side4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == side4) {
                    InterfaceActions.addToOrder("Layne's Sauce", "513", OrderList, QueryList);
                    orderItems.append("Layne's Sauce\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        side4.setBounds(730, 390, 160, 30);
        side4.setBackground(new Color(198, 227, 243));
        side4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        side4.setOpaque(true);
        f.add(side4);

        //1 Finger button
        JButton side5 = new JButton("Chicken Finger");
        side5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == side5) {
                    InterfaceActions.addToOrder("Chicken Finger", "514", OrderList, QueryList);
                    orderItems.append("Chicken Finger\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        side5.setBounds(50, 390, 160, 30);
        side5.setBackground(new Color(198, 227, 243));
        side5.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        side5.setOpaque(true);
        f.add(side5);

        //Drinks Title
        JLabel drinksLabel = new JLabel("Drinks");
        drinksLabel.setBounds(50, 430, 250, 50);
        f.add(drinksLabel);

        //Fountain Drink button
        JButton drink1 = new JButton("Fountain Drink");
        drink1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == drink1) {
                    InterfaceActions.addToOrder("Fountain Drink", "518", OrderList, QueryList);
                    orderItems.append("Fountain Drink\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        drink1.setBounds(50, 480, 160, 30);
        drink1.setBackground(new Color(198, 227, 243));
        drink1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        drink1.setOpaque(true);
        f.add(drink1);


        //Bottle Drink button
        JButton drink2 = new JButton("Bottle Drink");
        drink2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == drink2) {
                    InterfaceActions.addToOrder("Bottle Drink", "519", OrderList, QueryList);
                    orderItems.append("Bottle Drink\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        drink2.setBounds(220, 480, 160, 30);
        drink2.setBackground(new Color(198, 227, 243));
        drink2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        drink2.setOpaque(true);
        f.add(drink2);

        //Gallon of Tea button
        JButton drink3 = new JButton("Gallon of Tea");
        drink3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == drink3) {
                    InterfaceActions.addToOrder("Gallon of Tea", "505", OrderList, QueryList);
                    orderItems.append("Gallon of Tea\n");
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                }
            }
        } );
        drink3.setBounds(390, 480, 160, 30);
        drink3.setBackground(new Color(198, 227, 243));
        drink3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        drink3.setOpaque(true);
        f.add(drink3);

        // go back button

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == back) {
                    f.dispose();
                    chooseInterface();
                }
            }
        } );
        back.setBounds(1200, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);


        //Total button
        JButton totalBtn = new JButton("Total");
        totalBtn.setBounds(950, 470, 130, 50);
        totalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == totalBtn) {
                    f.dispose();
                    //InterfaceActions.calculateTotal(total, OrderList);
                    totalPanel();
                }
            }
        } );
        totalBtn.setBackground(new Color(255, 178, 102));
        totalBtn.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        totalBtn.setOpaque(true);
        f.add(totalBtn);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);
        logoLabel.setBounds(920, 0, 450, 100);
        f.add(logoLabel);

        JButton startOver = new JButton("Delete Order");
        startOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == startOver) {
                    orderItems.setText("");
                    InterfaceActions.deleteCurrentOrder(OrderList);
                    order.add(orderItems);
                    order.revalidate();
                    order.repaint();
                    
                }
            }
        } );
        startOver.setBounds(1100, 470, 120, 30);
        startOver.setBackground(new Color(255,204,203));
        startOver.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        startOver.setOpaque(true);
        f.add(startOver);


        f.setVisible(true);
        f.show();

    }


    public void totalPanel() {
        int orderNum = InterfaceActions.orderNum();
        final int order_id = orderNum;
        System.out.println("Order Number: " + order_id);

        JFrame f = new JFrame("Total");
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);
        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);
        
        //Title
        JLabel title = new JLabel("ORDER:" + orderNum);
        title.setVerticalAlignment(JLabel.TOP);
        title.setFont(new Font("Verdana", Font.PLAIN, 25));
        title.setBounds(50, 25, 500, 120);
        f.add(title);

        //Product heading
        JLabel Product = new JLabel("Product");
        Product.setFont(new Font("Verdana", Font.BOLD, 20));
        Product.setBounds(60, 75, 150, 100);
        f.add(Product);

        //Description heading
        JLabel description = new JLabel("Description");
        description.setFont(new Font("Verdana", Font.BOLD, 20));
        description.setBounds(260, 75, 300, 100);
        f.add(description);

        //Price heading
        JLabel price = new JLabel("Price");
        price.setFont(new Font("Verdana", Font.BOLD, 20));
        price.setBounds(610, 75, 100, 100);
        f.add(price);


        // create a panel
        JPanel p = new JPanel();
        p.setBackground(new Color(240, 247, 255));
            

        //Display order list
        JTextArea productItems = new JTextArea();
        JTextArea descriptionItems = new JTextArea();
        JTextArea priceItems = new JTextArea();
        String orderItems = "";

        for(String item : OrderList) {
            String[] arr = new String[4];
            arr = item.split("@");
            orderItems += (arr[3] + " ");
            productItems.append(arr[0] + System.getProperty("line.separator"));
            descriptionItems.append(arr[1] + System.getProperty("line.separator"));
            priceItems.append(arr[2] + System.getProperty("line.separator"));
        }
        final String order = orderItems;

        productItems.setEditable(false);
        descriptionItems.setEditable(false);
        priceItems.setEditable(false);

        productItems.setBackground(new Color(240, 247, 255));
        descriptionItems.setBackground(new Color(240, 247, 255));
        priceItems.setBackground(new Color(240, 247, 255));

        productItems.setFont(new Font("Verdana", Font.PLAIN, 13));
        descriptionItems.setFont(new Font("Verdana", Font.PLAIN, 13));
        priceItems.setFont(new Font("Verdana", Font.PLAIN, 13));

        productItems.setPreferredSize(new Dimension(200, 600));
        productItems.setBounds(0, 0, 200, 600);
        
        descriptionItems.setPreferredSize(new Dimension(350, 600));
        descriptionItems.setBounds(210, 0, 350, 600);
        
        priceItems.setPreferredSize(new Dimension(100, 600));
        priceItems.setBounds(560, 0, 150, 600);
        
        //add text to panel
        p.add(productItems,BorderLayout.WEST);
        p.add(descriptionItems,BorderLayout.CENTER);
        p.add(priceItems, BorderLayout.EAST);

        p.setBounds(20, 150, 750, 325);
        f.add(p);

        JSeparator sep = new JSeparator(); 
        sep.setBounds(50, 145, 700, 5); 
        f.add(sep);  

        JSeparator sep1 = new JSeparator(); 
        sep1.setBounds(50, 490, 700, 5); 
        f.add(sep1);  

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == back) {
                    f.dispose();
                    serverPanel();
                }
            }
        } );
        back.setBounds(500, 10, 70, 30);
        f.add(back);

        //Total heading
        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        totalLabel.setBounds(500, 465, 100, 120);
        f.add(totalLabel);

        //Display total price
        Double total = 0.0;
        total = InterfaceActions.calculateTotal(total, OrderList);
        System.out.println("Total is: $"+total);
        String totalstr = "$"+ String.format("%.2f", total);
        System.out.println(totalstr);
        JLabel totalPrice = new JLabel(totalstr);
        totalPrice.setFont(new Font("Verdana", Font.BOLD, 20));
        totalPrice.setBounds(600, 465, 200, 120);
        f.add(totalPrice);

        JButton cash = new JButton("Cash");
        cash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == cash) {
                    InterfaceActions.createOrder(order_id, order, totalstr, "Cash", LocalDate.now());
                    OrderList.clear();
                    serverPanel();
                    f.dispose();
                }
            }
        } );
        cash.setBounds(790, 265, 120, 30);
        f.add(cash);

        JButton card = new JButton("Card");
        card.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == card) {
                    InterfaceActions.createOrder(order_id, order, totalstr, "Card", LocalDate.now());
                    OrderList.clear();
                    serverPanel();
                    f.dispose();
                }
            }
        } );
        card.setBounds(790, 300, 120, 30);
        f.add(card);

        JButton startOver = new JButton("Delete Order");
        startOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == startOver) {
                    f.dispose();
                    InterfaceActions.deleteCurrentOrder(OrderList);
                    serverPanel();
                }
            }
        } );
        startOver.setBounds(790, 340, 120, 30);
        f.add(startOver);

        f.setVisible(true);
        f.show();

    }

    /////////////////////////////////// ////////////////// MANAGER INTERFACE ////////////////////////// ////////////////////////////////////

    public void managerPanel() {
        f = new JFrame("Manager");  
        
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // rectangle on the left side
        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));


        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("NAVIGATION");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 162, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);


        //Create server Button option
        JButton menu = new JButton("Menu");
        menu.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == menu) {
                    f.dispose();
        			menuPanel();
        		}
        	}
        } );
        menu.setBounds(250, 130, 170, 60);
        menu.setBackground(new Color(198, 227, 243));
        menu.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        menu.setOpaque(true);
        f.add(menu);
        
        //Create inventory Button option
        JButton inventory = new JButton("Inventory");
        inventory.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == inventory) {
                    f.dispose();
        			inventoryPanel();
        		}
        	}
        } );
        inventory.setBounds(495, 130, 170, 60);
        inventory.setBackground(new Color(198, 227, 243));
        //inventory.setBorder(new RoundedBorder(10));
        inventory.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        inventory.setOpaque(true);
        f.add(inventory);
        
        //Create statistics button option
        JButton statistics = new JButton("Statistics");
        statistics.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == statistics) {
                    f.dispose();
        			statisticsPanel();
        		}
        	}
        } );
        statistics.setBounds(740, 130, 170, 60);
        statistics.setBackground(new Color(198, 227, 243));
        //inventory.setBorder(new RoundedBorder(10));
        statistics.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        statistics.setOpaque(true);
        f.add(statistics);

        // go back button

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    chooseInterface();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);


        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);


        f.setVisible(true);
        f.show();
    }

    public void statisticsPanel() {
        f = new JFrame("Manager");  
        
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // rectangle on the left side
        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("Statistics");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 162, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);


        //Create usage button option
        JButton usage = new JButton("Inventory Usage");
        usage.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == usage) {
                    f.dispose();
                    // link jframe here
                    inventoryUsagePanel();
        		}
        	}
        } );
        usage.setBounds(250, 130, 170, 60);
        usage.setBackground(new Color(198, 227, 243));
        usage.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        usage.setOpaque(true);
        f.add(usage);
        
        /*//Create restock report button option
        JButton restock = new JButton("Restock Report");
        restock.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == restock) {
                    f.dispose();
                    restockReportPanel();
        		}
        	}
        } );
        restock.setBounds(495, 130, 170, 60);
        restock.setBackground(new Color(198, 227, 243));
        //inventory.setBorder(new RoundedBorder(10));
        restock.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        restock.setOpaque(true);
        f.add(restock);*/
        
        //Create order popularity button option
        JButton opopularity = new JButton("Order Popularity");
        opopularity.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == opopularity) {
                    f.dispose();
                    orderPopularityPanel();
        		}
        	}
        } );
        opopularity.setBounds(495, 130, 170, 60);
        opopularity.setBackground(new Color(198, 227, 243));
        //inventory.setBorder(new RoundedBorder(10));
        opopularity.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        opopularity.setOpaque(true);
        f.add(opopularity);

        //Create trends button option
        JButton trends = new JButton("Order Trends");
        trends.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == trends) {
                    f.dispose();
                    orderTrendsPanel();
        		}
        	}
        } );
        trends.setBounds(250, 265, 170, 60);
        trends.setBackground(new Color(198, 227, 243));
        trends.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        trends.setOpaque(true);
        f.add(trends);
        
        //Create inventory popularity button option
        JButton ipopularity = new JButton("Inventory Popularity");
        ipopularity.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent  ae) {
        		if(ae.getSource() == ipopularity) {
                    f.dispose();
                    inventoryPopularityPanel();
        		}
        	}
        } );
        ipopularity.setBounds(495, 265, 170, 60);
        ipopularity.setBackground(new Color(198, 227, 243));
        //inventory.setBorder(new RoundedBorder(10));
        ipopularity.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
        ipopularity.setOpaque(true);
        f.add(ipopularity);

        // go back button

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    managerPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);


        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);


        f.setVisible(true);
        f.show();
    }

    public void viewMenuItemsPanel(){
        f = new JFrame("View Menu Items");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(backgroundColor);
        
        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        //Title
        /*JLabel labelM = new JLabel("View Menu Items");
        labelM.setBounds(50, 25, 200, 50);
        f.add(labelM);*/

        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    menuPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(backButtonColor);
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // rectangle on the left side

        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("VIEW MENU ITEMS");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        /*JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);*/

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 230, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        //LOOK AT ALL MENU ITEMS
        JLabel menuTableText = new JLabel("View entire menu table:");
        menuTableText.setBounds(270, 100, 300, 100);
        menuTableText.setFont(new Font("Arial", Font.PLAIN, 20));
        f.add(menuTableText);

        //button
        JButton menuTable = new JButton("Menu Table");
        menuTable.setBounds(570, 130, 130, 30);
        menuTable.setBackground(new Color(198, 227, 243));
        menuTable.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        menuTable.setOpaque(true);
        menuTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == menuTable) {

                    InterfaceActions.viewEntireMenu();
                    
                }
            }
        } );
        f.add(menuTable);

        // SEARCH UP ONE ITEM USING ITEM ID
        JLabel menuItemIdText = new JLabel("View menu item using item_id:");
        menuItemIdText.setBounds(270, 170, 300, 100);
        menuItemIdText.setFont(new Font("Arial", Font.PLAIN, 20));
        f.add(menuItemIdText);

        // text field to enter item_id

        JTextField itemIdTextField = new JTextField("Enter item_id");
        //itemIdFromTextField = itemIdTextField.getText();


        System.out.println("itemIdFromTextField: " + itemIdFromTextField);
        itemIdTextField.setBounds(370, 280, 200, 30);
        itemIdTextField.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        itemIdTextField.setOpaque(true);
        f.add(itemIdTextField);

        

        //button
        JButton menuItemId = new JButton("Search");
        menuItemId.setBounds(590, 280, 100, 30);
        menuItemId.setBackground(new Color(198, 227, 243));
        menuItemId.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        menuItemId.setOpaque(true);
        f.add(menuItemId);

        menuItemId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){

                if(ae.getSource() == menuItemId) {

                    itemIdFromTextField = itemIdTextField.getText();
                    InterfaceActions.viewMenuFromInput();
                    
                }
            }
        } );

        f.setVisible(true);
        f.show();
    }

    public void addMenuItemsPanel(){
        f = new JFrame("Add Menu Items");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));
        
        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        //Title
        /*JLabel labelM = new JLabel("Add Menu Items");
        labelM.setBounds(50, 25, 200, 50);
        f.add(labelM);*/

        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    menuPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // rectangle on the left side

        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("ADD MENU ITEMS");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 220, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();

        // prompt text
        JLabel instruction = new JLabel("Type in the following fields...");
        instruction.setBounds(300, 110, 400, 30);
        instruction.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        f.add(instruction);

        // text fields to enter each attribute of the menu item to be added (TF means text field)

        // text field to enter item_id
        JTextField addMenuItemsItemIdTF = new JTextField("Enter item_id");
        //System.out.println("itemIdFromTextField: " + itemIdFromTextField);
        addMenuItemsItemIdTF.setBounds(370, 170, 200, 30);
        addMenuItemsItemIdTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        addMenuItemsItemIdTF.setOpaque(true);
        f.add(addMenuItemsItemIdTF);

        JTextField addMenuItemsItemNameTF = new JTextField("Enter item_name");
        addMenuItemsItemNameTF.setBounds(370, 230, 200, 30);
        addMenuItemsItemNameTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        addMenuItemsItemNameTF.setOpaque(true);
        f.add(addMenuItemsItemNameTF);
        //public static String addMenuItemsItemName;


        //public static String addMenuItemsItemDesc;
        JTextField addMenuItemsItemDescTF = new JTextField("Enter item_description");
        addMenuItemsItemDescTF.setBounds(370, 290, 200, 30);
        addMenuItemsItemDescTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        addMenuItemsItemDescTF.setOpaque(true);
        f.add(addMenuItemsItemDescTF);

        //public static String addMenuItemsItemPrice;
        JTextField addMenuItemsItemPriceTF = new JTextField("Enter item_price");
        addMenuItemsItemPriceTF.setBounds(370, 350, 200, 30);
        addMenuItemsItemPriceTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        addMenuItemsItemPriceTF.setOpaque(true);
        f.add(addMenuItemsItemPriceTF);

        //public static String addMenuItemsItemIngre;
        JTextField addMenuItemsItemIngreTF = new JTextField("Enter item_ingredients");
        addMenuItemsItemIngreTF.setBounds(370, 410, 200, 30);
        addMenuItemsItemIngreTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        addMenuItemsItemIngreTF.setOpaque(true);
        f.add(addMenuItemsItemIngreTF);

        // add button to insert new row to the menu_items table
        JButton addMenuItem = new JButton("Add");
        addMenuItem.setBounds(700, 255, 120, 70);
        addMenuItem.setBackground(new Color(255, 178, 102));
        addMenuItem.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        addMenuItem.setOpaque(true);
        f.add(addMenuItem);

        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == addMenuItem) {

                    String temp = addMenuItemsItemIdTF.getText();
                    addMenuItemsItemId = Integer.parseInt(temp);
                    addMenuItemsItemName = addMenuItemsItemNameTF.getText();
                    addMenuItemsItemDesc = addMenuItemsItemDescTF.getText();
                    addMenuItemsItemPrice = addMenuItemsItemPriceTF.getText();
                    addMenuItemsItemIngre = addMenuItemsItemIngreTF.getText();

                    // call method
                    InterfaceActions.addMenuItemFromInput();
                }
            }
        } );     
    }

    public void updateMenuItemsPanel(){
        f = new JFrame("Update Menu Items");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));
        
        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        //Title
        /*JLabel labelM = new JLabel("Update Menu Items");
        labelM.setBounds(50, 25, 200, 50);
        f.add(labelM);*/

        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    menuPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // rectangle on the left side

        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("UPDATE MENU ITEMS");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 265, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();

        // prompt what item_id user would like to update
        JLabel instruction = new JLabel("Which item ID would you like to update?");
        instruction.setBounds(300, 110, 400, 30);
        instruction.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        f.add(instruction);

        // text field to enter item_id for the item to be updated
        JTextField updateMenuItemsItemIdTF = new JTextField("Enter item_id"); // TF stands for text field
        updateMenuItemsItemIdTF.setBounds(420, 170, 200, 30);
        updateMenuItemsItemIdTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        updateMenuItemsItemIdTF.setOpaque(true);
        f.add(updateMenuItemsItemIdTF);

        // prompt the attributes that the user would like to change for that item_id
        JLabel changeAttributes = new JLabel("Enter the values you would like to be updated:");
        changeAttributes.setBounds(300, 230, 400, 30);
        changeAttributes.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        f.add(changeAttributes);

        // text field to enter item_name for the item to be updated
        JTextField updateMenuItemsItemNameTF = new JTextField("Enter item_name"); // TF stands for text field
        updateMenuItemsItemNameTF.setBounds(300, 300, 140, 30);
        updateMenuItemsItemNameTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        updateMenuItemsItemNameTF.setOpaque(true);
        f.add(updateMenuItemsItemNameTF);

        // text field to enter item_description for the item to be updated

        // text field to enter item_name for the item to be updated
        JTextField updateMenuItemsItemDescTF = new JTextField("Enter item_description"); // TF stands for text field
        updateMenuItemsItemDescTF.setBounds(460, 300, 140, 30);
        updateMenuItemsItemDescTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        updateMenuItemsItemDescTF.setOpaque(true);
        f.add(updateMenuItemsItemDescTF);

        // text field to enter item_price for the item to be updated
        JTextField updateMenuItemsItemPriceTF = new JTextField("Enter item_price"); // TF stands for text field
        updateMenuItemsItemPriceTF.setBounds(620, 300, 140, 30);
        updateMenuItemsItemPriceTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        updateMenuItemsItemPriceTF.setOpaque(true);
        f.add(updateMenuItemsItemPriceTF);

        // text field to enter item_ingredients for the item to be updated
        JTextField updateMenuItemsItemIngreTF = new JTextField("Enter item_ingredients"); // TF stands for text field
        updateMenuItemsItemIngreTF.setBounds(780, 300, 140, 30);
        updateMenuItemsItemIngreTF.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        updateMenuItemsItemIngreTF.setOpaque(true);
        f.add(updateMenuItemsItemIngreTF);

        // button to update

        JButton updateMenuItem = new JButton("Update");
        updateMenuItem.setBounds(540, 385, 120, 70);
        updateMenuItem.setBackground(new Color(255, 178, 102));
        updateMenuItem.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        updateMenuItem.setOpaque(true);
        f.add(updateMenuItem);

        updateMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == updateMenuItem) {

                    String temp2 = updateMenuItemsItemIdTF.getText();
                    updateMenuItemsItemId = Integer.parseInt(temp2);

                    updateMenuItemsItemName = updateMenuItemsItemNameTF.getText();

                    updateMenuItemsItemDesc = updateMenuItemsItemDescTF.getText();

                    updateMenuItemsItemPrice = updateMenuItemsItemPriceTF.getText();

                    updateMenuItemsItemIngre = updateMenuItemsItemIngreTF.getText();
                    
                    // call method
                    InterfaceActions.updateMenuItemFromInput();
                }
            }
        } );
    }
    public void orderPopularityPanel() {
        f = new JFrame("Order Popularity");
        //set size and location of frame
        f.setSize(1000, 610);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(backgroundColor);

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        //Ask for dates
        JTextArea askDates = new JTextArea("Please enter start/end dates for the time windows you want to view, then press enter. ");
        askDates.setBounds(50, 100, 580, 50);
        askDates.setLineWrap(true);
        askDates.setWrapStyleWord(true);
        askDates.setEditable(false);
        askDates.setFont(new Font("Arial", Font.PLAIN, 15));
        askDates.setBackground(backgroundColor);
        f.add(askDates);

        // start date 1 text box
        JTextField startDate1 = new JTextField("Start Date 1 (YYYY-MM-DD)");
        startDate1.setBounds(50, 160, 150, 30);
        startDate1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        startDate1.setOpaque(true);
        f.add(startDate1);

        // end date 1 text box
        JTextField endDate1 = new JTextField("End Date 1 (YYYY-MM-DD)");
        endDate1.setBounds(250, 160, 150, 30);
        endDate1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        endDate1.setOpaque(true);
        f.add(endDate1);


        Color resultsbgColor = new Color(235, 235, 235);

        // scroll pane
        Integer listBoxHeight = 300;
        JPanel orderResults = new JPanel();
        orderResults.setBounds(50, 200, 800, listBoxHeight);
        orderResults.setBackground(new Color(240, 247, 255));
        orderResults.setOpaque(true);
        orderResults.setVisible(true);


        // orders 1 list
        JList<String> orders1 = new JList<String>();
        orders1.setBounds(0, 0, 800, 1000);
        orders1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        orders1.setOpaque(true);
        orders1.setBackground(resultsbgColor);
        
        f.add(orderResults);

        // enter button
        JButton enter = new JButton("Enter");
        enter.setBounds(860, 200, 70, 30);
        enter.setBackground(backButtonColor);
        enter.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        enter.setOpaque(true);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == enter) {
                    if (!(InterfaceActions.checkDate(startDate1.getText()) && InterfaceActions.checkDate(endDate1.getText()))) {
                        JOptionPane.showMessageDialog(null, "Please make sure all of the dates are formatted as YYYY-MM-DD.");
                    } else {
                        orderResults.removeAll();

                        String borderTitle = "All Orders Between " + startDate1.getText() + " and " + endDate1.getText() + " (Most Popular to Least Popular)";
                        Border title = BorderFactory.createTitledBorder(borderTitle);
                        orderResults.setBorder(title);
                        ((javax.swing.border.TitledBorder) orderResults.getBorder()).setTitleFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
                        //orderResults.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));

                        ArrayList<MenuItem> items1 = new ArrayList<MenuItem>();
                        
                        ArrayList<String> ordersList1 = InterfaceActions.computeItemsOrdered(startDate1.getText(), endDate1.getText(), items1);
                        
                        
                        orders1.setListData(new Vector<String>(ordersList1));

                        int size = ordersList1.size() - 1;
                        String[][] data = new String[size][3];
                        for(int i = 1; i < ordersList1.size(); i++) {
                            
                            String orderItem = ordersList1.get(i);
                            System.out.println(orderItem);
                            String itemId = orderItem.substring(0,orderItem.indexOf(':'));
                            String amount = orderItem.substring(orderItem.indexOf(' '), orderItem.indexOf('.'));
                            String itemName = orderItem.substring(orderItem.indexOf('(')+1,orderItem.length()-1);
                            data[i-1][0] = itemId;
                            data[i-1][1] = itemName;
                            data[i-1][2] = amount;
                            System.out.println(data[i-1][0] + " " + data[i-1][1] + " " + data[i-1][2]);
                            
                        }

                    
                        String[] header = {"Item ID", "Item Name", "Amount Ordered"};
                        JTable table = new JTable(data, header);
                        table.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
                       
                        JScrollPane orderScroll1 = new JScrollPane(table);
                        orderScroll1.setBackground(resultsbgColor);
                        orderScroll1.setPreferredSize(new Dimension(600, 250));
                        orderResults.add(orderScroll1);

                        orderResults.revalidate();
                        orderResults.repaint();
                    }
                }
            }
        } );
        f.add(enter);

        // default button
        JButton df = new JButton("Default");
        df.setBounds(860, 240, 70, 30);
        df.setBackground(backButtonColor);
        df.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        df.setOpaque(true);
        df.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == df) {
                    startDate1.setText("2022-02-01");
                    endDate1.setText("2022-03-31");
                }
            }
        } );
        f.add(df);
        

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    statisticsPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(backButtonColor);
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // jlabel title
        JLabel thislabel = new JLabel("Order Popularity");
        thislabel.setBounds(50, 30, 280, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(50, 72, 210, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();
    }
    public void orderTrendsPanel () {
        Integer listBoxHeight = 500;
        Integer listBoxWidth = 1000;

        f = new JFrame("Order Trends");
        //set size and location of frame
        f.setSize(200 + listBoxWidth, 270 + listBoxHeight);
        f.setLocation(150,0);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(backgroundColor);

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        //Ask for dates
        JTextArea askDates = new JTextArea("Please enter start/end dates for the time windows you want to compare, then press enter. ");
        askDates.setBounds(50, 100, 450, 50);
        askDates.setLineWrap(true);
        askDates.setWrapStyleWord(true);
        askDates.setEditable(false);
        askDates.setFont(new Font("Arial", Font.PLAIN, 20));
        askDates.setBackground(backgroundColor);
        f.add(askDates);

        // start date 1 text box
        JTextField startDate1 = new JTextField("Start Date 1 (YYYY-MM-DD)");
        startDate1.setBounds(50, 160, 150, 30);
        startDate1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        startDate1.setOpaque(true);
        f.add(startDate1);

        // end date 1 text box
        JTextField endDate1 = new JTextField("End Date 1 (YYYY-MM-DD)");
        endDate1.setBounds(250, 160, 150, 30);
        endDate1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        endDate1.setOpaque(true);
        f.add(endDate1);

        // start date 2 text box
        JTextField startDate2 = new JTextField("Start Date 2 (YYYY-MM-DD)");
        startDate2.setBounds(450, 160, 150, 30);
        startDate2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        startDate2.setOpaque(true);
        f.add(startDate2);

        // end date 2 text box
        JTextField endDate2 = new JTextField("End Date 2 (YYYY-MM-DD)");
        endDate2.setBounds(650, 160, 150, 30);
        endDate2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        endDate2.setOpaque(true);
        f.add(endDate2);

        //Color resultsbgColor = new Color(198, 227, 243);
        Color resultsbgColor = new Color(235, 235, 235);

        // scroll pane
        JPanel orderResults = new JPanel();
        orderResults.setBounds(50, 200, listBoxWidth, listBoxHeight);
        orderResults.setLayout(new GridLayout(1, 2));
        orderResults.setBackground(resultsbgColor);
        orderResults.setOpaque(true);
        orderResults.setVisible(true);

        JPanel orderLists = new JPanel();
        orderLists.setBounds(0, 0, listBoxWidth/2, listBoxHeight/2);
        orderResults.setLayout(new GridLayout(2, 1));
        orderResults.setBackground(resultsbgColor);
        orderResults.setOpaque(true);
        orderResults.setVisible(true);
        

        // orders 1 list
        JList<String> orders1 = new JList<String>();
        orders1.setBounds(0, 0, listBoxWidth / 2, listBoxHeight);
        orders1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        orders1.setOpaque(true);
        orders1.setBackground(resultsbgColor);
        JScrollPane orderScroll1 = new JScrollPane(orders1);
        orderScroll1.setBackground(resultsbgColor);

        // orders 2 list
        JList<String> orders2 = new JList<String>();
        orders2.setBounds(0, 0, listBoxWidth / 2, listBoxHeight);
        orders2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        orders2.setOpaque(true);
        orders2.setBackground(resultsbgColor);
        JScrollPane orderScroll2 = new JScrollPane(orders2);
        orderScroll2.setBackground(resultsbgColor);


        f.add(orderResults);

        
        // enter button
        JButton enter = new JButton("Enter");
        enter.setBounds(listBoxWidth + 60, 200, 70, 30);
        enter.setBackground(backButtonColor);
        enter.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        enter.setOpaque(true);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == enter) {
                    if (!(InterfaceActions.checkDate(startDate1.getText()) && InterfaceActions.checkDate(endDate1.getText()) && InterfaceActions.checkDate(startDate2.getText()) && InterfaceActions.checkDate(endDate2.getText()))) {
                        JOptionPane.showMessageDialog(null, "Please make sure all of the dates are formatted as YYYY-MM-DD.");
                    } else {
                        orderLists.removeAll();
                        orderResults.removeAll();

                        ArrayList<MenuItem> items1 = new ArrayList<MenuItem>();
                        ArrayList<MenuItem> items2 = new ArrayList<MenuItem>();
                        ArrayList<String> ordersList1 = InterfaceActions.findItemsOrdered(startDate1.getText(), endDate1.getText(), items1);
                        ArrayList<String> ordersList2 = InterfaceActions.findItemsOrdered(startDate2.getText(), endDate2.getText(), items2);
                        ArrayList<String> differencesList = InterfaceActions.findDifferences(items1, items2);
                        orders1.setListData(new Vector<String>(ordersList1));
                        orders2.setListData(new Vector<String>(ordersList2));
                        orderLists.add(orderScroll1);
                        orderLists.add(orderScroll2);
                        orderResults.add(orderLists);
                        
                        DefaultCategoryDataset diffData = InterfaceActions.createDiffDataset(items1, items2, startDate1.getText(), startDate2.getText(), endDate1.getText(), endDate2.getText());
                        JFreeChart diffChart = ChartFactory.createBarChart("Trends In Menu Items From Timeframe 1 to Timeframe 2", "Menu Items", "Difference in Revenue Percentage", diffData, PlotOrientation.VERTICAL, true, true, false);
                        ChartPanel diffPanel = new ChartPanel(diffChart);
                        diffPanel.setPreferredSize(new java.awt.Dimension(listBoxWidth - 20, listBoxHeight / 2));
                        JScrollPane chartScroll = new JScrollPane(diffPanel);
                        orderResults.add(chartScroll);

                        orderResults.revalidate();
                        orderResults.repaint();
                    }
                }
            }
        } );
        f.add(enter);

        // default button
        JButton df = new JButton("Default");
        df.setBounds(listBoxWidth + 60, 240, 70, 30);
        df.setBackground(backButtonColor);
        df.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        df.setOpaque(true);
        df.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == df) {
                    startDate1.setText("2022-02-01");
                    endDate1.setText("2022-03-02");
                    startDate2.setText("2022-03-03");
                    endDate2.setText("2022-03-31");
                }
            }
        } );
        f.add(df);
        

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    statisticsPanel();
                }
            }
        } );
        back.setBounds(listBoxWidth + 60, 165 + listBoxHeight, 70, 30);
        back.setBackground(backButtonColor);
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // jlabel title
        JLabel thislabel = new JLabel("Order Trends");
        thislabel.setBounds(50, 30, 280, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(50, 72, 210, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();
    }

    /*public void restockReportPanel() {
        f = new JFrame("Restock Report");
        f.setSize(1000, 700);
        f.setLocation(260, 150);

        //make sure the program exits when the frame closes
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    statisticsPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // navigation title
        JLabel navigation = new JLabel("Restock Report");
        navigation.setBounds(50, 30, 400, 50);
        navigation.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        //thislabel.setBounds(50, 30, 280, 50);
        //thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(50, 72, 370, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        // submit button
        JButton submit = new JButton("Submit");
        //submit.setBounds(460, 160, 70, 30);
        submit.setBackground(backButtonColor);
        submit.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        submit.setOpaque(true);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == submit) {
                    // do something
                }
            }
        } );
        f.add(submit);

        f.setVisible(true);
        f.show();
    }*/
    
    public void menuPanel() { // menu panel: from manager panel
    	f = new JFrame("Menu");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);
        
        //Title
        /*JLabel labelM = new JLabel("Menu Items");
        labelM.setBounds(50, 25, 200, 50);
        f.add(labelM);*/

        // View menu items button
        JButton viewMenuItems = new JButton("View Menu Items");
        viewMenuItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == viewMenuItems) {
                    f.dispose();
                    viewMenuItemsPanel();
                }
            }
        } );
        viewMenuItems.setBounds(270, 130, 200, 30);
        viewMenuItems.setBackground(new Color(198, 227, 243));
        viewMenuItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        viewMenuItems.setOpaque(true);

        f.add(viewMenuItems);

        // Add menu items button
        JButton addMenuItems = new JButton("Add Menu Items");
        addMenuItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == addMenuItems) {
                    f.dispose();
                    addMenuItemsPanel();
                }
            }
        } );
        addMenuItems.setBounds(490, 130, 200, 30);
        addMenuItems.setBackground(new Color(198, 227, 243));
        addMenuItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        addMenuItems.setOpaque(true);
        f.add(addMenuItems);

        // Update menu items button
        JButton updateMenuItems = new JButton("Update Menu Items");
        updateMenuItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == updateMenuItems) {
                    f.dispose();
                    updateMenuItemsPanel();
                }
            }
        } );
        updateMenuItems.setBounds(710, 130, 200, 30);
        updateMenuItems.setBackground(new Color(198, 227, 243));
        updateMenuItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        updateMenuItems.setOpaque(true);
        f.add(updateMenuItems);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    managerPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // rectangle on the left side

        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        // navigation title
        JLabel navigation = new JLabel("MENU");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 85, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();
    }

    public void inventoryPanel() { // inventory panel: from manager panel
    	f = new JFrame("Interface");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        JLabel thislabel = new JLabel("Manager");
        thislabel.setBounds(50, 30, 200, 50);
        thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
        f.add(thislabel);
        
        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));

        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        // rectangle on the left side

        JPanel p = new JPanel();
        p.setBackground(new Color(145, 145, 145));
        p.setBounds(9, 10, 220, 550);//New.
        p.setOpaque(true);
        p.show();
        f.add(p);

        //new jpanel to display all the inventory items
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setBounds(270, 190, 660, 300);
        inventoryPanel.setOpaque(true);
        //inventoryPanel.show();
        //inventoryPanel.setVisible(true);

        BoxLayout inventoryBoxLayout = new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS);
        inventoryPanel.setLayout(inventoryBoxLayout);

        //vertical box layout
        BoxLayout inventoryBoxLayout2 = new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS);
        inventoryPanel.setLayout(inventoryBoxLayout2);

        // intructions for user
        JLabel inventoryInstructions = new JLabel("Click on a row to select an inventory item, and then click on the Update or Remove buttons to perform the action.");
        inventoryInstructions.setBounds(260, 140, 1000, 50);
        inventoryInstructions.setFont(new Font("Arial", Font.PLAIN, 14));
        f.add(inventoryInstructions);

        //sends a request to the database to get all the inventory items
        Connection conn = InterfaceActions.conn;
        //String name = "";
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = "SELECT * FROM inventory;";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            //invItemPanel.setLayout(itemBoxLayout);
            while (result.next()) {
                JPanel invItemPanel = new JPanel();
                BoxLayout itemBoxLayout = new BoxLayout(invItemPanel, BoxLayout.X_AXIS);
                //name += result.getString("name")+"\n";
                String name = result.getString("inventory_item");
                String sku = result.getString("inventory_sku");
                String quantity = result.getString("inventory_quantity");
                String sold_by = result.getString("sold_by");
                String delivered_by = result.getString("delivered_by");
                String quantity_multiplied = result.getString("quantity_multiplied");
                String price = result.getString("inventory_price");
                String description = result.getString("inventory_description");
                String category = result.getString("category");
                String fill_level = result.getString("fill_level");
                JLabel nameLabel = new JLabel(name);
                JLabel skuLabel = new JLabel(sku);
                JLabel quantityLabel = new JLabel(quantity);
                JLabel soldByLabel = new JLabel(sold_by);
                JLabel deliveredByLabel = new JLabel(delivered_by);
                JLabel quantityMultipliedLabel = new JLabel(quantity_multiplied);
                JLabel priceLabel = new JLabel(price);
                JLabel descriptionLabel = new JLabel(description);
                JLabel categoryLabel = new JLabel(category);
                JLabel fillLevelLabel = new JLabel(fill_level);

                //set label sizes
                nameLabel.setPreferredSize(new Dimension(150, 20));
                skuLabel.setPreferredSize(new Dimension(50, 20));
                quantityLabel.setPreferredSize(new Dimension(50, 20));
                soldByLabel.setPreferredSize(new Dimension(100, 20));
                deliveredByLabel.setPreferredSize(new Dimension(150, 20));
                quantityMultipliedLabel.setPreferredSize(new Dimension(10, 20));
                priceLabel.setPreferredSize(new Dimension(100, 20));
                descriptionLabel.setPreferredSize(new Dimension(600, 20));
                categoryLabel.setPreferredSize(new Dimension(100, 20));
                fillLevelLabel.setPreferredSize(new Dimension(50, 20));

                invItemPanel.add(nameLabel);
                invItemPanel.add(skuLabel);
                invItemPanel.add(quantityLabel);
                invItemPanel.add(soldByLabel);
                invItemPanel.add(deliveredByLabel);
                invItemPanel.add(quantityMultipliedLabel);
                invItemPanel.add(priceLabel);
                invItemPanel.add(descriptionLabel);
                invItemPanel.add(categoryLabel);
                invItemPanel.add(fillLevelLabel);

                //invItemPanel.setVisible(true);
                inventoryPanel.add(invItemPanel);

                //makes the items selectable
                invItemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        rowSelected = true;
                        if (updatingInventory == false) {
                            invItemList.get(invItemIndex).setBackground(null);
                            inventoryPanel.repaint();
                            invItemPanel.setBackground(new Color(161, 221, 247));
                            //store the index of the selected item in the arraylist
                            invItemIndex = invItemList.indexOf(invItemPanel);
                        }
                    }
                });

                //adds the items to the list of inventory items
                invItemList.add(invItemPanel);
            }

            JScrollPane inventoryScrollPane = new JScrollPane(inventoryPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            inventoryScrollPane.setBounds(270, 190, 660, 300);
            inventoryScrollPane.setVisible(true);
            //f.setVisible(true);
            f.add(inventoryScrollPane);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        //f.add(inventoryPanel);
        
        //Title
        /*JLabel labelM = new JLabel("Menu Items");
        labelM.setBounds(50, 25, 200, 50);
        f.add(labelM);*/

        /*// View inventory items button
        JButton viewInventoryItems = new JButton("View Inventory Items");
        viewInventoryItems.setBounds(270, 130, 200, 30);
        viewInventoryItems.setBackground(new Color(198, 227, 243));
        viewInventoryItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        viewInventoryItems.setOpaque(true);
        f.add(viewInventoryItems);*/

        // Update inventory items button
        JButton updateInventoryItems = new JButton("Update");
        updateInventoryItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if (rowSelected == true) {
                    updatingInventory = !updatingInventory;
                    //new panel for updating inventory items
                    JPanel updateInventoryPanel = new JPanel();
                    if(updatingInventory == true) {
                        //System.out.println(invItemIndex);
                        updateInventoryPanel = invItemList.get(invItemIndex);
    
                        //changes text of update button until done with update
                        updateInventoryItems.setText("Finalize Changes");
    
                        //store the values of all the jlabels in strings
                        Component[] components = updateInventoryPanel.getComponents();
                        String invName = ((JLabel) components[0]).getText();
                        String invSku = ((JLabel) components[1]).getText();
                        String invQuantity = ((JLabel) components[2]).getText();
                        String invSold_by = ((JLabel) components[3]).getText();
                        String invDelivered_by = ((JLabel) components[4]).getText();
                        String invQuantity_multiplied = ((JLabel) components[5]).getText();
                        String invPrice = ((JLabel) components[6]).getText();
                        String invDescription = ((JLabel) components[7]).getText();
                        String invCategory = ((JLabel) components[8]).getText();
                        String invFill_level = ((JLabel) components[9]).getText();
                        /*//prints out all the strings
                        System.out.println(invName);
                        System.out.println(invSku);
                        System.out.println(invQuantity);
                        System.out.println(invSold_by);
                        System.out.println(invDelivered_by);
                        System.out.println(invQuantity_multiplied);
                        System.out.println(invPrice);
                        System.out.println(invDescription);
                        System.out.println(invCategory);*/
    
                        //remove the labels from the row and replace with text fields
                        updateInventoryPanel.remove((JLabel) components[0]);
                        updateInventoryPanel.remove((JLabel) components[1]);
                        updateInventoryPanel.remove((JLabel) components[2]);
                        updateInventoryPanel.remove((JLabel) components[3]);
                        updateInventoryPanel.remove((JLabel) components[4]);
                        updateInventoryPanel.remove((JLabel) components[5]);
                        updateInventoryPanel.remove((JLabel) components[6]);
                        updateInventoryPanel.remove((JLabel) components[7]);
                        updateInventoryPanel.remove((JLabel) components[8]);
                        updateInventoryPanel.remove((JLabel) components[9]);

                        JTextField invNameField = new JTextField(invName);
                        JTextField invSkuField = new JTextField(invSku);
                        JTextField invQuantityField = new JTextField(invQuantity);
                        JTextField invSold_byField = new JTextField(invSold_by);
                        JTextField invDelivered_byField = new JTextField(invDelivered_by);
                        JTextField invQuantity_multipliedField = new JTextField(invQuantity_multiplied);
                        JTextField invPriceField = new JTextField(invPrice);
                        JTextField invDescriptionField = new JTextField(invDescription);
                        JTextField invCategoryField = new JTextField(invCategory);
                        JTextField invFill_levelField = new JTextField(invFill_level);

                        invNameField.setPreferredSize(new Dimension(150, 20));
                        invSkuField.setPreferredSize(new Dimension(50, 20));
                        invQuantityField.setPreferredSize(new Dimension(50, 20));
                        invSold_byField.setPreferredSize(new Dimension(100, 20));
                        invDelivered_byField.setPreferredSize(new Dimension(150, 20));
                        invQuantity_multipliedField.setPreferredSize(new Dimension(10, 20));
                        invPriceField.setPreferredSize(new Dimension(100, 20));
                        invDescriptionField.setPreferredSize(new Dimension(600, 20));
                        invCategoryField.setPreferredSize(new Dimension(100, 20));
                        invFill_levelField.setPreferredSize(new Dimension(50, 20));

                        updateInventoryPanel.add(invNameField);
                        updateInventoryPanel.add(invSkuField);
                        updateInventoryPanel.add(invQuantityField);
                        updateInventoryPanel.add(invSold_byField);
                        updateInventoryPanel.add(invDelivered_byField);
                        updateInventoryPanel.add(invQuantity_multipliedField);
                        updateInventoryPanel.add(invPriceField);
                        updateInventoryPanel.add(invDescriptionField);
                        updateInventoryPanel.add(invCategoryField);
                        updateInventoryPanel.add(invFill_levelField);
    
                        inventoryPanel.revalidate();
                        inventoryPanel.repaint();
                    } else {
                        //changes the update button back to say Update Inventory Items
                        updateInventoryItems.setText("Update");
    
                        updateInventoryPanel = invItemList.get(invItemIndex);
    
                        //store the values of all the jtextfields in strings
                        Component[] components = updateInventoryPanel.getComponents();
                        String invName = ((JTextField) components[0]).getText();
                        String invSku = ((JTextField) components[1]).getText();
                        String invQuantity = ((JTextField) components[2]).getText();
                        String invSold_by = ((JTextField) components[3]).getText();
                        String invDelivered_by = ((JTextField) components[4]).getText();
                        String invQuantity_multiplied = ((JTextField) components[5]).getText();
                        String invPrice = ((JTextField) components[6]).getText();
                        String invDescription = ((JTextField) components[7]).getText();
                        String invCategory = ((JTextField) components[8]).getText();
                        String invFill_level = ((JTextField) components[9]).getText();
    
                        try {
                            //create a statement object
                            Statement stmt = InterfaceActions.conn.createStatement();
                            String sqlSring = "UPDATE inventory SET inventory_item = '" + invName + "', inventory_sku = '" + invSku 
                                + "', inventory_quantity = '" + invQuantity + "', sold_by = '" + invSold_by + "', delivered_by = '" 
                                + invDelivered_by + "', quantity_multiplied = '" + invQuantity_multiplied + "', inventory_price = '" 
                                + invPrice + "', inventory_description = '" + invDescription + "', category = '" + invCategory + "'', fill_level = " + invFill_level + " WHERE inventory_sku = '" 
                                + invSku + "'";
                            stmt.executeUpdate(sqlSring);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        }
    
                        //remove the jtextfields from the row and replace with jlabels
                        updateInventoryPanel.remove((JTextField) components[0]);
                        updateInventoryPanel.remove((JTextField) components[1]);
                        updateInventoryPanel.remove((JTextField) components[2]);
                        updateInventoryPanel.remove((JTextField) components[3]);
                        updateInventoryPanel.remove((JTextField) components[4]);
                        updateInventoryPanel.remove((JTextField) components[5]);
                        updateInventoryPanel.remove((JTextField) components[6]);
                        updateInventoryPanel.remove((JTextField) components[7]);
                        updateInventoryPanel.remove((JTextField) components[8]);
                        updateInventoryPanel.remove((JTextField) components[9]);

                        JLabel invNameField = new JLabel(invName);
                        JLabel invSkuField = new JLabel(invSku);
                        JLabel invQuantityField = new JLabel(invQuantity);
                        JLabel invSold_byField = new JLabel(invSold_by);
                        JLabel invDelivered_byField = new JLabel(invDelivered_by);
                        JLabel invQuantity_multipliedField = new JLabel(invQuantity_multiplied);
                        JLabel invPriceField = new JLabel(invPrice);
                        JLabel invDescriptionField = new JLabel(invDescription);
                        JLabel invCategoryField = new JLabel(invCategory);
                        JLabel invFill_levelField = new JLabel(invFill_level);

                        invNameField.setPreferredSize(new Dimension(150, 20));
                        invSkuField.setPreferredSize(new Dimension(50, 20));
                        invQuantityField.setPreferredSize(new Dimension(50, 20));
                        invSold_byField.setPreferredSize(new Dimension(100, 20));
                        invDelivered_byField.setPreferredSize(new Dimension(150, 20));
                        invQuantity_multipliedField.setPreferredSize(new Dimension(10, 20));
                        invPriceField.setPreferredSize(new Dimension(100, 20));
                        invDescriptionField.setPreferredSize(new Dimension(600, 20));
                        invCategoryField.setPreferredSize(new Dimension(100, 20));
                        invFill_levelField.setPreferredSize(new Dimension(50, 20));

                        updateInventoryPanel.add(invNameField);
                        updateInventoryPanel.add(invSkuField);
                        updateInventoryPanel.add(invQuantityField);
                        updateInventoryPanel.add(invSold_byField);
                        updateInventoryPanel.add(invDelivered_byField);
                        updateInventoryPanel.add(invQuantity_multipliedField);
                        updateInventoryPanel.add(invPriceField);
                        updateInventoryPanel.add(invDescriptionField);
                        updateInventoryPanel.add(invCategoryField);
                        updateInventoryPanel.add(invFill_levelField);
    
                        inventoryPanel.revalidate();
                        inventoryPanel.repaint();
                    }
                }
            }
        } );
        updateInventoryItems.setBounds(425, 110, 150, 30); // setBounds(int x, int y, int width, int height)
        updateInventoryItems.setBackground(new Color(198, 227, 243));
        updateInventoryItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        updateInventoryItems.setOpaque(true);
        f.add(updateInventoryItems);

        //add inventory items button
        JButton addInventoryItems = new JButton("Add");
        addInventoryItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //creates jtextfields for the user to enter the values of the inventory items they want to add
                JTextField invNameField = new JTextField();
                JTextField invSkuField = new JTextField();
                JTextField invQuantityField = new JTextField();
                JTextField invSold_byField = new JTextField();
                JTextField invDelivered_byField = new JTextField();
                JTextField invQuantity_multipliedField = new JTextField();
                JTextField invPriceField = new JTextField();
                JTextField invDescriptionField = new JTextField();
                JTextField invCategoryField = new JTextField();
                JTextField invFill_levelField = new JTextField();

                invNameField.setPreferredSize(new Dimension(150, 20));
                invSkuField.setPreferredSize(new Dimension(50, 20));
                invQuantityField.setPreferredSize(new Dimension(50, 20));
                invSold_byField.setPreferredSize(new Dimension(100, 20));
                invDelivered_byField.setPreferredSize(new Dimension(150, 20));
                invQuantity_multipliedField.setPreferredSize(new Dimension(10, 20));
                invPriceField.setPreferredSize(new Dimension(100, 20));
                invDescriptionField.setPreferredSize(new Dimension(600, 20));
                invCategoryField.setPreferredSize(new Dimension(100, 20));
                invFill_levelField.setPreferredSize(new Dimension(50, 20));
                
                //array of objects
                Object[] components = {"Name: ", invNameField, "SKU: ", invSkuField, "Quantity: ", invQuantityField, "Sold By: ", invSold_byField, "Delivered By: ", invDelivered_byField, "Quantity Multiplied: ", invQuantity_multipliedField, "Price: ", invPriceField, "Description: ", invDescriptionField, "Category: ", invCategoryField, "Fill Level: ", invFill_levelField};

                int option = JOptionPane.showConfirmDialog(null, components, "Add Inventory Items", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        //create a statement object
                        Statement stmt = InterfaceActions.conn.createStatement();
                        String sqlSring = "INSERT INTO inventory (inventory_item, inventory_sku, inventory_quantity, sold_by, delivered_by, quantity_multiplied, inventory_price, inventory_description, category, fill_level) VALUES ('" + invNameField.getText() + "', '" + invSkuField.getText() + "', '" + invQuantityField.getText() + "', '" + invSold_byField.getText() + "', '" + invDelivered_byField.getText() + "', '" + invQuantity_multipliedField.getText() + "', '" + invPriceField.getText() + "', '" + invDescriptionField.getText() + "', '" + invCategoryField.getText() + "', '" + invFill_levelField.getText() + "');";
                        stmt.executeUpdate(sqlSring);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                    }

                    //create a new inventory panel
                    JPanel inventoryAddPanel = new JPanel();
                    BoxLayout itemBoxLayout = new BoxLayout(inventoryAddPanel, BoxLayout.X_AXIS);
                    //creates labels for each text field
                    JLabel invName = new JLabel(invNameField.getText());
                    JLabel invSku = new JLabel(invSkuField.getText());
                    JLabel invQuantity = new JLabel(invQuantityField.getText());
                    JLabel invSold_by = new JLabel(invSold_byField.getText());
                    JLabel invDelivered_by = new JLabel(invDelivered_byField.getText());
                    JLabel invQuantity_multiplied = new JLabel(invQuantity_multipliedField.getText());
                    JLabel invPrice = new JLabel(invPriceField.getText());
                    JLabel invDescription = new JLabel(invDescriptionField.getText());
                    JLabel invCategory = new JLabel(invCategoryField.getText());
                    JLabel invFill_level = new JLabel(invFill_levelField.getText());

                    invName.setPreferredSize(new Dimension(150, 20));
                    invSku.setPreferredSize(new Dimension(50, 20));
                    invQuantity.setPreferredSize(new Dimension(50, 20));
                    invSold_by.setPreferredSize(new Dimension(100, 20));
                    invDelivered_by.setPreferredSize(new Dimension(150, 20));
                    invQuantity_multiplied.setPreferredSize(new Dimension(10, 20));
                    invPrice.setPreferredSize(new Dimension(100, 20));
                    invDescription.setPreferredSize(new Dimension(600, 20));
                    invCategory.setPreferredSize(new Dimension(100, 20));
                    invFill_level.setPreferredSize(new Dimension(50, 20));

                    inventoryAddPanel.add(invName);
                    inventoryAddPanel.add(invSku);
                    inventoryAddPanel.add(invQuantity);
                    inventoryAddPanel.add(invSold_by);
                    inventoryAddPanel.add(invDelivered_by);
                    inventoryAddPanel.add(invQuantity_multiplied);
                    inventoryAddPanel.add(invPrice);
                    inventoryAddPanel.add(invDescription);
                    inventoryAddPanel.add(invCategory);
                    inventoryAddPanel.add(invFill_level);

                    inventoryPanel.revalidate();
                    inventoryPanel.repaint();

                    inventoryPanel.add(inventoryAddPanel);
                }
            }
        });
        addInventoryItems.setBounds(250, 110, 150, 30);
        addInventoryItems.setBackground(new Color(198, 227, 243));
        addInventoryItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        addInventoryItems.setOpaque(true);
        f.add(addInventoryItems);

        //remove inventory items button
        JButton removeInventoryItems = new JButton("Remove");
        removeInventoryItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (rowSelected == true) { 
                    Component components[] = invItemList.get(invItemIndex).getComponents();
                    String rsku = ((JLabel) components[1]).getText();

                    try {
                        Statement stmt = InterfaceActions.conn.createStatement();
                        String sqlSring = "DELETE FROM inventory WHERE inventory_sku = '" + rsku + "'";
                        stmt.executeUpdate(sqlSring);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                    }

                    inventoryPanel.remove(invItemList.get(invItemIndex));
                    inventoryPanel.revalidate();
                    inventoryPanel.repaint();
                }
            }
        });
        removeInventoryItems.setBounds(600, 110, 150, 30);
        removeInventoryItems.setBackground(new Color(198, 227, 243));
        removeInventoryItems.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        removeInventoryItems.setOpaque(true);
        f.add(removeInventoryItems);

        //submit button for restock report
        JButton submitRestockReport = new JButton("Submit/Report");
        submitRestockReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                restockReport();
            }
        });
        submitRestockReport.setBounds(775, 110, 150, 30);
        submitRestockReport.setBackground(new Color(198, 227, 243));
        submitRestockReport.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        submitRestockReport.setOpaque(true);
        f.add(submitRestockReport);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    managerPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // navigation title
        JLabel navigation = new JLabel("INVENTORY");
        navigation.setBounds(270, 10, 250, 100);
        navigation.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(262, 72, 162, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();
    }

    public void restockReport() {
        Connection conn2 = InterfaceActions.conn;
        JPanel restockReportPanel = new JPanel();
        restockReportPanel.setBounds(0, 0, 900, 600);
        BoxLayout restockReportBoxLayout = new BoxLayout(restockReportPanel, BoxLayout.Y_AXIS);
        restockReportPanel.setLayout(restockReportBoxLayout);

        // fill from report button
        JButton fillButton = new JButton("Refill Inventory");
        fillButton.setBounds(200, 10, 70, 30);
        fillButton.setBackground(new Color(198, 227, 243));
        fillButton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        fillButton.setOpaque(true);
        restockReportPanel.add(fillButton);

        //System.out.println("a");
        Vector<String> itemsNeeded = new Vector<String>(); // name of each item needed to refill inventory
        Vector<Float> amountsNeeded = new Vector<Float>(); // amount of each item necessary to refill
        Vector<Float> amountHad = new Vector<Float>(); // amount of each item currently in inventory
        try {
            String rsqlString = "SELECT * FROM inventory WHERE inventory_quantity < fill_level";
            Statement rstmt = conn2.createStatement();
            ResultSet rrs = rstmt.executeQuery(rsqlString);
            //System.out.println("b");
            int counter = 0;
            while (rrs.next()) {
                counter ++;
                JPanel restockReportItemPanel = new JPanel();
                String rinvName = rrs.getString("inventory_item");
                String rinvSku = rrs.getString("inventory_sku");
                String rinvQuantity = rrs.getString("inventory_quantity");
                String rinvFill_level = rrs.getString("fill_level");
                
                //difference between the fill level and the inventory quantity
                float difference = Float.valueOf(rinvFill_level) - Float.valueOf(rinvQuantity);
                String rdifference = Float.toString(difference);
                //String rdifference = Integer.toString(Integer.parseInt(rinvFill_level) - Integer.parseInt(rinvQuantity));

                itemsNeeded.add(rinvName);
                amountsNeeded.add(difference);
                amountHad.add(Float.valueOf(rinvQuantity));

                JLabel rinvNameLabel = new JLabel(rinvName);
                JLabel rinvSkuLabel = new JLabel(rinvSku);
                JLabel rinvQuantityLabel = new JLabel("current amount: " + rinvQuantity);
                JLabel rinvFill_levelLabel = new JLabel("fill level: " + rinvFill_level);
                JLabel rdifferenceLabel = new JLabel("needed: " + rdifference);

                rinvNameLabel.setPreferredSize(new Dimension(150, 20));
                rinvSkuLabel.setPreferredSize(new Dimension(50, 20));
                rinvQuantityLabel.setPreferredSize(new Dimension(200, 20));
                rinvFill_levelLabel.setPreferredSize(new Dimension(100, 20));
                rdifferenceLabel.setPreferredSize(new Dimension(200, 20));

                restockReportItemPanel.add(rinvNameLabel);
                restockReportItemPanel.add(rinvSkuLabel);
                restockReportItemPanel.add(rinvQuantityLabel);
                restockReportItemPanel.add(rinvFill_levelLabel);
                restockReportItemPanel.add(rdifferenceLabel);

                restockReportPanel.add(restockReportItemPanel);
            }

            if (counter == 0) {
                fillButton.setVisible(false);
                JLabel noResults = new JLabel("The inventory is full. Nothing to report!");
                noResults.setBounds(200, 100, 100, 100);
                restockReportPanel.add(noResults);
            }

            
            fillButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae){
                    //String cmd = ae.getActionCommand();
                    if(ae.getSource() == fillButton) {
                        // round up the amounts needed and add them to the inventory

                        try {
                            for (int i = 0; i < itemsNeeded.size(); i++) {
                                Statement stmt = conn2.createStatement();
                                Float amountToAdd = (float) Math.ceil(amountsNeeded.get(i));
                                Float newAmount = amountToAdd + amountHad.get(i);
                                String update = "UPDATE inventory SET inventory_quantity = " + newAmount + " WHERE inventory_item LIKE '%" + itemsNeeded.get(i) + "%';";
                                stmt.executeUpdate(update);
                            }

                            f.dispose();
                            inventoryPanel();
                        } catch (Exception e) {
                            System.out.println("Error refilling inventory:");
                            e.printStackTrace();
                            System.exit(0);
                        }
                    }
                }
            } );

            //System.out.println("c");
            JScrollPane rinventoryScrollPane = new JScrollPane(restockReportPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            rinventoryScrollPane.setBounds(270, 190, 660, 300);
            rinventoryScrollPane.setVisible(true);
            //f.setVisible(true);
            //f.add(rinventoryScrollPane);

            //System.out.println("d");
            JFrame restockReportFrame = new JFrame("Restock Report");
            //restockReportFrame.add(restockReportPanel);
            restockReportFrame.add(rinventoryScrollPane);
            restockReportFrame.setSize(900, 600);
            restockReportFrame.setVisible(true);
            //System.out.println("e");
        } catch (Exception e) {
            e.printStackTrace();
            //e.getMessage();
        }
    }

    public void inventoryUsagePanel() {
        f = new JFrame("Inventory Usage");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));
        
        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    statisticsPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // navigation title
        JLabel navigation = new JLabel("Inventory Usage Charts");
        navigation.setBounds(50, 30, 400, 50);
        navigation.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        //thislabel.setBounds(50, 30, 280, 50);
        //thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(50, 72, 370, 2);//New.
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();


        // time variables input
        //Ask for dates
        JTextArea askDates = new JTextArea("Please enter start & end dates for the time windows you want to compare, then press enter.");
        askDates.setBounds(50, 100, 650, 50);
        askDates.setLineWrap(true);
        askDates.setWrapStyleWord(true);
        askDates.setEditable(false);
        askDates.setFont(new Font("Arial", Font.PLAIN, 15));
        askDates.setBackground(backgroundColor);
        f.add(askDates);

        // start date 1 text box
        JTextField startDate = new JTextField("Start Date (YYYY-MM-DD)");
        startDate.setBounds(50, 160, 150, 30);
        startDate.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        startDate.setOpaque(true);
        f.add(startDate);

        // end date 1 text box
        JTextField endDate = new JTextField("End Date (YYYY-MM-DD)");
        endDate.setBounds(250, 160, 150, 30);
        endDate.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        endDate.setOpaque(true);
        f.add(endDate);

        // panels

        JPanel chartResult = new JPanel();
        chartResult.setBounds(50, 200, 800, 345);
        chartResult.setLayout(new GridLayout(1, 2));
        chartResult.setBackground(new Color(255,255,255));
        chartResult.setOpaque(true);
        chartResult.setVisible(true);

        f.add(chartResult);

        // enter button
        JButton enter = new JButton("Enter");
        enter.setBounds(460, 160, 70, 30);
        enter.setBackground(backButtonColor);
        enter.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        enter.setOpaque(true);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == enter) {

                    if (!(InterfaceActions.checkDate(startDate.getText()) && InterfaceActions.checkDate(endDate.getText()) )) {
                        JOptionPane.showMessageDialog(null, "Please make sure all of the dates are formatted as YYYY-MM-DD.");
                    } else {

                        ArrayList<String> ingr = new ArrayList<String>();
                        ArrayList<Float> amount = new ArrayList<Float>();  

                        chartResult.removeAll();

                        itemsAndTheirAmount = InterfaceActions.itemsAmountMap(startDate.getText(), endDate.getText());
                        itemsAndTheirIngredients = InterfaceActions.itemsIngredientsMap(itemsAndTheirAmount);
                        ingredientsAndTheirAmount = InterfaceActions.ingredientsAmountMap2(itemsAndTheirAmount);
                        //InterfaceActions.ingredientsAmountMap2(itemsAndTheirAmount);

                        ingr = InterfaceActions.ingredientsList(ingredientsAndTheirAmount);
                        amount = InterfaceActions.amountList(ingredientsAndTheirAmount);

                        // creating the dataset.

                        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  

                        for(int i = 0; i < ingr.size(); i++){ // populating the dataset
                            dataset.addValue(amount.get(i), ingr.get(i), "");
                        }

                        JFreeChart barChart = ChartFactory.createBarChart("Inventory Usage", "Inventory Items", "Amount Used", dataset, PlotOrientation.VERTICAL, true, true, false);

                        ChartPanel barPanel = new ChartPanel(barChart);  
                        //setContentPane(barPanel); 

                        chartResult.add(barPanel);

                        chartResult.revalidate();
                        chartResult.repaint();
                    }


                }
            }
        } );
        f.add(enter);

        // default button
        JButton df = new JButton("Default");
        df.setBounds(560, 160, 100, 30);
        df.setBackground(backButtonColor);
        df.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        df.setOpaque(true);
        df.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == df) {
                    startDate.setText("2022-02-01");
                    endDate.setText("2022-03-02");
                }
            }
        } );
        f.add(df);

    }

    public void inventoryPopularityPanel() {
        f = new JFrame("Inventory Popularity");  
        //set size and location of frame
        f.setSize(1000, 600);
        f.setLocation(260,150);
        f.setLayout(null);

        //make sure it quits when x is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background color of jframe f
        f.getContentPane().setBackground(new Color(240, 247, 255));
        
        //set look and feel
        f.setDefaultLookAndFeelDecorated(true);

        // go back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                //String cmd = ae.getActionCommand();
                if(ae.getSource() == back) {
                    f.dispose();
                    statisticsPanel();
                }
            }
        } );
        back.setBounds(900, 515, 70, 30);
        back.setBackground(new Color(198, 227, 243));
        back.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        back.setOpaque(true);
        f.add(back);

        // navigation title
        JLabel navigation = new JLabel("Inventory Popularity");
        navigation.setBounds(50, 30, 400, 50);
        navigation.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        //thislabel.setBounds(50, 30, 280, 50);
        //thislabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));

        f.add(navigation);

        // underline on jlabel title
        JPanel t = new JPanel();
        t.setBackground(new Color(255, 153, 51));
        t.setBounds(45, 72, 320, 2);
        t.setOpaque(true);
        t.show();
        f.add(t);

        // layne's logo
        ImageIcon laynesLogo = new ImageIcon("layneslogo.png");
        JLabel logoLabel = new JLabel(laynesLogo);

        logoLabel.setBounds(620, 0, 450, 100);
        f.add(logoLabel);

        f.setVisible(true);
        f.show();


        // time variables input
        //Ask for dates
        JTextArea askDates = new JTextArea("Please enter start & end dates for the time frame you want to view, then press enter.");
        askDates.setBounds(50, 100, 650, 50);
        askDates.setLineWrap(true);
        askDates.setWrapStyleWord(true);
        askDates.setEditable(false);
        askDates.setFont(new Font("Arial", Font.PLAIN, 15));
        askDates.setBackground(backgroundColor);
        f.add(askDates);

        // start date 1 text box
        JTextField startDate = new JTextField("Start Date (YYYY-MM-DD)");
        startDate.setBounds(50, 160, 150, 30);
        startDate.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        startDate.setOpaque(true);
        f.add(startDate);

        // end date 1 text box
        JTextField endDate = new JTextField("End Date (YYYY-MM-DD)");
        endDate.setBounds(250, 160, 150, 30);
        endDate.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 12));
        endDate.setOpaque(true);
        f.add(endDate);

        // panels

        JPanel popularityResult = new JPanel();
        popularityResult.setBounds(50, 200, 800, 300);
        popularityResult.setBackground(new Color(255,255,255));
        popularityResult.setOpaque(true);
        popularityResult.setVisible(true);

        f.add(popularityResult);
        

        // orders 1 list
        JList<String> orders1 = new JList<String>();
        orders1.setBounds(0, 0, 800, 1000);
        orders1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 15));
        orders1.setOpaque(true);
        orders1.setBackground(new Color(255,255,255));


        f.add(popularityResult); 

        // enter button
        JButton enter = new JButton("Enter");
        enter.setBounds(460, 160, 70, 30);
        enter.setBackground(backButtonColor);
        enter.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        enter.setOpaque(true);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == enter) {
                    // do something
                    if (!(InterfaceActions.checkDate(startDate.getText()) && InterfaceActions.checkDate(endDate.getText()) )) {
                        JOptionPane.showMessageDialog(null, "Please make sure all of the dates are formatted as YYYY-MM-DD.");
                    } else {

                        popularityResult.removeAll();

                        String borderTitle = "All Inventory Amounts Used Between " + startDate.getText() + " and " + endDate.getText() + " (Most Popular to Least Popular)";
                        Border title = BorderFactory.createTitledBorder(borderTitle);
                        popularityResult.setBorder(title);
                        ((javax.swing.border.TitledBorder) popularityResult.getBorder()).setTitleFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
                        
                        
                        ArrayList<String> ingrSKU = new ArrayList<String>();
                        
                        ArrayList<Float> amount = new ArrayList<Float>();  
                        Map<String, Float> thisMap = new HashMap<String, Float>();

                        ArrayList<String> displayList = new ArrayList<String>();
                        
                        itemsAndTheirAmount = InterfaceActions.itemsAmountMap(startDate.getText(), endDate.getText());
                        itemsAndTheirIngredients = InterfaceActions.itemsIngredientsMap(itemsAndTheirAmount);
                        ingredientsAndTheirAmount = InterfaceActions.ingredientsAmountMap2(itemsAndTheirAmount);
                        thisMap = InterfaceActions.sortMap(ingredientsAndTheirAmount); 

                        ingrSKU = InterfaceActions.ingredientsList(thisMap);
                        amount = InterfaceActions.amountList(thisMap);


                        String[] ingrName = new String[ingrSKU.size()];
                        InterfaceActions.getInventoryInfo(ingrSKU, ingrName);

                        
                        String[][] data = new String[ingrSKU.size()][3];
                        for(int i = 0; i < ingrSKU.size(); i++) {
                            
                            data[i][0] = ingrSKU.get(i);
                            data[i][1] = ingrName[i];
                            data[i][2] = amount.get(i).toString();
                            System.out.println(data[i][0] + " " + data[i][1] + " " + data[i][2]);
                            
                        }

                        String[] header = {"Item ID", "Item Name", "Amount Ordered"};
                        JTable table = new JTable(data, header);
                        table.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
                       
                        JScrollPane popularityScroll1 = new JScrollPane(table);
                        popularityScroll1.setBackground(new Color(255,255,255));
                        popularityScroll1.setPreferredSize(new Dimension(600, 250));
                        popularityResult.add(popularityScroll1);
                        
                        
                        popularityResult.revalidate();
                        popularityResult.repaint();
                    }
                }
            }
        } );
        f.add(enter);

        // default button
        JButton df = new JButton("Default");
        df.setBounds(560, 160, 100, 30);
        df.setBackground(backButtonColor);
        df.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 10));
        df.setOpaque(true);
        df.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(ae.getSource() == df) {
                    startDate.setText("2022-02-01");
                    endDate.setText("2022-03-02");
                }
            }
        } );
        f.add(df);

    }

 /////////////////////////////////// ////////////////// GUI Login / Logout ////////////////////////// ////////////////////////////////////

    public void guiLogin() {
        String password = JOptionPane.showInputDialog("Please enter password:");

        if (password == null) {
            System.exit(0); // so that clicking "cancel" in the login dialog does not throw an error
        }
        
        try {
            InterfaceActions.login(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error logging in. Closing...");
            e.printStackTrace();
            System.exit(0);
        }
        //JOptionPane.showMessageDialog(null, "Logged in successfully.");
    }

    public void guiLogout() {
        try {
            InterfaceActions.logout();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error logging out. Closing...");
            e.printStackTrace();
            System.exit(0);
        }
        //JOptionPane.showMessageDialog(null, "Logged out successfully.");
    }
 
    public void StartGUI() {

        guiLogin();
        
        chooseInterface();
    }
    
}