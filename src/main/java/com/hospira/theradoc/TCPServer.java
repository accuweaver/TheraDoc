package com.hospira.theradoc;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Very simple TCP server ...
 *
 * @author robweaver
 */
public class TCPServer {

    /**
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
    volatile private boolean listening = true;
    private static TCPServer instance;
    private ServerSocket welcomeSocket;
    /**
     *
     */
    public static int PORT = 1234;

    /**
     * Make it a singleton by hiding the constructor
     */
    private TCPServer() {
        this.listening = true;
    }

    /**
     * To instantiate the singleton ...
     *
     * @return
     */
    public static TCPServer getInstance() {
        if (instance == null) {
            instance = new TCPServer();
        }
        return instance;
    }

    /**
     * Main method for running the server ...
     * 
     * Fires the server off into a separate thread to make it act like a real
     * server. 
     * 
     * Single threaded, and the connection is managed in the main thread.
     *
     * @param argv
     * @throws IOException  
     */
    public static void main(String argv[]) throws IOException {
        logger.info("Main Method");
        new Thread(){

            @Override
            public void run() {
                try {
                    getInstance().startServer();
                } catch (IOException ex) {
                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }.start();
        
    }

    /**
     * Public method to start the server ...
     * 
     * @throws IOException
     */
    public void startServer() throws IOException {
        logger.info("startServer");
        Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, Throwable ex) {
                logger.log(Level.SEVERE, "Exception in Thread", ex);
            }
        };

        getInstance().runServer();

    }

    /**
     * Run the actual server - this is not called directly.
     * 
     * @throws IOException 
     */
    private void runServer() throws IOException {
        logger.info("runServer");
        try {
            welcomeSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problems with opening socket on port" + PORT, e);
            System.exit(-1);
        }
        while (listening) {
            logger.info("New thread spawning");
            new TCPServerThread(welcomeSocket.accept()).start();
        }
        logger.info("Close the socket");
        welcomeSocket.close();

    }

    /**
     * Stop the server - really just changes the boolean ...
     *
     * @return
     * @throws IOException
     */
    public boolean stopServer() throws IOException {
        setListening(false);
        return isListening();
    }

    /**
     * See if the server is running or not ...
     *
     * @return the running
     */
    public boolean isListening() {
        return listening;
    }

    /**
     * Set the flag to not running
     *
     * @param listening 
     */
    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
