/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package netgraph;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Armand
 */
public class NetNode extends DefaultMutableTreeNode{
    private String name;
    
    public NetNode (String n){
        this.name = n ;
        
        
    }
    
    public String getName(){
        
        return this.name;
    }
    
}
