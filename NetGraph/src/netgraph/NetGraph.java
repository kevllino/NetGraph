/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netgraph;

 import java.awt.*;
 import javax.swing.*;
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

    public NetGraph(){
        // Panel p to hold the label and text field
 JPanel p = new JPanel();
 p.setLayout(new BorderLayout());
 p.add(new JLabel("Enter IP or DN: "), BorderLayout.WEST);
 p.add(tZone, BorderLayout.CENTER);
 tZone.setHorizontalAlignment(JTextField.RIGHT);

 setLayout(new BorderLayout());
 add(p, BorderLayout.NORTH);
 add(new JScrollPane(jta), BorderLayout.CENTER);



 setTitle("IP");
 setSize(500, 300);
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
 setVisible(true); // It is necessary to show the frame here!
 
 System.out.println("Fenetre crée!");
 
 //Encore une autre 
 //Yo
 //cava et toi ? cool
 //blablbaablalb
 
    }
}