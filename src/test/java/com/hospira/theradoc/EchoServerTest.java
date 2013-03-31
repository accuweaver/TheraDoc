package com.hospira.theradoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author robweaver
 */
public class EchoServerTest {

    /**
     * Logger for this class ...
     */
    private static final Logger logger = Logger.getLogger(EchoServerTest.class.getName());

    /**
     * Constructor
     */
    public EchoServerTest() {
    }

    /**
     * 
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class EchoServer.
     * 
     * @throws Exception 
     */
    @Test
    public void testMain() throws Exception {
        logger.info("main");

        EchoServer.main(new String[0]);

        Client client = new Client();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     *
     */
    public class Client {

        /**
         *
         */
        public Client() {
            String line;
            BufferedReader in = null;
            PrintWriter out = null;
            Socket socket = null;

            logger.info("Starting up client");

            try {
                socket = new Socket("localhost", 7777);

                out = new PrintWriter(socket.getOutputStream(),
                        true);
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
            } catch (UnknownHostException e) {
                logger.info("Unknown host: localhost");
                System.exit(1);
            } catch (IOException e) {
                logger.info("No I/O");
                System.exit(1);
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(EchoServerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            logger.info("Now test the connection");
            while (true) {
                try {
                    // Write the data to the server
                    out.println("test");

                    line = in.readLine();

                    // Print the line ...
                    logger.log(Level.INFO, "Got: ''{0}''", line);
                } catch (IOException e) {
                    logger.info("Read failed");
                    System.exit(-1);
                }
            }


        }
    }
}