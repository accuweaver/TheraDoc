/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hospira.theradoc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robweaver
 */
public class TCPClient {

    private Socket clientSocket;

    /**
     *
     * @param sentence
     * @return
     */
    public String talkToServer(String sentence) {
        String modifiedSentence = null;
        try {

            clientSocket = new Socket("localhost", TCPServer.PORT);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
            clientSocket = null;

        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, "Could not resolve localhost", ex);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, "IO exception", ex);
        }
        return modifiedSentence;
    }
}
