/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author carlo
 */
public class Client {

    final static int PORT = 40080;
    final static String HOST = "localhost";

    public static void main(String[] args) {
        try {
            Socket sk = new Socket(HOST, PORT);

            sendMessageToServer(sk);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void sendMessageToServer(Socket sk) {
        OutputStream os = null;
        InputStream is = null;
        DataInputStream dis = null;
        try {
            os = sk.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            is = sk.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            dis = new DataInputStream(is);
            Scanner sc = new Scanner(System.in);
            String linea;
            while (true) {

                linea = br.readLine();
                System.out.println(linea);
                linea = sc.nextLine();
                bw.write(linea);
                bw.newLine();
                bw.flush();
                byte[] message = null;

                int length = dis.readInt();                    // read length of incoming message
                if (length > 0) {
                    message = new byte[length];
                    dis.readFully(message, 0, message.length); // read the message
                }

                File f = new File("./myfile.mp3");
                try ( FileOutputStream fos = new FileOutputStream(f)) {
                    fos.write(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

               FileInputStream fis = new FileInputStream(f);
               Player playMP3 = new Player(fis);
               playMP3.play();
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
