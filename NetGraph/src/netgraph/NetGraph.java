/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netgraph;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.lang.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
//import javax.swing.tree.NetNode;
import netgraph.NetGraph;
import javax.swing.JProgressBar;
import javax.swing.tree.DefaultTreeModel;

public class NetGraph extends JFrame {

//global private variables and constants
//regex to match IP addresses
 private static final String IPADDRESS_PATTERN =
"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
//number of element in an IP address
private static final int NUMBER_OF_ELEMENTS_IN_IP = 4;
    private static int number_tree = 0 ; 
    private static int previous_length = 0 ;
    private static ArrayList<NetNode> tab = new ArrayList<NetNode>();       
    private JTree tree;
    private JTextField tZone = new JTextField();
    private JLabel label = new JLabel("Enter IP or Domain Name: ");
    private JButton randomIP = new JButton("Random IP Generator");
    private JButton exitButton = new JButton("Exit");
    // Text area to display contents
    private JPanel jta = new JPanel();
    private String IP = "";
    //command to get a tracert output
    private String cmd = "java -jar C:\\Users\\Armand\\Documents\\NetBeansProjects\\NetGraph1\\NetGraph\\fakeroute.jar  ";
    //private String cmd = "tracert -d";
    private String rootNode;
    //add IPs  to:
    private JFrame windows = new JFrame();   
    private JPanel panel2 = new JPanel();
    private int progressValue = 0;
    JProgressBar progressBar = new JProgressBar(0,100);
    DefaultTreeModel treeModel = new DefaultTreeModel(null);

    //main function where we call the graph constructor
    public static void main(String[] args) {
        // TODO code application logic here
        NetGraph test = new NetGraph();
        
    }
    
    //create a frame with gridlayout
    public class ShowGridLayout extends JFrame {
        public ShowGridLayout() {
            
            //set progress bar value
            progressBar.setValue(progressValue);
// Set GridLayout, 4 rows, 1 columns, and gaps 5 between
 // components horizontally and vertically
      
 setLayout(new GridLayout(4, 1, 5, 5));

// Add labels and text fields to the frame
   
        add(label);
        add(tZone);

        add(randomIP);
        add(exitButton);
         //register listeners
         tZone.addActionListener(new NetGraph.TextFieldListener());
        randomIP.addActionListener(new NetGraph.ButtonFieldListener());
        exitButton.addActionListener(new NetGraph.ExitFieldListener());
}
    }
    public NetGraph() {

       
        // create GUI to enter IP and dispslay the graph
        ShowGridLayout p = new ShowGridLayout();
        p.setLocationRelativeTo(null);//center frame

        //p.setLayout(new BorderLayout());
       /* p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
        p.add(tZone, BorderLayout.CENTER);
        tZone.setHorizontalAlignment(JTextField.LEFT);
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(jta, BorderLayout.CENTER);*/
        
        //add the IP random generator option
        //p.add(randomIP, BorderLayout.NORTH);
        //p.add(exitButton,BorderLayout.SOUTH); 
        
        p.setTitle("IP Trace");
        p.setSize(300, 200);

        p.setVisible(true); // It is necessary to show the frame here!
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       

    }
    //create the Random IP generator listener to generate random IP adresses.
    private class ButtonFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                //create random IP 
                int[] elements = new int[NUMBER_OF_ELEMENTS_IN_IP];
                for(int k =0; k<NUMBER_OF_ELEMENTS_IN_IP - 1; k++){
                    elements[k] = (int) (255*Math.random());
                       //concatenate the numbers to get the final IP
                    IP += elements[k] + ".";
                }
                //add the last element to avoid getting "." at the end of the IP 
                elements[NUMBER_OF_ELEMENTS_IN_IP-1] = (int) (255*Math.random());
                IP +=   elements[NUMBER_OF_ELEMENTS_IN_IP-1];
                
                //test if IP was correctly generated
                System.out.println("RANDOM IP IS: "+ IP);
                
