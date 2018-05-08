/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corecloudserver;

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
        
        String csid=JOptionPane.showInputDialog(new JFrame(), "Enter the Cloud Server Id: ");
        
        CloudServerFrame csf=new CloudServerFrame(Integer.parseInt(csid.trim()));
        csf.setVisible(true);
        csf.setResizable(false);
        csf.setTitle("Cloud Server - "+csid.trim());
        csf.jLabel1.setText("Cloud Server - "+csid.trim());
        
        CloudServerReceiver csr=new CloudServerReceiver(csf,Integer.parseInt(csid.trim()));
        csr.start();
    }
}