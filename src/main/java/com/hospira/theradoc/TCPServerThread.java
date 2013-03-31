package com.hospira.theradoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServerThread extends Thread {

    /**
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(TCPServerThread.class.getName());
    private Socket socket = null;

    public TCPServerThread(Socket socket) {
        super("TCPServerThread");
        this.socket = socket;
    }

    @Override
    public void run() {

        if (!TCPServer.getInstance().isListening()) {
            return;
        }
        
        String clientSentence;
        String capitalizedSentence;

        try {

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
            boolean reading = true;


            // The try with resources will automatically close the PrintWriter for us ...
            try (PrintWriter outToClient = new PrintWriter(out, true)) {

                while (reading) {
                    clientSentence = inFromClient.readLine();
                    logger.log(Level.FINEST, "Received: '{0}'", clientSentence);
                    capitalizedSentence = clientSentence.toUpperCase();
                    logger.log(Level.FINEST, "Converted: '{0}'", capitalizedSentence);
                    outToClient.println(capitalizedSentence);
                    logger.log(Level.FINEST, "Written to client");

                    if (clientSentence.contains("quit")) {
                        outToClient.printf("Closing down server for '%s'", clientSentence);
                        logger.log(Level.INFO, "Closing down server for ''{0}''", clientSentence);
                        reading = false;
                        TCPServer.getInstance().stopServer();
                        System.exit(0);
                    }
                }

            }
            inFromClient.close();
            socket.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Caught IO Exception - client disconnect ?", ex);
        }


    }
}