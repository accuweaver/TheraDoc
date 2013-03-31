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

/**
 * This class handles the server connection after the call to accept.
 * 
 * @author robweaver
 */
public class TCPServerThread extends Thread {

    /**
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(TCPServerThread.class.getName());
    private Socket socket = null;

    /**
     *
     * @param socket
     */
    public TCPServerThread(Socket socket) {
        super("TCPServerThread");
        this.socket = socket;
    }

    @Override
    public void run() {

        /** 
         * this check makes sure that we don't keep doing work when we 
         * should be shutting down the server ....
         */
        if (!TCPServer.getInstance().isListening()) {
            return;
        }
        
        String clientSentence;
        String capitalizedSentence;

        try {

            // Get the input stream from the client ...
            InputStream in = socket.getInputStream();
            
            // Get the output stream for sending to the client
            OutputStream out = socket.getOutputStream();
            
            // This try-with-resource makes sure that the Buffered reader gets closed ...
            try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in))) {
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

                        /**
                         * We look for the word "quit" to shut down the server ...
                         */
                        if (clientSentence.contains("quit")) {
                            outToClient.printf("Closing down server for '%s'", clientSentence);
                            logger.log(Level.INFO, "Closing down server for ''{0}''", clientSentence);
                            reading = false;
                            TCPServer.getInstance().stopServer();
                            System.exit(0);
                        }
                    }

                }
            }
            // Make sure the streams are closed ...
            in.close();
            out.close();
            
            // close the socket - this should probably be done in the finally
            socket.close();
        } catch (IOException ex) {
            // This is most likely that the connection was terminated by the client
            logger.log(Level.SEVERE, "Caught IO Exception - client disconnect ?", ex);
        }


    }
}