/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corecloudserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class CloudServerReceiver extends Thread{
    
    CloudServerFrame csf;
    int csid,port;
    int rowwid=0;
    public static ArrayList bandwidthSavings=new ArrayList();
    
    CloudServerReceiver(CloudServerFrame f,int id)
    {
        csf=f;
        csid=id;
        port=csid+6000;
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
                if(req[0].trim().equals("Connect"))       
                {
                    String uid=req[1].trim();
                    
                    DefaultTableModel dm=(DefaultTableModel)csf.jTable1.getModel();
                    Vector v=new Vector();
                    v.add(uid.trim());
                    dm.addRow(v);
                }
                if(req[0].trim().equals("Upload"))       
                {
                    rowwid++;
                    
                    int savedbandwidth=0;
                    
                    String uid=req[1].trim();
                    String allData=req[2].trim();
                    String sp[]=allData.trim().split("\n");
                    String uploadData="";
                    for(int i=0;i<sp.length;i++)
                    {
                        String s[]=sp[i].trim().split("@");
                        String chunkId=s[0].trim();
                        String chunk=s[1].trim();
                        String chuSig=s[2].trim();
                        
                        uploadData=uploadData+chunk.trim();
                        
                        DefaultTableModel dm=(DefaultTableModel)csf.jTable2.getModel();
                        Vector v=new Vector();
                        v.add(rowwid);
                        v.add(chunkId.trim());
                        v.add(chunk.trim());
                        v.add(chuSig.trim());
                        dm.addRow(v);
                    }
                    
                    DefaultTableModel dm=(DefaultTableModel)csf.jTable3.getModel();
                    Vector v=new Vector();
                    v.add(rowwid);
                    v.add(uid.trim());
                    v.add(uploadData.trim());
                    dm.addRow(v);
                    
                    JOptionPane.showMessageDialog(csf,"Uploaded Successfully!");
                    
                    bandwidthSavings.add(rowwid+"@"+savedbandwidth);
                }
                if(req[0].trim().equals("UploadWithMatched"))       
                {
                    rowwid++;
                    
                    int savedbandwidth=0;
                    
                    String uid=req[1].trim();
                    String allData=req[2].trim();
                    String sp[]=allData.trim().split("\n");
                    String uploadData="";
                    for(int i=0;i<sp.length;i++)
                    {
                        if(sp[i].trim().contains("@"))
                        {
                            String s[]=sp[i].trim().split("@");
                            String chunkId=s[0].trim();
                            String chunk=s[1].trim();
                            String chuSig=s[2].trim();

                            uploadData=uploadData+chunk.trim();

                            DefaultTableModel dm=(DefaultTableModel)csf.jTable2.getModel();
                            Vector v=new Vector();
                            v.add(rowwid);
                            v.add(chunkId.trim());
                            v.add(chunk.trim());
                            v.add(chuSig.trim());
                            dm.addRow(v);
                        }
                        if(sp[i].trim().contains("\t"))
                        {
                            String s[]=sp[i].trim().split("\t");
                            String roId=s[0].trim();
                            String chId=s[1].trim();
                            
                            for(int j=0;j<csf.jTable2.getRowCount();j++)
                            {
                                String rowId=csf.jTable2.getValueAt(j,0).toString().trim();
                                String chunkId=csf.jTable2.getValueAt(j,1).toString().trim();
                                String chunk=csf.jTable2.getValueAt(j,2).toString().trim();
                                
                                if((roId.trim().equals(rowId.trim()))&&(chId.trim().equals(chunkId.trim())))
                                {
                                    uploadData=uploadData+chunk.trim();
                                    savedbandwidth=savedbandwidth+chunk.trim().length();
                                    break;
                                }
                            }
                        }
                    }
                    
                    DefaultTableModel dm=(DefaultTableModel)csf.jTable3.getModel();
                    Vector v=new Vector();
                    v.add(rowwid);
                    v.add(uid.trim());
                    v.add(uploadData.trim());
                    dm.addRow(v);
                    
                    JOptionPane.showMessageDialog(csf,"Uploaded Successfully!");
                    
                    bandwidthSavings.add(rowwid+"@"+savedbandwidth);
                    
                }
                if(req[0].trim().equals("Predict"))       
                {
                    String uid=req[1].trim();
                    String firstChunkSignature=req[2].trim();
                    
                    String rowId="";
                    int cou=0;
                    for(int i=0;i<csf.jTable2.getRowCount();i++)
                    {
                        String chuSig=csf.jTable2.getValueAt(i,3).toString().trim();
                        
                        if(firstChunkSignature.trim().equals(chuSig.trim()))
                        {
                            cou=1;
                            rowId=csf.jTable2.getValueAt(i,0).toString().trim();
                        }                        
                    }
                    if(cou==0)      // Signature not matched
                    {
                        String msg="PredictionACK#"+"Signature not Matched";
                        int pt=Integer.parseInt(uid.trim())+7000;
                        JOptionPane.showMessageDialog(csf,"First Chunk Signature not Matched!");
                        PacketTransmission(msg,pt);
                    }
                    else            // Signature Matched
                    {
                        JOptionPane.showMessageDialog(csf,"First Chunk Signature Matched!");
                        
                        for(int i=0;i<csf.jTable2.getRowCount();i++)
                        {
                            String rowId1=csf.jTable2.getValueAt(i,0).toString().trim();

                            if(rowId.trim().equals(rowId1.trim()))
                            {
                                String chuId=csf.jTable2.getValueAt(i,1).toString().trim();
                                String chuSig=csf.jTable2.getValueAt(i,3).toString().trim();
                                
                                String msg="PredictionResults#"+rowId1.trim()+"#"+chuId.trim()+"#"+chuSig.trim();
                                int pt=Integer.parseInt(uid.trim())+7000;                                
                                PacketTransmission(msg,pt);
                            }
                        }
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
