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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import netgraph.NetGraph;

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

    private JTree tree;
    private JTextField tZone = new JTextField();
    private JButton randomIP = new JButton("Random IP Generator");
    // Text area to display contents
    private JPanel jta = new JPanel();
    private String IP = "";
    //command to get a tracert output
    private String cmd = "java -jar C:\\Users\\Kevin\\Downloads\\fakeroute.jar  ";
    private String rootNode;

    //main function where we call the graph constructor
    public static void main(String[] args) {
        // TODO code application logic here
        new NetGraph();
    }
    
    public NetGraph() {

     
        // create GUI to enter IP and dispslay the graph
        
        //create IP area
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
        p.add(tZone, BorderLayout.CENTER);
        tZone.setHorizontalAlignment(JTextField.LEFT);
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(jta, BorderLayout.CENTER);
        
        //add the IP random generator option
        p.add(randomIP, BorderLayout.SOUTH);
        
        //register listeners
        tZone.addActionListener(new NetGraph.TextFieldListener());
        randomIP.addActionListener(new NetGraph.ButtonFieldListener());

        setTitle("Chemin IP");
        setSize(300, 90);

        setVisible(true); // It is necessary to show the frame here!
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       

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
                System.out.println("RANDOM IP IS: "+ IP);
                
                //trace the graph
                String cmdprevious = cmd;
                cmd += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);
                cmd = cmdprevious;

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
               
                //parse output 
                String[] check = parseOutput(stdInput);

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

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
               
                //parse output 
                String[] check = parseOutput(stdInput);

                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

            } catch (Exception ex) {
                System.err.println(ex);
            }

        }

    }

    private void createNodes(DefaultMutableTreeNode top,String newNode) {
        DefaultMutableTreeNode category = null;
        category = new DefaultMutableTreeNode(newNode);
        top.add(category);

    }

    public String[] parseOutput(BufferedReader stdInput) {
        
        String IP = "no ip";
        String s, s1 = null;
        String[] tokens = null;
        rootNode = tZone.getText();
        System.out.println("Root Node : "+rootNode);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootNode);
        try {
            //read each line
            while ((s = stdInput.readLine()) != null) {
                //split each line into words
                tokens = s.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    //System.out.print(tokens[i] + " ");
                    
                   
                    if (tokens[i].matches(IPADDRESS_PATTERN)) {
                        IP = tokens[i];
                        System.out.println("IP : "+IP);
                        createNodes(top,IP);

                    } else {/* System.out.println("Erreur!");*/}

                }
            }
            
            JFrame windows = new JFrame();        
        
        JPanel panel2 = new JPanel();
        
          
        tree = new JTree(top);
        JScrollPane treeView = new JScrollPane(tree);
        windows.setLayout(new BorderLayout());
        panel2.setSize(300,200);
        panel2.add(treeView);
        
        windows.add(panel2, BorderLayout.CENTER);
       
        windows.setSize(300, 200);

        windows.setVisible(true); // It is necessary to show the frame here!
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        return tokens;
    }

}
