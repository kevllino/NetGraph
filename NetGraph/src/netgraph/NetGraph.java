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
import netgraph.NetGraph;
import javax.swing.JProgressBar;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JOptionPane;

public class NetGraph extends JFrame {

//global private variables and constants
//regex to match IP addresses
    private static final String IPADDRESS_PATTERN
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
//number of element in an IP address
    private static final int NUMBER_OF_ELEMENTS_IN_IP = 4;

    private static int number_tree = 0;
    private static int previous_length = 0;
    private static ArrayList<DefaultMutableTreeNode> tab = new ArrayList<DefaultMutableTreeNode>();
    private JTree tree;
    private JTextField tZone = new JTextField();
    private JTextField tZone1 = new JTextField();
    private JTextField tZone2 = new JTextField();
    private JLabel enter = new JLabel("Fakeroute: ");
    private JButton randomIP = new JButton("Random IP Generator");
    private JLabel winIP = new JLabel("Tracert");
    private JLabel linuxIP = new JLabel("Traceroute");
    private JButton exitButton = new JButton("Exit");
    // Text area to display contents
    private static DefaultMutableTreeNode top = null;
    private JPanel jta = new JPanel();
    private String IP = "";
    //command to get a tracert output
    private String cmd = "java -jar C:\\Users\\Kevin\\Downloads\\fakeroute.jar  ";
    private String cmdWindows = "tracert -d ";
    private String cmdLinux = "traceroute -n ";
    //private String cmd = "tracert -d";
    private String rootNode;
    JProgressBar progressBar = new JProgressBar(0, 100);
    protected int progressValue = 0;
    private JFrame windows = new JFrame();
    private JPanel panel2 = new JPanel();

    private DefaultTreeModel treeModel = new DefaultTreeModel(null);

    private JPanel panel1 = new JPanel(new GridLayout(3, 2, 5, 5));
    private JPanel panel3 = new JPanel(new GridLayout(2, 1, 5, 5));

