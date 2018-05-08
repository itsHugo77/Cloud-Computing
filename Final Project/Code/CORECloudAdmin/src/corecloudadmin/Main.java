/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corecloudadmin;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import javax.swing.UIManager;

/**
 *
 * @author admin
 */
public class Main {
    public static void main(String args[]) throws Exception
    {
        UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
             
        CloudAdminFrame csf=new CloudAdminFrame();
        csf.setVisible(true);
        csf.setResizable(false);
        csf.setTitle("Cloud Admin");        
        
        CloudAdminReceiver csr=new CloudAdminReceiver(csf);
        csr.start();
    }
}