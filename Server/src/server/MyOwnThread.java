/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;

/**
 *
 * @author carlo
 */
public class MyOwnThread extends Thread{
        Socket sk;

    public MyOwnThread(Socket sk) {
        this.sk = sk;
    }
        
       
    @Override
    public void run(){
        InputStream is = null;
        OutputStream os = null;
        File file = null;
        DataOutputStream dos = null;
        
        
        try{
            is = sk.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            os = sk.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
           
            
            dos = new DataOutputStream(os);
            Inet4Address ip = (Inet4Address) sk.getInetAddress();
            String laIP = ip.getHostAddress();
            
            
            
            while(true){
                bw.write("1: Dubstep    2: Cradles-Sub Urban    3: Xylophone    4:Never2Old   5: Digital Bell SMS");       
                bw.newLine();
                bw.flush();
                String linea = br.readLine();
                
                file = new File("./"+linea+".mp3");
                
                byte[] fileContent = Files.readAllBytes(file.toPath());
                
                
                
                dos.writeInt(fileContent.length); // write length of the message
                dos.write(fileContent);  
                
                System.out.println(fileContent);
                System.out.println("Canción " + linea + "desde la ip : " + laIP);   
                
                
               
            }
            
        }   catch (IOException ex) {
                Logger.getLogger(MyOwnThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try{
             if(is != null) is.close();
            } catch (IOException ex) {
                Logger.getLogger(MyOwnThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
