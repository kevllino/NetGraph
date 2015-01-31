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
 
private JTree tree;    
private JTextField tZone = new JTextField();
    // Text area to display contents
private JPanel jta = new JPanel();
private String IP; 
private String cmd ="tracert -d";

    public NetGraph(){
     //Tree
      
    DefaultMutableTreeNode top =
        new DefaultMutableTreeNode("The Java Series");
    createNodes(top);
    tree = new JTree(top);
   
    JScrollPane treeView = new JScrollPane(tree);
    //add(treeView, BorderLayout.CENTER);
  // create GUI to enter IP and dispslay the graph
 JPanel p = new JPanel();
 p.setLayout(new BorderLayout());
 p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
 p.add(tZone, BorderLayout.CENTER);
 tZone.setHorizontalAlignment(JTextField.RIGHT);
 setLayout(new BorderLayout());
 add(p, BorderLayout.NORTH);
 jta.add(treeView);
 add(jta, BorderLayout.CENTER);

 tZone.addActionListener(new TextFieldListener());
 
 setTitle("IP");
 setSize(500, 300);
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
 
 setVisible(true); // It is necessary to show the frame here!
 
 System.out.println("Fenetre crée!");
 

    }


     //create the TEextListener to launch/execute the tracert command
    private class TextFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                //get the information from tZone
                IP = tZone.getText().trim();
                System.out.println(IP);
                cmd += " "+IP;
              
                Runtime rt = Runtime.getRuntime();

                Process proc = rt.exec(cmd);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                
                //parse output 
                String[] check = parseOutput (stdInput);
                
                
                
            // read the output from the command
           /* System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                    }*/
            
                
            }
           catch (Exception ex) {
           System.err.println(ex);
             }
            
        }
        

    }
     private void createNodes(DefaultMutableTreeNode top) {
    DefaultMutableTreeNode category = null;
    DefaultMutableTreeNode book = null;
    
    category = new DefaultMutableTreeNode("Books for Java Programmers");
    top.add(category);
    
    //original Tutorial
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
        
    category.add(book);
}
    
    public String[] parseOutput (BufferedReader stdInput ){
        String[] IP = null;
         String s, s1 = null;
         String[] tokens = null;
         try{
               //read each line
            while ((s = stdInput.readLine()) != null) {
                     //split each line into words
                    tokens =  s.split("[ ]");
                    for(int i=0; i< tokens.length; i++){
                    System.out.print(tokens[i]+" ");
                    if (tokens[i].matches("[0-9]*{1,3}\\.[0-9]*{1,3}\\.[0-9]*{1,3}\\.[0-9]*{1,3}")){
                        IP[i] = tokens[i];
                        
                    }
                    
                }
                    }
           
         }
         catch(IOException e){
               System.err.println(e);
         }
        
        
        return tokens;
    }
 
}