/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servershootingstar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Goran
 */
public class ServerShootingStar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4343, 10);
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        
        while(true) {
            // Receiving
            byte[] lenBytes = new byte[4];
            is.read(lenBytes, 0, 4);
            int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                      ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
            byte[] receivedBytes = new byte[len];
            is.read(receivedBytes, 0, len);
            String received = new String(receivedBytes, 0, len);

            System.out.println("Server received: " + received);

    //        Communication.sendAngle(BallDetector.getAngleFromRobot(Integer.parseInt(received)));
            Communication.sendAngle(serverSocket, is, os, BallDetector.getAngleFromRobot(Integer.parseInt(received)));
        }
        
//        Communication.sendAngle(serverSocket, is, os, "Echo " + received);
//        socket.close();
    }
    
}
