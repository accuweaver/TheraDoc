package com.hospira.theradoc;

import java.io.IOException;
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
public class TCPServerTest {

    private static TCPServer server = TCPServer.getInstance();
    private static TCPClient client;

    /**
     *
     */
    public TCPServerTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Setup - we need a server fired up, and a client to use in the tests ..
     * 
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        TCPServer.main(null);
        client = new TCPClient();
    }

    /**
     * In the tear down, we need to stop the server. 
     * 
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        server.stopServer();
    }

    /**
     * Test of startServer method, of class TCPServer.
     * 
     * @throws IOException 
     */
    @Test
    public void testTalkToServer() throws IOException {
        System.out.println("starServer");
        String result = client.talkToServer("hello");
        assertEquals("HELLO", result);
        assertEquals("QUIT", client.talkToServer("quit"));
        assertEquals("AGAIN", client.talkToServer("again"));  
    }

    /**
     * Test of stopServer method, of class TCPServer.
     * 
     * @throws Exception 
     */
    @Test
    public void testStopServer() throws Exception {
        System.out.println("stopServer");
        boolean expResult = false;
        boolean result = server.stopServer();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isListening method, of class TCPServer.
     * 
     */
    @Test
    public void testIsListening() {
        System.out.println("isListening");
        boolean expResult = false;
        boolean result = server.isListening();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setListening method, of class TCPServer.
     */
    @Test
    public void testSetListening() {
        System.out.println("setListening");
        boolean running = false;
        server.setListening(running);
        
    }
}