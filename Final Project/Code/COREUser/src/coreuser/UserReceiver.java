/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreuser;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class UserReceiver extends Thread{
    
    UserFrame uf;
    int userid,port;
    int tccount=0;
    
    UserReceiver(UserFrame f,int id)
    {
        uf=f;
        userid=id;
        port=userid+7000;
    }
    
    public void run()
    {
        try
        {
            DatagramSocket ds=new DatagramSocket(port);
            while(true)
            {                
                byte data[]=new byte[10000];
                DatagramPacket dp=new DatagramPacket(data,0,data.length);
                ds.receive(dp);
                String str=new String(dp.getData()).trim(); 
                String req[]=str.split("#");
                if(req[0].trim().equals("CloudServerDetails"))       
                {
                    String allServerId=req[1].trim();
                    if(allServerId.trim().equals("-"))
                    {
                        JOptionPane.showMessageDialog(uf,"Cloud Servers are Unavailable!");
                    }
                    else
                    {
                        if(allServerId.trim().contains(","))
                        {
                            String sp[]=allServerId.trim().split(",");
                            for(int i=0;i<sp.length;i++)
                            {
                                String servId=sp[i].trim();
                                
                                DefaultTableModel dm=(DefaultTableModel)uf.jTable1.getModel();
                                Vector v=new Vector();
                                v.add(servId.trim());
                                dm.addRow(v);
                                
                                uf.jComboBox1.addItem(servId.trim());
                            }
                        }
                        else
                        {
                            DefaultTableModel dm=(DefaultTableModel)uf.jTable1.getModel();
                            Vector v=new Vector();
                            v.add(allServerId.trim());
                            dm.addRow(v);
                            
                            uf.jComboBox1.addItem(allServerId.trim());
                        }
                    }
                }
                if(req[0].trim().equals("PredictionACK"))       
                {
                    String res=req[1].trim();
                    JOptionPane.showMessageDialog(uf,"Prediction Result Received Successfully!");
                    JOptionPane.showMessageDialog(uf,res.trim());
                    JOptionPane.showMessageDialog(uf,"No alternate way! You should sent all chunks!");
                }
                if(req[0].trim().equals("PredictionResults"))       
                {
                    String rowId=req[1].trim();
                    String chunkId=req[2].trim();
                    String chunkSig=req[3].trim();
                    
                    DefaultTableModel dm=(DefaultTableModel)uf.jTable2.getModel();
                    Vector v=new Vector();
                    v.add(chunkSig.trim());
                    v.add(chunkId.trim());
                    v.add(rowId.trim());
                    dm.addRow(v);   
                                        
                    if(tccount==5)
                    {                        
                        tccount--;
                        
                        DefaultTableModel dm2=(DefaultTableModel)uf.jTable3.getModel();
                        dm2.removeRow(0);
                        
                        DefaultTableModel dm1=(DefaultTableModel)uf.jTable3.getModel();
                        Vector v1=new Vector();
                        v1.add(chunkSig.trim());
                        v1.add(chunkId.trim());
                        v1.add(rowId.trim());
                        dm1.addRow(v1);
                        tccount++;
                    }
                    else
                    {
                        DefaultTableModel dm1=(DefaultTableModel)uf.jTable3.getModel();
                        Vector v1=new Vector();
                        v1.add(chunkSig.trim());
                        v1.add(chunkId.trim());
                        v1.add(rowId.trim());
                        dm1.addRow(v1);
                        tccount++;
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
