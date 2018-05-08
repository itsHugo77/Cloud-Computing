/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreuser;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author admin
 */
public class Main {
    public static void main(String args[]) throws Exception
    {
        UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        
        String uid=JOptionPane.showInputDialog(new JFrame(), "Enter the User Id: ");
        
        UserFrame uf=new UserFrame(Integer.parseInt(uid.trim()));
        uf.setVisible(true);
        uf.setResizable(false);
        uf.setTitle("User - "+uid.trim());
        uf.jLabel1.setText("User - "+uid.trim());
        
        UserReceiver csr=new UserReceiver(uf,Integer.parseInt(uid.trim()));
        csr.start();
    }
}