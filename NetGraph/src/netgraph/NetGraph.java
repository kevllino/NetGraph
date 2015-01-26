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
import javax.swing.event.*;
import java.util.*;
import java.lang.*;

public class NetGraph extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         new NetGraph();
    }
    
private JTextField tZone = new JTextField();
    // Text area to display contents
private JTextArea jta = new JTextArea();
private String IP; 
private String cmd ="tracert -d";

    public NetGraph(){
        
  // create GUI to enter IP and dispslay the graph
 JPanel p = new JPanel();
 p.setLayout(new BorderLayout());
 p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
 p.add(tZone, BorderLayout.CENTER);
 tZone.setHorizontalAlignment(JTextField.RIGHT);
 setLayout(new BorderLayout());
 add(p, BorderLayout.NORTH);
 add(new JScrollPane(jta), BorderLayout.CENTER);
 
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