    //main function where we call the graph constructor
    public static void main(String[] args) {
        //warning message
        //custom title, warning icon
         JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,"Change the fakeroute.jar absolute path before running the Fakeroute button!","Absolute path of fakeroute.jar",JOptionPane.WARNING_MESSAGE);
        // TODO code application logic here
        NetGraph test = new NetGraph();

    }

    //grow the JTree by adding the children (IPs)
    public void GrowTree() {

        //windows.setLayout(new BorderLayout(2,1));
        tree = new JTree(treeModel);

        JScrollPane treeView = new JScrollPane(tree);
        treeView.setPreferredSize(new Dimension(600, 450));

        panel2.add(treeView);
        windows.add(panel2, BorderLayout.CENTER);
        windows.add(progressBar, BorderLayout.SOUTH);
        windows.setSize(600, 450);
        windows.setTitle("Several IP traces");
        expandAll(tree);
        // windows.setLocationRelativeTo( null );
        windows.setVisible(true); // It is necessary to show the frame here!
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        number_tree++;

    }

    public void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    //create a frame with gridlayout
    public class ShowGridLayout extends JFrame {

        public ShowGridLayout() {

            //set progress bar value
            progressBar.setValue(progressValue);
// Set GridLayout, 4 rows, 1 columns, and gaps 5 between
            // components horizontally and vertically

            setLayout(new GridLayout(2, 1, 5, 5));

// Add labels and text fields to the frame
            panel1.add(enter);
            panel1.add(tZone);
            panel1.add(winIP);
            panel1.add(tZone1);
            panel1.add(linuxIP);
            panel1.add(tZone2);
            panel3.add(randomIP);
            panel3.add(exitButton);

            add(panel1);
            add(panel3);
            //register listeners
            tZone.addActionListener(new NetGraph.TextFieldListener());
            randomIP.addActionListener(new NetGraph.ButtonFieldListener());
            tZone1.addActionListener(new NetGraph.TextFieldWinListener());
            tZone2.addActionListener(new NetGraph.TextFieldLinuxListener());
            exitButton.addActionListener(new NetGraph.ExitFieldListener());

        }
    }

    public NetGraph() {

        // create GUI to enter IP and dispslay the graph
        NetGraph.ShowGridLayout p = new NetGraph.ShowGridLayout();
        p.setLocationRelativeTo(null);//center frame

        p.setTitle("IP Trace");
        p.setSize(300, 200);

        p.setVisible(true); // It is necessary to show the frame here!
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //create the Random IP generator listener to generate random IP adresses from tracert
    private class TextFieldWinListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                IP = tZone2.getText().trim();
                System.out.println(IP);
                //trace the graph
                String cmdprevious = cmdWindows;
                cmdWindows += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmdWindows);
                //clear the IP, set IP to a blank value in order to get other IP traces
                IP = "";
                cmdWindows = cmdprevious;
                //increase progress bar value 
                progressValue += 5;
                progressBar.setValue(progressValue);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                //parse output 
                String[] check = parseOutput(stdInput);

                if (number_tree != 0) {

                    treeModel.reload();
                    expandAll(tree);
                } else {

                    GrowTree();

                }

            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

    }

    //create the Random IP generator listener to generate random IP adresses.
    private class ButtonFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //create random IP 
                int[] elements = new int[NUMBER_OF_ELEMENTS_IN_IP];
                for (int k = 0; k < NUMBER_OF_ELEMENTS_IN_IP - 1; k++) {
                    elements[k] = (int) (255 * Math.random());
                    //concatenate the numbers to get the final IP
                    IP += elements[k] + ".";
                }
                //add the last element to avoid getting "." at the end of the IP 
                elements[NUMBER_OF_ELEMENTS_IN_IP - 1] = (int) (255 * Math.random());
                IP += elements[NUMBER_OF_ELEMENTS_IN_IP - 1];

                //test if IP was correctly generated
                System.out.println("RANDOM IP IS: " + IP);

                //trace the graph
                String cmdprevious = cmd;
                cmd += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);
                //clear the IP, set IP to a blank value in order to get other IP traces
                IP = "";
                cmd = cmdprevious;
                //increase progress bar value 
                progressValue += 5;
                progressBar.setValue(progressValue);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                //parse output 
                String[] check = parseOutput(stdInput);

                if (number_tree != 0) {
                    treeModel.reload();
                    expandAll(tree);
                } else {
                    GrowTree();

                }

            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

    }

    //create the Random IP generator listener to generate random IP adresses.
    private class TextFieldLinuxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                IP = tZone1.getText().trim();
                System.out.println(IP);
                //trace the graph
                String cmdprevious = cmdLinux;
                cmdLinux += " " + IP;

                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmdLinux);
                //clear the IP, set IP to a blank value in order to get other IP traces
                IP = "";
                cmdLinux = cmdprevious;
                //increase progress bar value 
                progressValue += 5;
                progressBar.setValue(progressValue);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                //parse output 
                String[] check = parseOutput(stdInput);

                if (number_tree != 0) {
                    treeModel.reload();
                    expandAll(tree);
                } else {
                    GrowTree();

                }

            } catch (Exception ex) {
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
                progressValue += 5;
                progressBar.setValue(progressValue);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                //parse output 
                String[] check = parseOutput(stdInput);

                if (number_tree != 0) {
                    treeModel.reload();
                    expandAll(tree);
                } else {
                       GrowTree();
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
    private class ExitFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                System.exit(0);

            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

    }

    private DefaultMutableTreeNode createNodes(DefaultMutableTreeNode top, String newNode) {
        DefaultMutableTreeNode category = null;
        category = new DefaultMutableTreeNode(newNode);
        top.add(category);
        return category;

    }

    public String[] parseOutput(BufferedReader stdInput) {

        previous_length = tab.size();
        String IP = "no ip";
        String s, s1 = null;
        String[] tokens = null;
        rootNode = tZone.getText();
        System.out.println("Root Node : " + rootNode);

        if (number_tree == 0) {
            top = new DefaultMutableTreeNode(rootNode);
            treeModel.setRoot(top);
        }

        System.out.println("top :" + top.getUserObject());

        try {
            //read each line
            while ((s = stdInput.readLine()) != null) {
                //split each line into words
                tokens = s.split(" ");
                System.out.println("La taille de tokens vaut " + tokens.length);
                for (int i = 0; i < tokens.length; i++) {
                    //System.out.print(tokens[i] + " ");

                    if (tokens[i].matches(IPADDRESS_PATTERN)) {

                        IP = tokens[i];
                        DefaultMutableTreeNode no = new DefaultMutableTreeNode(IP);
                        tab.add(no);
                        System.out.println("IP : " + IP);
                        System.out.println("Nombre Arbre" + number_tree);

                    } else {/* System.out.println("Erreur!");*/

                    }

                }
            }
            if (number_tree == 0) {
                System.out.println("1er Arbre");
                for (int i = 0; i < tab.size(); i++) {
                    if (i == 0) {
                        top.add(tab.get(i));
                        tab.get(i).add(tab.get(i + 1));
                    } else if (i != (tab.size() - 1)) {
                        tab.get(i).add(tab.get(i + 1));

                    }

                }

            } else if (number_tree != 0) {
                DefaultMutableTreeNode ip = null;
                int n = 0;
                System.out.println("Taille précédente de la table de noeuds : " + previous_length);
                System.out.println("Voici le tableau de noeuds");
                for (int i = 0; i < tab.size(); i++) {
                    System.out.println(tab.get(i).getUserObject());
                }

                System.out.println("Le tab suivant a pour length : " + (tab.size() - previous_length));
                //System.out.println("Recherche...");
                for (int j = 0; j < previous_length; j++) {
                    //System.out.println("Début de Recherche...");
                    for (int k = previous_length; k < tab.size(); k++) {

                        if (tab.get(j).getUserObject().equals(tab.get(k).getUserObject())) {

                            n = k;
                            ip = tab.get(j);
                            top.add(ip);

                          
                        } else {

                         
                        }

                    }

                }
                for (int l = previous_length; l < tab.size(); l++) {
                    if (ip != null) {
                        System.out.println("Adresse ip commune");
                        createNodes(ip, (String) tab.get(l).getUserObject() + "IP Fils n° " + l);
                    } else if (l != n) {
                        createNodes(top, (String) tab.get(l).getUserObject() + "Fils n° " + l);
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