                //trace the graph
                String cmdprevious = cmd;
                cmd += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);
                //clear the IP, set IP to a blank value in order to get other IP traces
                IP = "";
                cmd = cmdprevious;
                //increase progress bar value 
                progressValue +=5;
               progressBar.setValue(progressValue);
                
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
               
                //parse output 
                String[] check = parseOutput(stdInput);
                
                if(number_tree != 0)
                    treeModel.reload();
                else {
                    tree = new JTree(treeModel);
        JScrollPane treeView = new JScrollPane(tree);
        windows.setLayout(new BorderLayout());
        panel2.setSize(300,200);
        panel2.add(treeView);
        
        windows.add(panel2, BorderLayout.CENTER);
       windows.add(progressBar,BorderLayout.SOUTH);
        windows.setSize(1000, 500);
        //windows.setSize(600, 450);
        windows.setTitle("Several IP traces");
        windows.setVisible(true); // It is necessary to show the frame here!
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        number_tree ++;
                    
                    
                    
                }
                    

            }
            catch(Exception ex){
                System.err.println(ex);
            }
        }
        
    }

    //create the TEextListener to launch/execute the tracert command
    private class TextFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                
                //get the information from tZone
          
                IP = tZone.getText().trim();
                System.out.println(IP);
                String cmdprevious = cmd;
                cmd += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);
                cmd = cmdprevious;
                  //increase progress bar value 
                progressValue +=5;
                 progressBar.setValue(progressValue);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
               
                //parse output 
                String[] check = parseOutput(stdInput);
                
                if(number_tree != 0)
                    treeModel.reload();
                else {
                    tree = new JTree(treeModel);
        JScrollPane treeView = new JScrollPane(tree);
        windows.setLayout(new BorderLayout());
        panel2.setSize(300,200);
        panel2.add(treeView);
        
        windows.add(panel2, BorderLayout.CENTER);
       windows.add(progressBar,BorderLayout.SOUTH);
        windows.setSize(1000, 500);
        //windows.setSize(600, 450);
        windows.setTitle("Several IP traces");
        windows.setVisible(true); // It is necessary to show the frame here!
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        number_tree ++;
                    
                    
                    
                }

                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

            } catch (Exception ex) {
                System.err.println(ex);
            }

        }

    }
     //create the Random IP generator listener to generate random IP adresses.
    private class ExitFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                 System.exit(0);

            }
            catch(Exception ex){
                System.err.println(ex);
            }
        }
        
    }

    private NetNode createNodes(NetNode top,String newNode) {
        NetNode category = null;
        category = new NetNode(newNode);
        top.add(category);
        
        return category;

    }

    public String[] parseOutput(BufferedReader stdInput) {
        
        previous_length = tab.size();
        String IP = "no ip";
        String s, s1 = null;
        String[] tokens = null;
        rootNode = tZone.getText();
        System.out.println("Root Node : "+rootNode);
        NetNode top = new NetNode(rootNode);
        
        treeModel.setRoot(top);
        try {
            //read each line
            while ((s = stdInput.readLine()) != null) {
                //split each line into words
                tokens = s.split(" ");
                System.out.println("La taille de tokens vaut "+tokens.length);
                for (int i = 0; i < tokens.length; i++) {
                    //System.out.print(tokens[i] + " ");
                    
                   
                    if (tokens[i].matches(IPADDRESS_PATTERN)) {
                        
                        
                        IP = tokens[i];
                        tab.add(tokens[i]);
                        System.out.println("IP : "+IP);
                        System.out.println("Nombre Arbre"+number_tree);
                        if(number_tree == 0){
                        System.out.println("1er Arbre");    
                        createNodes(top,IP);
                        }
                        
                        
                        
                        

                    } else {/* System.out.println("Erreur!");*/}

                }
            }
        
        if(number_tree != 0)    {
            System.out.println("Taille précédente de la table de noeuds : "+previous_length);
            System.out.println("Voici le tableau de noeuds");
            for(int i =0;i<tab.size();i++)
                System.out.println(tab.get(i));
               
            //System.out.println("Recherche...");
        for(int j = 0;j<previous_length;j++){
            //System.out.println("Début de Recherche...");
            for(int k=previous_length;k<tab.size();k++){
                if(tab.get(j).equals(tab.get(k))){
                    
                    NetNode ip = createNodes(top,tab.get(j));
                    
                   
                    
                    System.out.println("Meme adresse ip !");
                   
                     createNodes(ip,tab.get(j)+" = "+tab.get(k));
                    //top = ip;
                     //treeModel.setRoot(top);
                }
                else{
                    System.out.println("Bingo!");
                    createNodes(top,tab.get(k));    
                }
                
            }     
        }
       }
        
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        return tokens;
    }

}
