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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new NetGraph();
    }
    private static final String IPADDRESS_PATTERN =
"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private JTree tree;
    private JTextField tZone = new JTextField();
    // Text area to display contents
    private JPanel jta = new JPanel();
    private String IP;
    private String cmd = "java -jar C:\\Users\\Armand\\Documents\\NetBeansProjects\\NetGraph1\\NetGraph\\fakeroute.jar  ";
    private String rootNode;

    public NetGraph() {

     
        // create GUI to enter IP and dispslay the graph
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
        p.add(tZone, BorderLayout.CENTER);
        tZone.setHorizontalAlignment(JTextField.LEFT);
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        //jta.add(treeView);
        add(jta, BorderLayout.CENTER);

        tZone.addActionListener(new TextFieldListener());

        setTitle("Chemin IP");
        setSize(300, 90);

        setVisible(true); // It is necessary to show the frame here!
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Fenetre crée!");

    }

    //create the TEextListener to launch/execute the tracert command
    private class TextFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                
                //get the information from tZone
          
                IP = tZone.getText().trim();
                System.out.println(IP);
                cmd += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
               
                //parse output 
                String[] check = parseOutput(stdInput);

                // read the output from the command
                System.out.println("Here is the standard output of the command:\n");
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

       /* //original Tutorial
        book = new DefaultMutableTreeNode("The Java Tutorial: A Short Course on the Basics");

        category.add(book);

        //Tutorial Continued
        book = new DefaultMutableTreeNode("The Java Tutorial Continued: The Rest of the JDK");
        category.add(book);

        //Swing Tutorial
        book = new DefaultMutableTreeNode("The Swing Tutorial: A Guide to Constructing GUIs");

        category.add(book);

        //...add more books for programmers...
        category = new DefaultMutableTreeNode("Books for Java Implementers");
        top.add(category);

        //VM
        book = new DefaultMutableTreeNode("The Java Virtual Machine Specification");

        category.add(book);

        //Language Spec
        book = new DefaultMutableTreeNode("The Java Language Specification");

        category.add(book);*/
    }

    public String[] parseOutput(BufferedReader stdInput) {
        
        String IP = "no ip";
        String s, s1 = null;
        String[] tokens = null;
        rootNode = tZone.getText();
        System.out.println("Racine : "+rootNode);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootNode);
        try {
            //read each line
            while ((s = stdInput.readLine()) != null) {
                //split each line into words
                tokens = s.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    //System.out.print(tokens[i] + " ");
                    
                    //[0-9]*{1,3}\\.[0-9]*{1,3}\\.[0-9]*{1,3}\\.[0-9]*{1,3}
                    if (tokens[i].matches(IPADDRESS_PATTERN)) {
                        IP = tokens[i];
                        System.out.println("IP : "+IP);
                        createNodes(top,IP);

                    } else { /*System.out.println("Erreur!");*/}

                }
            }
            
            JFrame windows = new JFrame();        
        
        JPanel panel2 = new JPanel();
        
        //top.add(new DefaultMutableTreeNode("Bonjour"));
          
        tree = new JTree(top);
        JScrollPane treeView = new JScrollPane(tree);
        windows.setLayout(new BorderLayout());
        panel2.setSize(300,200);
        panel2.add(treeView);
        
        windows.add(panel2, BorderLayout.CENTER);
       
        windows.setSize(300, 200);

        windows.setVisible(true); // It is necessary to show the frame here!
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Fenetre crée!");

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        return tokens;
    }

}
