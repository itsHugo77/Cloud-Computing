/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corecloudadmin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class CloudAdminReceiver extends Thread{
    
    CloudAdminFrame caf;
    
    CloudAdminReceiver(CloudAdminFrame af)
    {
        caf=af;        
    }
    
    public void run()
    {
        try
        {
            DatagramSocket ds=new DatagramSocket(5000);
            while(true)
            {                
                byte data[]=new byte[10000];
                DatagramPacket dp=new DatagramPacket(data,0,data.length);
                ds.receive(dp);
                String str=new String(dp.getData()).trim(); 
                String req[]=str.split("#");
                if(req[0].trim().equals("Connect"))       
                {
                    String serverId=req[1].trim();
                    
                    DefaultTableModel dm=(DefaultTableModel)caf.jTable1.getModel();
                    Vector v=new Vector();
                    v.add(serverId.trim());
                    dm.addRow(v);
                }
                if(req[0].trim().equals("CloudServerDetails"))       
                {
                    String uid=req[1].trim();
                    
                    String sid="";
                    for(int i=0;i<caf.jTable1.getRowCount();i++)
                    {
                        String serverId=caf.jTable1.getValueAt(i,0).toString().trim();
                        sid=sid+serverId.trim()+",";
                    }
                    String allServerId="-";
                    if(!(sid.trim().equals("")))
                    {
                        allServerId=sid.substring(0,sid.lastIndexOf(','));
                        String msg="CloudServerDetails#"+allServerId.trim();
                        int pt=Integer.parseInt(uid.trim())+7000;
                        PacketTransmission(msg,pt);
                    }                    
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void PacketTransmission(String msg, int pt) {
        try
        {
            byte data1[]=msg.getBytes();
            DatagramSocket ds1=new DatagramSocket();
            DatagramPacket dp1=new DatagramPacket(data1,0,data1.length,InetAddress.getByName("127.0.0.1"),pt);
            ds1.send(dp1);
            System.out.println("Port is "+pt+"\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
